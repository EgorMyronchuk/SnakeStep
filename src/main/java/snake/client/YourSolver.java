package snake.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class YourSolver implements Solver<Board> {
    private final HamiltonianPath hamiltonianPath;

    public YourSolver(int xSize, int ySize) {
        this.hamiltonianPath = new HamiltonianPath(xSize, ySize);
        this.hamiltonianPath.createHamiltonPath();
    }

    @Override
    public String get(Board board) {
        Point head = board.getHead();
        Direction nextDirection = getNextDirection(head);
        return nextDirection.toString();
    }

    private Direction getNextDirection(Point head) {
        List<Point> path = hamiltonianPath.getPath();
        int index = path.indexOf(head);
        if (index == -1 || index == path.size() - 1) {
            return Direction.STOP;  // Если голову не нашли или это последний элемент пути
        }
        Point nextPoint = path.get((index + 1) % path.size());
        return directionFromTo(head, nextPoint);
    }

    private Direction directionFromTo(Point from, Point to) {
        if (to.getX() == from.getX() && to.getY() == from.getY() - 1) {
            return Direction.UP;
        }
        if (to.getX() == from.getX() && to.getY() == from.getY() + 1) {
            return Direction.DOWN;
        }
        if (to.getX() == from.getX() - 1 && to.getY() == from.getY()) {
            return Direction.LEFT;
        }
        if (to.getX() == from.getX() + 1 && to.getY() == from.getY()) {
            return Direction.RIGHT;
        }
        return Direction.STOP;  // Непредвиденное направление
    }

    public static void main(String[] args) {
        String url = "http://138.197.189.109/codenjoy-contest/board/player/9bt11l280hhkgd085i1i?code=3275607761448940229";
        WebSocketRunner.runClient(
                url,
                new YourSolver(15, 15),
                new Board());
    }
}
