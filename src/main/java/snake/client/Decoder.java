package snake.client;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.*;

public class Decoder {
    static List<Point> PathChecker = new ArrayList<Point>();

    public static Direction getDirection() {
        int deltaX = PathChecker.getFirst().getX() - PathChecker.get(1).getX();
        int deltaY = PathChecker.getFirst().getY() - PathChecker.get(1).getY();
        String direction = deltaX + "," + deltaY;
        PathChecker.removeFirst(); // add handel if size = 1 need new
        System.out.println("getDirection: " + direction);
        return switch (direction) {
            case "0,1" -> Direction.DOWN;
            case "-1,0" -> Direction.RIGHT;
            case "0,-1" -> Direction.UP;
            case "1,0" -> Direction.LEFT;
            default -> throw new IllegalArgumentException("Invalid points for direction");
        };
    }


}
