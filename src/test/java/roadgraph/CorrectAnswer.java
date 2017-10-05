package roadgraph;

import geography.GeographicPoint;
import util.PathsToTheData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class CorrectAnswer {
    int vertices;
    int edges;
    List<GeographicPoint> path;

    CorrectAnswer(final String file, final boolean hasEdges) {
        try {
            Scanner s = new Scanner(new File(file));
            //@see https://www.coursera.org/learn/advanced-data-structures/discussions/AzSMLrioEeWjxw7W9fJX5Q
            s.useLocale(Locale.ENGLISH);

            if (hasEdges) {
                vertices = s.nextInt();
                edges = s.nextInt();
            }
            path = null;
            if (s.hasNextDouble()) {

                path = new ArrayList<>();
            }
            while (s.hasNextDouble()) {
                double x = s.nextDouble();
                double y = s.nextDouble();
                path.add(new GeographicPoint(x, y));
            }
        } catch (Exception e) {
            System.err.println("Error reading correct answer!");
            e.printStackTrace();
        }
    }

    //test it
    public static void main(String[] args) {
        CorrectAnswer corr = new CorrectAnswer(PathsToTheData.CORRECT_ANSWER, false);
        System.out.println("corr\t" + corr.path.size());
    }
}
