// camel-k: language=java

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.FileComponent;

public class BasicFileTransfer extends RouteBuilder {
  @Override
  public void configure() throws Exception {
	   from("{{source}}")
        .setHeader("example")
          .constant("Java")		
		.log("${body}")
        .to("{{destination}}");
      
  }
}
