package bmstu.iu9;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import bmstu.iu9.requests.Request;

public class CacheActor extends AbstractActor {

    private static final Long = default

    @Override
    public Receive createReseive() {
        return ReceiveBuilder.create()
                .match(Request.class, (request) -> {
                    Long
                })
    }

}
