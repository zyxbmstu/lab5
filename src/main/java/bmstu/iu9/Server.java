package bmstu.iu9;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.scaladsl.model.HttpRequest;
import akka.http.scaladsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.io.IOException;

public class Server {

    private static final String ACTOR_SYSTEM_NAME = "routes";

    public static void main(String[] args) throws IOException {
        System.out.println("Start!");

        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Ping ping = new Ping(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = ping.;//METHOD
    }

}
