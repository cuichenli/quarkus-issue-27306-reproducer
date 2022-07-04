package interceptors;

import io.grpc.*;
import io.quarkus.arc.Priority;
import io.quarkus.grpc.GlobalInterceptor;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@GlobalInterceptor
@Priority(1000)
public class TracingClientInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        System.out.println("client interceptor");
        return next.newCall(method, callOptions);
    }
}
