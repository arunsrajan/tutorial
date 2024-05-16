// camel-k: language=java
import org.apache.camel.builder.RouteBuilder;

public class MyRouting extends RouteBuilder {

  @Override
  public void configure() throws Exception {

      from("timer:java?period=3000")
        .id("generator")
        .setBody().constant("HelloWorld!!!")
        .log("${body}");
      
      rest()
      .get("/hello")
      .to("direct:hello");
      
      from("direct:hello")
      .id("undertow")
      .setBody().constant("<h1>HelloWorld From HTTP URL!!!</h1>")
      .log("${body}");
        
      
  }

}
