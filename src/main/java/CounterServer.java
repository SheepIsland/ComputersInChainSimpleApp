
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.io.*;

public class CounterServer implements ConsistentServer{
    private static final Logger LOG = LoggerFactory.getLogger(CounterServer.class);
    private static final int TIMEOUT = 500000;

    private static int leftCout = 0;
    private static int rightCount = 0;

    private ServerSocket serverSocket;

    @Override
    public void start(final int port, final String leftIP, final String rightIP) {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(TIMEOUT);
            while (!serverSocket.isClosed()) {
                new ClientHandler(serverSocket.accept(), port, leftIP, rightIP).start();
                LOG.info("Current count: {}", leftCout + rightCount + 1);
            }
        } catch (final IOException e) {
            LOG.debug(e.getMessage());
        } finally {
            LOG.info("Count: {}", leftCout + rightCount + 1);
            stop();
        }
    }

    @Override
    public void stop() {
        try {
            serverSocket.close();
        } catch (final IOException e) {
            LOG.debug(e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private final int port;
        private final String leftIP;
        private final String rightIP;

        public ClientHandler(final Socket socket, final int port, final String leftIP, final String rightIP) {
            this.clientSocket = socket;
            this.port = port;
            this.leftIP = leftIP;
            this.rightIP = rightIP;
        }

        private void sendMessageWithCurrentCount(final String IP, final int count) {
            try (final Socket leftSocket =  new Socket(IP, port)){
                try (final DataOutputStream out = new DataOutputStream(leftSocket.getOutputStream())){
                    out.writeInt(count);
                    out.close();
                };
            } catch (final IOException e) {
                LOG.error("Error while sending msg from server", e);
            }
        }

        @Override
        public void run() {
            try(final DataInputStream in = new DataInputStream(clientSocket.getInputStream())) {

                final int input = in.readInt();

                if (clientSocket.getInetAddress().getHostAddress().equals(leftIP)) {
                    if (input > leftCout) {
                        leftCout = input;
                    }
                    final int leftCur = leftCout + 1;
                    sendMessageWithCurrentCount(rightIP, leftCur);
                } else {
                    if (input > rightCount) {
                        rightCount = input;
                    }
                    final int rightCur = rightCount + 1;
                    sendMessageWithCurrentCount(leftIP, rightCur);
                }

                in.close();
                clientSocket.close();

            } catch (final Exception e) {
                LOG.debug(e.getMessage());
            }
        }
    }
}