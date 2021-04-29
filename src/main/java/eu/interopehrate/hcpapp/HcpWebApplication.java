package eu.interopehrate.hcpapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"eu.interopehrate.ihs.terminalclient", "eu.interopehrate.hcpapp"})
public class HcpWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(HcpWebApplication.class, args);
    }
}
