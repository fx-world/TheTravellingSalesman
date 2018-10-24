import java.util.List;

public class ProblemArray extends AbstractProblem implements IProblem {

    private int[] distances;

    public ProblemArray(List<Location> locations, int[][] distances) {
        super(locations);
        this.distances = convertDistances(distances, getLocationsCount());
    }

    protected int[] convertDistances(int[][] distances, int locationCount) {
        int[] result = new int[locationCount * locationCount];

        for (int i = 0; i < locationCount; i++) {
            for (int j = 0; j < locationCount; j++) {
                result[i * locationCount + j] = distances[i][j];
            }
        }

        return result;
    }

    /* (non-Javadoc)
     * @see IProblem#getDistance(Location, Location)
     */
    @Override
    public double getDistance(Location from, Location to) {
        return distances[from.index * getLocationsCount() + to.index];
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
            int result = 0;
            List<Location> locations = path.getLocations();
            for (int i = 1; i < locations.size(); i++) {
                Location from = locations.get(i - 1);
                Location to = locations.get(i);
                result += distances[from.index * getLocationsCount() + to.index];
            }
            path.setLength(result);
        }
    }
}