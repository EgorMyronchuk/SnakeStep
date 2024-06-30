package snake.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

public class YourSolver implements Solver<Board> {
    private final HamiltonianPath hamiltonianPath;

    public YourSolver() {
        hamiltonianPath = HamiltonianPath.getInstance();  // Используем Singleton
    }

    @Override
    public String get(Board board) {
        Point head = board.getHead();
        Point correctedHead = new PointImpl(head.getX() - 1, head.getY() - 1);
        return getNextDirection(correctedHead).toString();
    }

    private Direction getNextDirection(Point head) {
        return hamiltonianPath.getNextDirection(head);
    }

    public static void main(String[] args) {
        String url = "http://138.197.189.109/codenjoy-contest/board/player/9bt11l280hhkgd085i1i?code=3275607761448940229";
        WebSocketRunner.runClient(
                url,
                new YourSolver(),
                new Board());
    }
}
