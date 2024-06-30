package snake.client;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Direction;

import java.util.ArrayList;
import java.util.List;

public class HamiltonianPath {
    private static HamiltonianPath instance;
    private final int xSize;
    private final int ySize;
    private final List<Point> hamiltonPath;

    private HamiltonianPath() {
        this.xSize = 16;
        this.ySize = 16;
        this.hamiltonPath = new ArrayList<>();
        createHamiltonPath();
        System.out.println("created Hamiltonian path");
        System.out.println(hamiltonPath.toString());
    }

    public static synchronized HamiltonianPath getInstance() {
        if (instance == null) {
            instance = new HamiltonianPath();
        }
        return instance;
    }

    public void createHamiltonPath() {
        hamiltonPath.clear();
        hamiltonPath.add(new PointImpl(0, 0));
        if (!hamiltonStep(new PointImpl(0, 0))) {
            throw new RuntimeException("Failed to find a Hamiltonian path");
        }
    }

    public List<Point> getPath() {
        return hamiltonPath;
    }

    public Direction getNextDirection(Point head) {
        int index = hamiltonPath.indexOf(head);
        if (index == -1 || index == hamiltonPath.size() - 1) {
            return Direction.UP;  // Если голову не нашли или это последний элемент пути
        }
        Point nextPoint = hamiltonPath.get((index + 1) % hamiltonPath.size());
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

    private boolean hamiltonStep(Point current) {
        if (hamiltonPath.size() == xSize * ySize) {
            Point first = hamiltonPath.get(0);
            return (first.getX() == current.getX() && first.getY() == current.getY() - 1)
                    || (first.getX() == current.getX() && first.getY() == current.getY() + 1)
                    || (first.getX() - 1 == current.getX() && first.getY() == current.getY())
                    || (first.getX() + 1 == current.getX() && first.getY() == current.getY());
        }

        for (DirectionsForStep direction : DirectionsForStep.values()) {
            Point newElement = switch (direction) {
                case Up -> new PointImpl(current.getX(), current.getY() - 1);
                case Right -> new PointImpl(current.getX() + 1, current.getY());
                case Down -> new PointImpl(current.getX(), current.getY() + 1);
                case Left -> new PointImpl(current.getX() - 1, current.getY());
            };

            if (0 <= newElement.getX() && newElement.getX() < xSize
                    && 0 <= newElement.getY() && newElement.getY() < ySize
                    && !hamiltonPath.contains(newElement)) {
                hamiltonPath.add(newElement);
                System.out.println("Added: " + newElement);
                if (hamiltonStep(newElement)) {
                    return true;
                }
                hamiltonPath.remove(newElement);
                System.out.println("Removed: " + newElement);
            }
        }
        return false;
    }

    enum DirectionsForStep {
        Up,
        Right,
        Down,
        Left
    }
}
