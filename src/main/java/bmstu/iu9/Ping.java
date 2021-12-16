package bmstu.iu9;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.model.Query;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.http.javadsl.model.*;


public class Ping {

    private ActorRef cacheActor;

    private static final String URL_NAME = "url";

    public Ping(ActorSystem system) {
        cacheActor = system.actorOf(Props.create(CacheActor.class));
    }

    public Flow<HttpRequest, HttpResponse, NotUsed> httpFlow(ActorMaterializer materializer) {
        return Flow
                .of(HttpRequest.class)
                .map((request) -> {
                    Query requestQuery = request.getUri().query();
                    String url = requestQuery.getOrElse()
                })
    }

}
