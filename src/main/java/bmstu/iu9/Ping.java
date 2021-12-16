package bmstu.iu9;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.model.Query;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.http.javadsl.model.*;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import bmstu.iu9.CacheActor;
import bmstu.iu9.requests.Answer;
import bmstu.iu9.requests.Request;
import akka.stream.javadsl.Flow;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class Ping {

    private ActorRef cacheActor;

    private static final String URL_NAME_PARAM = "testUrl";
    private static final String DEFAULT_URL_NAME_PARAM = "";
    private static final String COUNT_PARAM = "count";
    private static final String DEFAULT_COUNT_PARAM = "-1";
    private static final Duration TIMEOUT = Duration.ofMillis(5000);
    private static final int ASYNC_NUMBER = 6;
    private static final Long ZERO= 0L;
    private AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();

    public Ping(ActorSystem system) {
        cacheActor = system.actorOf(Props.create(CacheActor.class));
    }

    private Sink<Request, CompletionStage<Long>> sink() {
        return Flow
                .<Request>create()
                .mapConcat((request) -> Collections.nCopies(request.getCount(), request.getUrl()))
                .mapAsync(ASYNC_NUMBER, (url) -> {
                    long startTime = System.nanoTime();
                    return asyncHttpClient
                            .prepareGet(url)
                            .execute()
                            .toCompletableFuture()
                            .thenApply((response -> System.nanoTime() - startTime));
                })
                .toMat(Sink.fold(ZERO, Long::sum), Keep.right());
    }

    private CompletionStage<Answer> ping(Request request, ActorMaterializer materializer) {
        return Source
                .from(Collections.singletonList(request))
                .toMat(sink(), Keep.right())
                .run(materializer)
                .thenApply((finalTime) -> new Answer(
                        request.getUrl(),
                        finalTime / request.getCount()
                ));
    }

    public Flow<HttpRequest, HttpResponse, NotUsed> httpFlow(ActorMaterializer materializer) {
        return Flow
                .of(HttpRequest.class)
                .map((request) -> {
                    Query requestQuery = request.getUri().query();
                    String url = requestQuery.getOrElse(URL_NAME_PARAM, DEFAULT_URL_NAME_PARAM);
                    int count = Integer.parseInt(requestQuery.getOrElse(COUNT_PARAM, DEFAULT_COUNT_PARAM));
                    return new Request(url, count);
                })
                .mapAsync(ASYNC_NUMBER, (request) -> Patterns.ask(cacheActor, request, TIMEOUT)
                        .thenCompose((requestResult) -> {
                            Answer cacheAnswer = (Answer) requestResult;
                            return cacheAnswer.getResponseTime() == -1
                                    ? ping(request, materializer)
                                    : CompletableFuture.completedFuture(cacheAnswer);
                        }))
                .map((result) -> {
                    cacheActor.tell(result, ActorRef.noSender());
                    return HttpResponse.create().withStatus(StatusCodes.OK)
                            .withEntity(
                                    HttpEntities.create(
                                            "URL: " + result.getUrl() + " RESPONSE TIME: " + result.getResponseTime()
                                    )
                            );
                });
    }

}
