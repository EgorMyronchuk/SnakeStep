package snake.client;

import a09lee.colored.Ansi;
import a09lee.colored.Attribute;
import a09lee.colored.Colored;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Algo2Changes {
    private static final int EMPTY = 0;
    private static final int START = 1;
    private static final int OBSTACLE = -10;
    private final int width;
    private final int height;
    private final int[][] board;

    public Algo2Changes(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new int[height][width];
    }

    private int get(int x, int y) {
        return board[y][x];
    }

    private void set(int x, int y, int value) {
        board[y][x] = value;
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

    private Stream<Point> neighboursUnvisited(Point p) {
        return neighbours(p)
                .filter(this::isUnvisited);
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

    public Optional<Iterable<Point>> trace(Point src, Set<Point> dstSet, Set<Point> obstacles) {
        initializeBoard(obstacles);

        // 1. fill the board
        int[] counter = {START};
        set(src, counter[0]);
        counter[0]++;
        boolean found = false;
        Point closestDst = null;
        for (Set<Point> curr = Set.of(src); !(found || curr.isEmpty()); counter[0]++) {
            Set<Point> next = curr.stream()
                    .flatMap(this::neighboursUnvisited)
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
        }

        // 2. backtrack (reconstruct path)
        if (!found || closestDst == null) return Optional.empty();
        LinkedList<Point> path = new LinkedList<>();
        path.add(closestDst);
        counter[0]--;
        Point curr = closestDst;
        while (counter[0] > START) {
            counter[0]--;
            Point prev = neighboursByValue(curr, counter[0])
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("impossible!"));
            path.addFirst(prev);
            curr = prev;
        }
        return Optional.of(path);
    }

    //------------------------------------------------------------------------------------
    String cellFormatted(Point p, Set<Point> path) {
        int value = get(p);
        String valueF = String.format("%3d", value);

        if (value == OBSTACLE) {
            Attribute a = new Attribute(Ansi.ColorFont.BLUE);
            return Colored.build(" XX", a);
        }

        if (path.isEmpty()) return valueF;

        if (path.contains(p)) {
            Attribute a = new Attribute(Ansi.ColorFont.RED);
            return Colored.build(valueF, a);
        }

        return valueF;
    }

    public String boardFormatted(Iterable<Point> path0) {
        Set<Point> path = StreamSupport
                .stream(path0.spliterator(), false)
                .collect(Collectors.toSet());
        return IntStream.range(0, height)
                .mapToObj(y -> height - 1 - y)  // изменено для зеркалирования по вертикали
                .map(y ->
                        IntStream.range(0, width).mapToObj(x -> PointImpl.pt(x, y))
                                .map(p -> cellFormatted(p, path))
                                .collect(Collectors.joining())
                )
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String toString() {
        return boardFormatted(Set.of());
    }
}
