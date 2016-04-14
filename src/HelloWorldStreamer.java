import java.io.File;
import com.streamhub.api.PushServer;
import com.streamhub.nio.NIOServer;
import com.streamhub.api.Client;
import com.streamhub.api.SubscriptionListener;
import com.streamhub.api.JsonPayload;

public class HelloWorldStreamer implements SubscriptionListener {
public static void main(String[] args) throws Exception {
  new HelloWorldStreamer();
}

public HelloWorldStreamer() throws Exception {
  PushServer server = new NIOServer(9080);
  server.addStaticContent(new File("."));
  server.start();
  server.getSubscriptionManager().addSubscriptionListener(this);
  ContinuousStreamer continuousStreamer = new ContinuousStreamer(server);
  new Thread(continuousStreamer).start();
  System.out.println("Comet server started at http://localhost:7878/.");
  System.out.println("Press any key to stop...");
  System.in.read();
  continuousStreamer.stop();
  server.stop();
}

public void onSubscribe(String topic, Client client) {
  JsonPayload payload = new JsonPayload(topic);
  payload.addField("Response", "Hello World! Initial Response");
  client.send(topic, payload);

}

public void onUnSubscribe(String topic, Client client) {

}

private static class ContinuousStreamer implements Runnable {
  private boolean isStopped = false;
  private int updateNumber = 0;
  private final PushServer server;

  public ContinuousStreamer(PushServer server) {
      this.server = server;
  }

  public void run() {
      while (!isStopped) {
          JsonPayload payload = new JsonPayload("HelloWorld");
          payload.addField("Response", "Hello World! Response number " + updateNumber++);
          server.publish("HelloWorld", payload);

          try {
              // Sleep for one second (1000ms)
              Thread.sleep(1000);
          } catch (InterruptedException e) {}
      }
  }

  public void stop() {
      isStopped = true;
  }
}
}