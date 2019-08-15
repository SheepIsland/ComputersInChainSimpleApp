import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SimpleClient implements Client{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleClient.class);
    private final int port;

    private Socket socket;

    public SimpleClient(final int port){
        this.port = port;
    }

    @Override
    public void startConnection(final String IP) {
        try {
            socket = new Socket(IP, port);
        } catch (final IOException e) {
            LOG.debug("Error when initializing connection", e);
        }
    }

    @Override
    public void sendMessage() {
        try (final DataOutputStream out = new DataOutputStream(socket.getOutputStream())){
            out.writeInt(1);
            out.close();
        } catch (final Exception e) {
            LOG.info("Error while sending message", e);
        }
    }

    @Override
    public void stopConnection() {
        try {
            socket.close();
        } catch (final Exception e) {
            LOG.debug("Error when closing", e);
        }
    }
}
