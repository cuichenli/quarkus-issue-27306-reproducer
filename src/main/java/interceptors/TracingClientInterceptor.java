package interceptors;

import io.grpc.*;
import io.quarkus.arc.Priority;
import io.quarkus.grpc.GlobalInterceptor;

import javax.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@GlobalInterceptor
@Priority(1000)
public class TracingClientInterceptor implements ClientInterceptor {
    public static Logger LOGGER = LoggerFactory.getLogger(TracingClientInterceptor.class);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        LOGGER.info("client interceptor");
        return next.newCall(method, callOptions);
    }
}
