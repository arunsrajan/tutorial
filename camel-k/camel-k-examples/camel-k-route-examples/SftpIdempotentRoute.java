// camel-k: dependency=camel:hazelcast
// camel-k: dependency=camel:flatpack
// camel-k: dependency=camel:kafka
// camel-k: dependency=camel:jacksonxml

import org.apache.camel.builder.RouteBuilder;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;

public class SftpIdempotentRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
		//Config config = new Config();
        //config.setClusterName("hazelcast-camel");
        
        // Define TCP/IP configuration for external cluster
        //config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        //config.getNetworkConfig().getJoin().getTcpIpConfig()
        //      .setEnabled(false);
		//config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
        //  .setProperty("namespace", "camel-k")
        //  .setProperty("service-name", "hazelcast-camel");
		//HazelcastIdempotentRepository repo = new HazelcastIdempotentRepository(Hazelcast.newHazelcastInstance(config), "idempotent-cache-map");
		ClientConfig config = new ClientConfig();
		config.setClusterName("dev");

		// Add Kubernetes internal DNS name
		config.getNetworkConfig()
			.addAddress("hazelcast-camel:5701")
			.setSmartRouting(false); // Disable smart routing for service proxies			
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
		HazelcastIdempotentRepository repo = new HazelcastIdempotentRepository(client, "idempotent-cache-map");
        // 3. Register it to the Camel Registry
        this.getCamelContext().getRegistry().bind("sftpIdempotentRepository", repo);
        // 1. CONSUMER: Poll files from an SFTP server
        from("sftp://{{sftp.username}}@{{sftp.host}}:{{sftp.port}}/{{sftp.directory.download}}"
             + "?password={{sftp.password}}"
             + "&binary=true"
			 + "&noop=true"
             + "&disconnect=true"
			 + "&strictHostKeyChecking=no"
			 + "&useUserKnownHostsFile=false"
			 + "&idempotent=true" // Enables the idempotent consumer check
             + "&idempotentRepository=#sftpIdempotentRepository" // Points to our Bean
             + "&idempotentKey=${file:name}-${file:size}" // Unique identifier strategy
             + "&move=./.done")
        .routeId("sftp-to-log")
        .log("Successfully downloaded file: ${header.CamelFileName}")
        
        // 2. PROCESSING: Send payload to a local target or processing queue
        .to("sftp://{{sftp.username}}@{{sftp.host}}:{{sftp.port}}/{{sftp.directory.upload}}?password={{sftp.password}}&disconnect=true&strictHostKeyChecking=no&useUserKnownHostsFile=false");
    }
}

