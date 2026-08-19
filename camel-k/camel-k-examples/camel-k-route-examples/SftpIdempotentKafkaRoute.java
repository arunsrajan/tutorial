// camel-k: trait=security-context.enabled=false
// camel-k: dependency=camel:hazelcast
// camel-k: dependency=camel:flatpack
// camel-k: dependency=camel:kafka
// camel-k: dependency=camel:jacksonxml

import org.apache.camel.builder.RouteBuilder;
import com.hazelcast.config.Config;
import org.apache.camel.dataformat.flatpack.FlatpackDataFormat;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;

public class SftpIdempotentKafkaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // 1. Configure Hazelcast Client and Idempotent Repository
        ClientConfig config = new ClientConfig();
        config.setClusterName("dev");
        config.getNetworkConfig()
                .addAddress("hazelcast-camel.camel-k");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        HazelcastIdempotentRepository repo = new HazelcastIdempotentRepository(client, "idempotent-cache-map");
		
		//Config config = new Config();
        //config.setClusterName("dev");
        
        // Define TCP/IP configuration for external cluster
        //config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        //config.getNetworkConfig().getJoin().getTcpIpConfig()
        //      .setEnabled(false);
		//config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
        //  .setProperty("namespace", "camel-k")
        //  .setProperty("service-name", "hazelcast-camel");
		//HazelcastIdempotentRepository repo = new HazelcastIdempotentRepository(Hazelcast.newHazelcastInstance(config), "idempotent-cache-map");
		
        this.getCamelContext().getRegistry().bind("sftpIdempotentRepository", repo);

        // 2. Configure Flatpack DataFormat (HEADER=true allows header extraction)
        FlatpackDataFormat flatpack = new FlatpackDataFormat();
        flatpack.setDefinition("customers.pzmap.xml");
        flatpack.setFixed(false);
        flatpack.setIgnoreFirstRecord(true);

        // 3. SFTP Consumer -> Flatpack Unmarshal (CSV to Map/XML) -> Kafka Producer
        from("sftp://{{sftp.username}}@{{sftp.host}}:{{sftp.port}}/{{sftp.directory.download}}"
                + "?password={{sftp.password}}"
                + "&binary=true"
                + "&noop=true"
                + "&disconnect=true"
                + "&strictHostKeyChecking=no"
                + "&useUserKnownHostsFile=false"
                + "&idempotent=true"
                + "&idempotentRepository=#sftpIdempotentRepository"
                + "&idempotentKey=${file:name}-${file:size}"
                + "&move=./.done")
                .routeId("sftp-flatpack-kafka-route")
                .log("Downloaded CSV file from SFTP: ${header.CamelFileName}")

                // Unmarshal entire CSV content using Flatpack
                .unmarshal(flatpack)

                // Convert dataset to XML format
                .marshal().jacksonXml(true)

                // Send payload to Kafka topic deployed via Strimzi
				.to("sftp://{{sftp.username}}@{{sftp.host}}:{{sftp.port}}/{{sftp.directory.upload}}?password={{sftp.password}}&disconnect=true&strictHostKeyChecking=no&useUserKnownHostsFile=false")
                .to("kafka:customers-xml-topic?brokers=my-cluster-kafka-brokers.camel-k:9092");
    }
}
