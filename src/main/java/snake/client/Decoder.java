package snake.client;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.*;

public class Decoder {
    static List<Point> PathChecker = new ArrayList<Point>();

    public static Direction getDirection(Point start , Point target) {
        int deltaX = start.getX() - target.getX();
        int deltaY = start.getY() - target.getY();
        String direction = deltaX + "," + deltaY;
        System.out.println("getDirection: " + direction);
        return switch (direction) {
            case "0,1" -> Direction.DOWN;
            case "-1,0" -> Direction.RIGHT;
            case "0,-1" -> Direction.UP;
            case "1,0" -> Direction.LEFT;
            default -> throw new IllegalArgumentException("Invalid points for direction");
        };
    }
    public static Direction getDirection(Optional<Iterable<Point>> points) {
        if (points.isPresent()) {
            Iterator<Point> iterator = points.get().iterator();
            if (iterator.hasNext()) {
                Point firstPoint = iterator.next();
                if (iterator.hasNext()) {
                    Point secondPoint = iterator.next();
                    return getDirection(firstPoint, secondPoint);
                } else {
                    throw new IllegalArgumentException("Not enough points for direction");
                }
            } else {
                throw new IllegalArgumentException("Not enough points for direction");
            }
        } else {
            throw new IllegalArgumentException("Invalid points for direction");
        }
    }
    public static Direction getDirection() {
        Direction result = getDirection (PathChecker.getFirst() , PathChecker.get(1));
        PathChecker.removeFirst();
        return result;
    }

}
