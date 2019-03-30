package pl.longhorn.komwojazWorker;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Servers {

    private List<Server> servers = new LinkedList<>();
    private Random random = new Random();

    public void createServer() {
        servers.add(new Server());
    }

    public void addServer(String path) {
        servers.add(new Server(path));
    }

    public void examinate(Solution solution, List<TargetPoint> targetPoints) {
        InputData inputData = new InputData(solution, targetPoints);
        servers.get(random.nextInt(servers.size())).examinate(inputData);
    }
}
