package rx.completable_future;

import lib.Runnables;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class Practice {

    public static void main(String[] args) throws InterruptedException {

        CompletionStage<String> completionStage = CompletableFuture.supplyAsync(() -> {
            Runnables.tryRun(() -> TimeUnit.SECONDS.sleep(1));
            return "shouldReplaceResultResult";
        });

        CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("Executing query in database");
                    Runnables.tryRun(() -> TimeUnit.SECONDS.sleep(2));
                    return "queryResult";
                })
                .thenApply((result) -> {
                    Runnables.tryRun(() -> TimeUnit.SECONDS.sleep(2));
                    return result + result;
                })
                .applyToEither(completionStage, (result) -> result + result)
                .thenAccept(System.out::println)
                .thenRun(() -> System.out.println("Cleaning"));

        System.out.println("This should print before query finishes");

        TimeUnit.SECONDS.sleep(10);


    }
}
