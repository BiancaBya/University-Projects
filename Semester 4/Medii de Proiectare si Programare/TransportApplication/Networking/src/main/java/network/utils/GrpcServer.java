package network.utils;

import io.grpc.stub.StreamObserver;
import network.grpcprotocol.TClientGrpcWorker;
import network.grpcprotocol.TransportProto;
import network.grpcprotocol.TransportServiceGrpc;
import services.IServices;

public class GrpcServer extends TransportServiceGrpc.TransportServiceImplBase{
    private final IServices server;
    public GrpcServer(final IServices server) {
        this.server = server;
    }

    @Override
    public StreamObserver<TransportProto.TransportRequest> connect(StreamObserver<TransportProto.TransportResponse> responseObserver) {
        return new TClientGrpcWorker(server, responseObserver);
    }
}
