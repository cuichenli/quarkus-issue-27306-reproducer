package interceptors;

import io.quarkus.arc.Priority;
import io.quarkus.grpc.GlobalInterceptor;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@GlobalInterceptor
@Priority(1000)
public class TracingServerInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        System.out.println("server interceptor");

        return serverCallHandler.startCall(serverCall, metadata);
    }
}
