package bmstu.iu9;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import bmstu.iu9.requests.Answer;
import bmstu.iu9.requests.Request;

import java.util.HashMap;
import java.util.Map;

public class CacheActor extends AbstractActor {

    private static final Long DEFAULT_VALUE = -1L;
    private final Map<String, Long> cache = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(Request.class, (request) -> {
                    Long requestResult = cache.getOrDefault(request.getUrl(), DEFAULT_VALUE);
                    sender().tell(new Answer(request.getUrl(), requestResult), self());
                })
                .match(Answer.class, (requestAnswer) ->
                    cache.put(requestAnswer.getUrl(), requestAnswer.getResponseTime())
                )
                .build();
    }

}
