import org.apache.camel.builder.RouteBuilder;

public class SftpRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        
        // 1. CONSUMER: Poll files from an SFTP server
        from("sftp://{{sftp.username}}@{{sftp.host}}:{{sftp.port}}/{{sftp.directory.download}}"
             + "?password={{sftp.password}}"
             + "&binary=true"
             + "&disconnect=true"
             + "&idempotent=true"
			 + "&strictHostKeyChecking=no"
			 + "&useUserKnownHostsFile=false"
             + "&readLock=changed") // Ensures file is fully written before reading
        .routeId("sftp-to-log")
        .log("Successfully downloaded file: ${header.CamelFileName}")
        
        // 2. PROCESSING: Send payload to a local target or processing queue
        .to("sftp://{{sftp.username}}@{{sftp.host}}:{{sftp.port}}/{{sftp.directory.upload}}?password={{sftp.password}}&disconnect=true&strictHostKeyChecking=no&useUserKnownHostsFile=false");
    }
}

