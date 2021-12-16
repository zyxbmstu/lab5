package bmstu.iu9;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Ping {

    private ActorRef cacheActor;

    public Ping(ActorSystem system) {
        cacheActor = system.actorOf(Props.create(CacheActor.class));
    }

    public Flow<HttpRequest, HttpResponse, NotUsed> httpFlow(ActorMaterializer materializer) {
        return 
    }

}
