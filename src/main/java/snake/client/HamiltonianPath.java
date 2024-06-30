package snake.client;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Direction;
import java.util.ArrayList;
import java.util.List;

public class HamiltonianPath {
    private final int xSize;
    private final int ySize;
    private final List<Point> hamiltonPath;

    public HamiltonianPath(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.hamiltonPath = new ArrayList<>();
    }

    public void createHamiltonPath() {
        this.hamiltonPath.clear();
        this.hamiltonPath.add(new PointImpl(0, 0));
        hamiltonStep(this.hamiltonPath.get(this.hamiltonPath.size() - 1));
    }

    public List<Point> getPath() {
        return this.hamiltonPath;
    }

    private boolean hamiltonStep(Point current) {
        if (hamiltonPath.size() == xSize * ySize) {
            Point first = hamiltonPath.get(0);
            return (first.getX() == current.getX() && first.getY() == current.getY() - 1)
                    || (first.getX() == current.getX() && first.getY() == current.getY() + 1)
                    || (first.getX() - 1 == current.getX() && first.getY() == current.getY())
                    || (first.getX() + 1 == current.getX() && first.getY() == current.getY());
        }

        for (Direction direction : Direction.onlyDirections()) {
            Point newElement = switch (direction) {
                case UP -> new PointImpl(current.getX(), current.getY() - 1);
                case LEFT -> new PointImpl(current.getX() - 1, current.getY());
                case DOWN -> new PointImpl(current.getX(), current.getY() + 1);
                case RIGHT -> new PointImpl(current.getX() + 1, current.getY());
                default -> null;
            };

            if (0 <= newElement.getX() && newElement.getX() < xSize
                    && 0 <= newElement.getY() && newElement.getY() < ySize
                    && !hamiltonPath.contains(newElement)) {
                hamiltonPath.add(newElement);
                if (hamiltonStep(newElement)) {
                    return true;
                }
                hamiltonPath.remove(newElement);
            }
        }
        return false;
    }
}
