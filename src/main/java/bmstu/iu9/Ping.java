package bmstu.iu9;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Ping {

    private ActorRef cacheActor;

    public Ping(ActorSystem system) {
        cacheActor = system.actorOf(Props.create(CacheActor.class));
    }

}
