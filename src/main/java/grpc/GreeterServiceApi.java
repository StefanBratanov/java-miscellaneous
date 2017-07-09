package grpc;

import io.grpc.stub.StreamObserver;

public class GreeterServiceApi extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply helloReply = HelloReply.newBuilder()
                .setMessage("Hello " + request.getName()).build();

        responseObserver.onNext(helloReply);
        responseObserver.onCompleted();


    }
}
