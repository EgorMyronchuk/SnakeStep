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
    Algo3 algo3 = new Algo3(15 , 15);
    Direction doSolve(Board board) {
        Set<Point> obstaclesToPath = new HashSet<>();
        obstaclesToPath.addAll(board.getStones());
        obstaclesToPath.addAll(board.getWalls());
        if(board.getSnake().size() > 1 ) {
            List<Point> snake = board.getSnake();
            snake.removeLast();
            obstaclesToPath.addAll(snake);
        }
        Set<Point> allApples = new HashSet<>(board.getApples());
        Optional<Iterable<Point>> result = algo3.trace(board.getHead() , allApples , obstaclesToPath);

        System.out.println(result);
        System.out.println();
        System.out.println(algo3);
        System.out.println();
       return getDirection(result);
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
