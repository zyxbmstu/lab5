package bmstu.iu9;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import bmstu.iu9.requests.Request;

import java.util.HashMap;
import java.util.Map;

public class CacheActor extends AbstractActor {

    private static final Long defaultValue = -1L;
    private Map<String, Long> cache = new HashMap<>();

    @Override
    public Receive createReseive() {
        return ReceiveBuilder.create()
                .match(Request.class, (request) -> {
                    Long requestResult = cache.getOrDefault()
                })
    }

}
