package snake.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.List;

public class YourSolver implements Solver<Board> {

    Direction doSolve(Board board) {
        List<Point> barriers = board.getBarriers();
        List<Point> walls = board.getWalls();
        List<Point> snake = board.getSnake();
        Point apple = board.getApples().get(0);
        Point head = board.getHead();

        if      (apple.getX() < head.getX()) return Direction.LEFT;
        else if (apple.getX() > head.getX()) return Direction.RIGHT;
        else if (apple.getY() > head.getY()) return Direction.UP;
        else  return Direction.DOWN;
    }

    @Override
    public String get(Board board) {
        return doSolve(board).toString();
    }

    public static void main(String[] args) {
        String url = "http://138.197.189.109/codenjoy-contest/board/player/9bt11l280hhkgd085i1i?code=3275607761448940229";

        WebSocketRunner.runClient(
                url,
                new YourSolver(),
                new Board());
    }

}
