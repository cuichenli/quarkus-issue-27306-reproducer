package io.quarkus.grpc.examples.hello;

import java.util.concurrent.atomic.AtomicInteger;

import io.opentelemetry.api.trace.Span;
import io.quarkus.grpc.GrpcClient;

import examples.Greeter;
import examples.HelloReply;
import examples.HelloRequest;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

@GrpcService
public class HelloWorldService implements Greeter {

    @GrpcClient("localhost")
    Greeter svc;

    AtomicInteger counter = new AtomicInteger();

    @Override
    // @Blocking
    public Uni<HelloReply> sayHello(HelloRequest request) {
        System.out.println("sayHello " + Span.current().getSpanContext().getTraceId());

        if (request.getName().equals("skip")) {
            return Uni.createFrom().item("Hello ")
                    .map(res -> {
                        System.out.println("skipped " + Span.current().getSpanContext().getTraceId());
                        return HelloReply.newBuilder().setMessage(res).build();
                    });
        }

        int count = counter.incrementAndGet();
        String name = request.getName();
        return svc.sayHello(HelloRequest.newBuilder().setName("skip").build())
                .map(res -> {
                    System.out.println("after calling sayHello " + Span.current().getSpanContext().getTraceId());
                    return HelloReply.newBuilder().setMessage("Hello " + name).setCount(count).build();
                });
    }
}
