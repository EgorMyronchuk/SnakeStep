package snake.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static snake.client.Decoder.PathChecker;
import static snake.client.Decoder.getDirection;

public class YourSolver implements Solver<Board> {
    Board board = new Board();
    Algo1 algo = new Algo1(15 , 15);
    Algo2 algo2 = new Algo2(15 , 15 , board);
    Direction doSolve(Board board) {
        Set<Point> applesSet = new HashSet<>(board.getApples());
        Set<Point> barriersSet = new HashSet<>(board.getBarriers());
        return getDirection(algo2.trace(board.getHead(), applesSet , barriersSet , board.getSnake() ));
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
