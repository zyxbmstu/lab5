package bmstu.iu9;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

public class CacheActor extends AbstractActor {

    @Override
    public Receive createReseive() {
        return ReceiveBuilder.create()
                .match()
    }

}
