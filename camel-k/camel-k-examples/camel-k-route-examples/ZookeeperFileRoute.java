// camel-k: dependency=camel:zookeeper-master

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.zookeepermaster.MasterComponent;


public class ZookeeperFileRoute extends RouteBuilder {	
	@Override
	public void configure() throws Exception {
		System.setProperty("zookeeper.sasl.client", "false");
		MasterComponent master = new MasterComponent();
		master.setZooKeeperUrl("10.244.2.25:2181");
		this.getCamelContext().removeComponent("zookeeper-master");
		this.getCamelContext().addComponent("zookeeper-master", master);
		from("zookeeper-master:fileEndpointGroup:file:/mnt/hostpath/camel-in")
		.log("Successfully downloaded file: ${header.CamelFileName}")
		.to("file:/mnt/hostpath/camel-out");		
	}

}
