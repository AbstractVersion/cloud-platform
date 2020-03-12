package com.hallo.ivocation;

import com.hallo.ivocation.template.PythonAPITemplate;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableFeignClients
@RefreshScope
public class InvocationHalloServiceApplication {

    private static String runtimeServiceId;
    @Autowired
    Environment environment;
    @Autowired
    HelloClient client;
    @Autowired
    PythonClient pyClient;
    @Autowired
    DiscoveryClient eurekaClient;

    @RequestMapping("/")
    public ResponseEntity<?> hello() throws UnknownHostException {
        log.info("Requesting information from {}", "hello-service");
        return ResponseEntity.ok(
                new MessageInvocation(client.hello(),
                        runtimeServiceId,
                        InetAddress.getLocalHost().getHostName(),
                        Integer.parseInt(environment.getProperty("local.server.port")))
        );
    }

    @RequestMapping("/python/info")
    public ResponseEntity<?> pythonClient() throws UnknownHostException {
        log.info("Requesting information from {}", "python-service");
        return ResponseEntity.ok(
                new MessageInvocation(pyClient.info(),
                        runtimeServiceId,
                        InetAddress.getLocalHost().getHostName(),
                        Integer.parseInt(environment.getProperty("local.server.port")))
        );
    }

//    @RibbonClient(name = "hello-service", configuration = RibbonConfiguration.class)
    @FeignClient(name = "hello-service", configuration = FeignConfiguration.class)
    interface HelloClient {

        @GetMapping(value = "/", produces = "application/json")
        public HelloMessage hello();
    }

    @FeignClient(name = "py-app", configuration = FeignConfiguration.class)
    interface PythonClient {

        @GetMapping(value = "/api/info", produces = "application/json")
        public PythonAPITemplate info();
    }

    public static void main(String[] args) {
        runtimeServiceId = UUID.randomUUID().toString();
        SpringApplication.run(InvocationHalloServiceApplication.class, args);
    }

}
