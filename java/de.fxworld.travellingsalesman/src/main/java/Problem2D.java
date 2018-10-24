import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Problem2D extends AbstractProblem implements IProblem {

    private double[][] distances;

    public Problem2D(List<Location> locations, double[][] distances) {
        super(locations);
        this.distances = distances;
    }

    public Problem2D(String path) throws IOException {
        super();
        this.distances = readGraph(path);

        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < distances.length; i++) {
            locations.add(new Location(i, "L" + i));
        }
        setLocations(locations);
    }

    /* (non-Javadoc)
     * @see IProblem#getDistance(Location, Location)
     */
    @Override
    public double getDistance(Location from, Location to) {
        //int fromIndex = locations.indexOf(from);
        //int toIndex = locations.indexOf(to);
        //return distances[fromIndex][toIndex];
        return distances[from.index][to.index];
    }

    /* (non-Javadoc)
     * @see IProblem#calculateLength(java.util.List)
     */
    @Override
    public double calculateLength(List<Location> path) {
        double result = 0;

        for (int i = 1; i < path.size(); i++) {
            result += getDistance(path.get(i - 1), path.get(i));
        }

        return result;
    }

    @Override
    public void calculateLengths(List<Path> paths) {

        for (Path path : paths) {
            double result = 0;
            List<Location> locations = path.getLocations();
            for (int i = 1; i < locations.size(); i++) {
                Location from = locations.get(i - 1);
                Location to = locations.get(i);
                result += distances[from.index][to.index];
            }
            path.setLength(result);
        }
    }

    // Read in graph from a file.
    // Allocates all memory.
    // Adds 1 to edge lengths to ensure no zero length edges.
    public double[][] readGraph(String path) throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader buf = new BufferedReader(fr);
        String line;
        int i = 0;

        double[][] graph = null;

        while ((line = buf.readLine()) != null) {
            String splitA[] = line.split(" ");
            LinkedList<String> split = new LinkedList<>();
            for (String s : splitA) {
                if (!s.isEmpty()) {
                    split.add(s);
                }
            }

            if (graph == null) {
                graph = new double[split.size()][split.size()];
            }
            int j = 0;

            for (String s : split) {
                if (!s.isEmpty()) {
                    graph[i][j++] = Double.parseDouble(s) + 1;
                }
            }

            i++;
        }

        return graph;
    }
}