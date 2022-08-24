package interceptors;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.smallrye.mutiny.subscription.UniSubscription;

public class UniInterceptor implements io.smallrye.mutiny.infrastructure.UniInterceptor {
    static public OpenTelemetry opentelemetry = GlobalOpenTelemetry.get();

    @Override
    public <T> UniSubscriber<? super T> onSubscription(Uni<T> instance,
        UniSubscriber<? super T> subscriber) {
        var parentContext = Span.current();

        return new UniSubscriber<T>() {
            @Override
            public void onSubscribe(UniSubscription subscription) {
                subscriber.onSubscribe(subscription);
            }

            @Override
            public void onItem(T item) {
                var s = UniInterceptor.opentelemetry.getTracer("mutiny").spanBuilder("onItem")
                    .setSpanKind(
                        SpanKind.INTERNAL)
                    .setParent(io.opentelemetry.context.Context.current().with(parentContext))
                    .startSpan();
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
