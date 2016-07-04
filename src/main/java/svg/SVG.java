package svg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SVG {
    private static final Random RANDOM = new Random();

    public static String svg(int n) {
        return new SVG()
                .generate(n);
    }

    public String generate(int n) {
        //Init random coords
        List<Coord> coords = new ArrayList<Coord>();
        for (int i1 = 0; i1 < n; i1++) {
            coords.add(new Coord(RANDOM.nextInt(500), RANDOM.nextInt(500), RANDOM.nextInt(10)));
        }

        //Sort coords by x * y
        coords.sort(new Comparator<Coord>() {
            public int compare(Coord x, Coord y) {
                return x.x * x.y - y.x * y.y;
            }
        });

        //Generate shapes on each coord
        List<String> shapes = new ArrayList<String>();
        for (int i = 0; i < coords.size(); i++) {
            String result;
            switch (i % 3) {
                case 0:
                    result = String.format("<circle cx='%d' cy='%d' r='%d' stroke='black' stroke-width='1' fill='red' />",
                            coords.get(i).x, coords.get(i).y, coords.get(i).r);
                    break;
                case 1:
                    result = String.format("<rect x='%d' y='%d' width='%d' height='%d' style='fill:rgb(255,0,0);stroke-width:1;stroke:rgb(0,0,0)' />",
                            coords.get(i).x - coords.get(i).r, coords.get(i).y - coords.get(i).r, coords.get(i).r * 2, coords.get(i).r * 2);
                    break;
                default:
                    result = String.format("<ellipse cx='%d' cy='%d' rx='%d' ry='%d' stroke='black' stroke-width='1' fill='red' />",
                            coords.get(i).x, coords.get(i).y, coords.get(i).r, coords.get(i).r * 2);
            }
            shapes.add(result);
        }

        // interpose circles between 2 coords
        for (int i = 0; i < coords.size() - 1; i++) {
            shapes.add(String.format("<circle cx='%d' cy='%d' r='%d' stroke='black' stroke-width='1' fill='blue' />",
                    (coords.get(i).x + coords.get(i+1).x) / 2,
                    (coords.get(i).y + coords.get(i+1).y) / 2,
                    (coords.get(i).r + coords.get(i+1).r) / 2));
        }

        // generate svg
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>\n" +
                "<h1>SVG</h1>\n" +
                "<svg width='500' height='500'>\n");
        for (String line : shapes) {
            sb.append(line);
            sb.append("\n");
        }
        sb.append("</svg></body></html>");
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("svg.html");
        Files.deleteIfExists(path);

        Files.write(path, svg(10).getBytes());
        //System.out.println(svg(10));
    }

}
