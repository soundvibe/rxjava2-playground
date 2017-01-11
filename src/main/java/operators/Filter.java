package operators;

import io.reactivex.FlowableOperator;
import org.reactivestreams.*;

import java.util.function.Predicate;

/**
 * @author linas on 17.1.12.
 */
public class Filter<Downstream> implements FlowableOperator<Downstream, Downstream> {

    private final Predicate<Downstream> predicate;

    private Filter(Predicate<Downstream> predicate) {
        this.predicate = predicate;
    }

    public static <Downstream> Filter<Downstream> filter(Predicate<Downstream> predicate) {
        return new Filter<>(predicate);
    }

    @Override
    public Subscriber<? super Downstream> apply(Subscriber<? super Downstream> observer) throws Exception {
        return new FilterSubscriber<>(predicate, observer);

    }

    static final class FilterSubscriber<Downstream> implements Subscriber<Downstream>, Subscription {

        private final Predicate<Downstream> predicate;
        private final Subscriber<? super Downstream> child;
        private Subscription s;

        FilterSubscriber(Predicate<Downstream> predicate, Subscriber<? super Downstream> child) {
            this.predicate = predicate;
            this.child = child;
        }

        @Override
        public void onSubscribe(Subscription s) {
            this.s = s;
            this.child.onSubscribe(s);
        }

        @Override
        public void onNext(Downstream t) {
            if (predicate.test(t)) {
                child.onNext(t);
            }
        }

        @Override
        public void onError(Throwable t) {
            child.onError(t);
        }

        @Override
        public void onComplete() {
            child.onComplete();
        }

        @Override
        public void request(long n) {
            s.request(n);
        }

        @Override
        public void cancel() {
            s.cancel();
        }
    }
}
