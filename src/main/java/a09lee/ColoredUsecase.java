package a09lee;

import a09lee.colored.Ansi;
import a09lee.colored.Attribute;
import a09lee.colored.Colored;

public class ColoredUsecase {

    public static void main(String[] args) {
        Point point = new Point(3, 5);
        String raw = point.toString();
        Attribute a = new Attribute(Ansi.ColorFont.RED);
        String colored = Colored.build(raw, a);

        System.out.println(colored);
        System.out.println(new Point(10,25));
        Point raw1 = new Point(12, 20);
        String colored1 = Colored.build(raw1, Ansi.ColorFont.BLUE);

        System.out.println(colored1);
    }

}
