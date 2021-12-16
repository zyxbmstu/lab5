package bmstu.iu9;

import akka.actor.ActorSystem;
import akka.http.scaladsl.Http;

import java.io.IOException;

public class Server {

    private static final String ACTOR_SYSTEM_NAME = "routes";

    public static void main(String[] args) throws IOException {
        System.out.println("Start!");

        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        final Http
    }

}
