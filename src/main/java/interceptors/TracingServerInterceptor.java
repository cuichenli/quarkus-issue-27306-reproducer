package interceptors;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.instrumentation.grpc.v1_6.GrpcTracing;
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
    private final ServerInterceptor tracing =  GrpcTracing.create(GlobalOpenTelemetry.get()).newServerInterceptor();

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        return tracing.interceptCall(serverCall, metadata, serverCallHandler);
    }
}
