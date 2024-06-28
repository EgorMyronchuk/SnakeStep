package snake.client;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Algo2 {
    private static final int EMPTY = 0;
    private static final int START = 1;
    private static final int OBSTACLE = -10;
    private final int width;
    private final int height;
    private final int[][] lockalBoard;
    private Board board;

    public Algo2(int width, int height , Board board) {
        this.width = width;
        this.height = height;
        this.lockalBoard = new int[height][width];
        this.board = board;
    }

    private int get(int x, int y) {
        return lockalBoard[y][x];
    }

    private void set(int x, int y, int value) {
        lockalBoard[y][x] = value;
    }

    private int get(Point p) {
        return get(p.getX(), p.getY());
    }

    private void set(Point p, int value) {
        set(p.getX(), p.getY(), value);
    }

    private boolean isOnBoard(Point p) {
        return p.getX() >= 0 && p.getX() < width && p.getY() >= 0 && p.getY() < height;
    }

    private boolean isUnvisited(Point p) {
        return get(p) == EMPTY;
    }

    private Supplier<Stream<Point>> deltas() {
        return () -> Stream.of(
                // actually just dx, dy
                PointImpl.pt(-1, 0),
                PointImpl.pt(0, -1),
                PointImpl.pt(1, 0),
                PointImpl.pt(0, 1)
        );
    }

    private Stream<Point> neighbours(Point p) {
        return deltas().get()
                .map(d ->  PointImpl.pt(d.getX() + p.getX(), d.getY() + p.getY()))
                .filter(this::isOnBoard);
    }

    private Stream<Point> neighboursUnvisited(Point p, List<Point> snakePosition, int steps) {
        Point futureTail = snakePosition.size() > steps ? snakePosition.get(snakePosition.size() - steps) : null;
        return neighbours(p)
                .filter(np -> isUnvisited(np) || (futureTail != null && np.equals(futureTail)));
    }



    private Stream<Point> neighboursByValue(Point pt, int value) {
        return neighbours(pt)
                .filter(p -> get(p) == value);
    }

    private void initializeBoard(Set<Point> obstacles) {
        IntStream.range(0, height).forEach(y ->
                IntStream.range(0, width).forEach(x ->
                        set(x, y, EMPTY)
                )
        );
        obstacles.forEach(p -> set(p, OBSTACLE));
    }
    private void stepChangesOnBoard (List<Point> snakePosition) {
        if (snakePosition.size() > 1) {
            set(snakePosition.getLast(), EMPTY);
            snakePosition.removeLast();
            System.out.println("Snake Board :" + snakePosition);
        }
    }

    public Optional<Iterable<Point>> trace(Point src, Set<Point> dstSet, Set<Point> obstacles, List<Point> snakePoints) {
        initializeBoard(obstacles);

        // 1. Заполняем доску
        int[] counter = {START};
        set(src, counter[0]);
        counter[0]++;
        boolean found = false;
        Point closestDst = null;
        for (Set<Point> curr = Set.of(src); !(found || curr.isEmpty()); counter[0]++) {
            Set<Point> next = curr.stream()
                    .flatMap(p -> neighboursUnvisited(p, snakePoints, counter[0]))
                    .collect(Collectors.toSet());
            next.forEach(p -> set(p, counter[0]));
            for (Point dst : dstSet) {
                if (next.contains(dst)) {
                    found = true;
                    closestDst = dst;
                    break;
                }
            }
            curr = next;
            // Симуляция движения змеи
            stepChangesOnBoard(snakePoints);
        }

        // 2. Обратная трассировка (восстановление пути)
        if (!found || closestDst == null) return Optional.empty();
        LinkedList<Point> path = new LinkedList<>();
        path.add(closestDst);
        counter[0]--;
        Point curr = closestDst;
        while (counter[0] > START) {
            counter[0]--;
            Point prev = neighboursByValue(curr, counter[0])
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Impossible!"));
            path.addFirst(prev);
            curr = prev;
        }
        return Optional.of(path);
    }



}
