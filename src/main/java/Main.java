import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final int PORT = 5050;

    public static void main(final String... args) {
        if (args.length == 2) {
            start(args[0], args[1]);
        } else if (args.length == 3){
            start(Integer.parseInt(args[0]), args[1], args[3]);
        } else {
            LOG.error("Arguments must be set like: left_port right_port config");
        }
    }

    private static void start(final int port, final String leftIP, final String rightIP){
        final ArrayList<Thread> threads = new ArrayList<>();

        final Thread server = new Thread() {
            @Override
            public void run() {
                LOG.info("Runnig server");
                new CounterServer().start(port, leftIP, rightIP);
            }
        };
        threads.add(server);

        final Thread client = new Thread() {
            @Override
            public void run() {
                final Client client = new SimpleClient(port);
                LOG.info("Sending simple message");
                client.startConnection(leftIP);
                client.sendMessage();
                client.stopConnection();
                client.startConnection(rightIP);
                client.sendMessage();
                client.stopConnection();
            }
        };
        threads.add(client);

        final ExecutorService exec = Executors.newCachedThreadPool();
        threads.forEach(exec::execute);
    }

    private static void start(final String leftIP, final String rightIP){
        start(PORT, leftIP, rightIP);
    }
}
