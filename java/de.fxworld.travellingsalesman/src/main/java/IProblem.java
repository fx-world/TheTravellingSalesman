import java.util.List;

public interface IProblem {

    double getDistance(Location from, Location to);

    double calculateLength(List<Location> path);

    List<Location> getLocations();

    Path getBestPath();

    void setBestPath(Path bestPath);

    void resetBestPath();

    void calculateLengths(List<Path> paths);

    int getLocationsCount();
}
