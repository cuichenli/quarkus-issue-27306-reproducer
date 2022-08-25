package interceptors;

import io.quarkus.arc.Priority;
import io.quarkus.grpc.GlobalInterceptor;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

import javax.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@GlobalInterceptor
@Priority(1000)
public class TracingServerInterceptor implements ServerInterceptor {
    public static Logger LOGGER = LoggerFactory.getLogger(TracingServerInterceptor.class);
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        LOGGER.info("server interceptor");

        return serverCallHandler.startCall(serverCall, metadata);
    }
}
