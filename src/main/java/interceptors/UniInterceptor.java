package interceptors;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.smallrye.mutiny.subscription.UniSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniInterceptor implements io.smallrye.mutiny.infrastructure.UniInterceptor {

    public static Logger LOGGER = LoggerFactory.getLogger(UniInterceptor.class);


    @Override
    public <T> UniSubscriber<? super T> onSubscription(Uni<T> instance,
        UniSubscriber<? super T> subscriber) {
        var parentContext = Span.current();

        return new UniSubscriber<T>() {
            public final OpenTelemetry opentelemetry = GlobalOpenTelemetry.get();

            @Override
            public void onSubscribe(UniSubscription subscription) {
                subscriber.onSubscribe(subscription);
            }

            @Override
            public void onItem(T item) {
                var c = Context.current();
                Span s;
                if (Span.current().getSpanContext().isValid()) {
                    s = Span.current();
                } else {
                    s = opentelemetry.getTracer("io.quarkus.opentelemetry")
                        .spanBuilder("onItem")
                        .setSpanKind(
                            SpanKind.SERVER)
                        .setParent(parentContext.storeInContext(c))
                        .startSpan();
                }
                try (var scope = s.makeCurrent()) {
                    subscriber.onItem(item);
                } finally {
                    s.end();
                }
            }

            @Override
            public void onFailure(Throwable failure) {
                subscriber.onFailure(failure);
            }
        };
    }
}
