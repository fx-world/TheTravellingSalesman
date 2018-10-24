import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Path implements Comparable<Path> {
    private IProblem problem;
    private List<Location> locations;
    private double length = Double.NaN;

    public Path(IProblem problem, List<Location> locations) {
        this.problem = problem;
        this.locations = Collections.unmodifiableList(new ArrayList<Location>(locations));
    }

    public Path(IProblem problem) {
        this.problem = problem;
        this.locations = Collections.emptyList();
        this.length = 0;
    }

    public Path(IProblem problem, Location start) {
        this.problem = problem;
        this.locations = Collections.singletonList(start);
    }

    public Path(IProblem problem, List<Location> locations, Location nextLocation) {
        ArrayList<Location> temp = new ArrayList<>(locations);
        temp.add(nextLocation);

        this.problem = problem;
        this.locations = Collections.unmodifiableList(temp);
    }

    public double getLength() {
        if (Double.isNaN(length)) {
            length = problem.calculateLength(locations);
        }

        return length;
    }

    protected void setLength(double length) {
        this.length = length;
    }

    public Path to(Location nextLocation) {
        return new Path(problem, locations, nextLocation);
    }

    public IProblem getProblem() {
        return problem;
    }

    public List<Location> getLocations() {
        return locations;
    }

    @Override
    public String toString() {
        return "Path [locations=" + locations + ", length=" + length + "]";
    }

    public Location getLast() {
        if (!locations.isEmpty()) {
            return locations.get(locations.size() - 1);
        } else {
            return null;
        }
    }

    public int getLocationsCount() {
        return locations.size();
    }

    @Override
    public int compareTo(Path o) {
        return Double.compare(getLength(), o.getLength());
    }
}