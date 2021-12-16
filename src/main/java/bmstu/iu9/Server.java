package bmstu.iu9;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class Server {

    private static final String ACTOR_SYSTEM_NAME = "routes";
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {

        System.out.println("Start!");

        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Ping ping = new Ping(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = ping.httpFlow(materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HOST, PORT),
                materializer
        );
        System.out.println("Server: " + HOST + ":" + PORT);
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbind -> system.terminate());
    }

}
