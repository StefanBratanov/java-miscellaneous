package grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lib.Runnables;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class GrpcServer {

    private Server server;

    public GrpcServer(int port) {
        server = ServerBuilder.forPort(port)
                .addService(new GreeterServiceApi())
                .build();
    }

    public void start() {
        Runnables.tryRun(server::start);
        log.info("Starting server on port {}", server.getPort());
    }

    public void shutdown() throws InterruptedException {
        server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }


}
