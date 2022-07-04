package io.quarkus.grpc.examples.hello;

import java.util.concurrent.atomic.AtomicInteger;

import io.opentelemetry.context.Context;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import org.jboss.logging.Logger;

import examples.Greeter;
import examples.HelloReply;
import examples.HelloRequest;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

@GrpcService
public class HelloWorldService implements Greeter {

    AtomicInteger counter = new AtomicInteger();

    @Override
    @Blocking
    public Uni<HelloReply> sayHello(HelloRequest request) {
        SpanContext spanContext = Span.current().getSpanContext();
        System.out.println("sayHello "+spanContext);

        int count = counter.incrementAndGet();
        String name = request.getName();
        return Uni.createFrom().item("Hello " + name)
                .map(res -> HelloReply.newBuilder().setMessage(res).setCount(count).build());
    }
}
