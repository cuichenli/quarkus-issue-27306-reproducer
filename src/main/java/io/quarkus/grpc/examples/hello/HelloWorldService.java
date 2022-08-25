package io.quarkus.grpc.examples.hello;

import java.util.concurrent.atomic.AtomicInteger;

import io.opentelemetry.api.trace.Span;
import io.quarkus.grpc.GrpcClient;

import examples.Greeter;
import examples.HelloReply;
import examples.HelloRequest;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class HelloWorldService implements Greeter {
    public static final org.slf4j.Logger Logger = LoggerFactory.getLogger(HelloWorldService.class);

    @GrpcClient("hello")
    Greeter svc;

    AtomicInteger counter = new AtomicInteger();

    @Override
    // @Blocking
    public Uni<HelloReply> sayHello(HelloRequest request) {
        Logger.info("sayHello " + Span.current().getSpanContext().getTraceId());

        if (request.getName().equals("skip")) {
            return Uni.createFrom().item("Hello ")
                    .map(res -> {
                        Logger.info("skipped " + Span.current().getSpanContext().getTraceId());
                        return HelloReply.newBuilder().setMessage(res).build();
                    });
        }

        int count = counter.incrementAndGet();
        String name = request.getName();
        return svc.sayHello(HelloRequest.newBuilder().setName("skip").build())
                .map(res -> {
                    Logger.info("after calling sayHello " + Span.current().getSpanContext().getTraceId());
                    return HelloReply.newBuilder().setMessage("Hello " + name).setCount(count).build();
                });
    }
}
