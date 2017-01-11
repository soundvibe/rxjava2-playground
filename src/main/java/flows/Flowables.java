package flows;

import io.reactivex.*;

import static operators.Filter.filter;

/**
 * @author linas on 17.1.11.
 */
public class Flowables {

    public static void main(String[] args) {
        Flowable.just("bar","foo")
                .lift(filter(o -> o.equals("foo")))
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("completed"));
    }


}
