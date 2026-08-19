import org.apache.camel.builder.RouteBuilder;

public class FileRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {		
		from("file:/mnt/hostpath/camel-in")
		.to("file:/mnt/hostpath/camel-out");		
	}

}
