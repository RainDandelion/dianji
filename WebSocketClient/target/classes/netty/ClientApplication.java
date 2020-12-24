package netty;

import netty.Server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class ClientApplication implements CommandLineRunner {
    @Autowired
    private NettyServer server;
    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        server.run(9999);
    }
}
