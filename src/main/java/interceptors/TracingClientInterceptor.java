package interceptors;

import io.grpc.*;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.instrumentation.grpc.v1_6.GrpcTracing;
import io.quarkus.grpc.GlobalInterceptor;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@GlobalInterceptor
public class TracingClientInterceptor implements ClientInterceptor {
    private final ClientInterceptor tracing = GrpcTracing.create(GlobalOpenTelemetry.get()).newClientInterceptor();

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return tracing.interceptCall(method, callOptions, next);
    }
}
