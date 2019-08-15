
public interface ConsistentServer {
    void start(int port, String leftIP, String rightIP);
    void stop();
}
