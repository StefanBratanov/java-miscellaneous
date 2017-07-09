package grpc;

import lib.Runnables;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestGrpc {

    public static void main(String[] args) {

        GrpcServer server = new GrpcServer(9700);
        server.start();

        GreeterClient client = new GreeterClient("localhost", 9700);

        String reply = client.getGreetingFor("Bob");

        log.info("Reply : {}", reply);

        Runnables.tryRun(() -> {
            server.shutdown();
            client.shutdown();
        });

    }
}
