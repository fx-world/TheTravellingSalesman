package de.fxworld.thetravelingsalesman;
import java.util.List;

public interface IProblem<L> {

    double getDistance(int from, int to);

    double calculateLength(int[] path);

    List<L> getLocations();

    Path getBestPath();

    void setBestPath(Path bestPath);

    void resetBestPath();

    void calculateLengths(List<Path> paths);

    int getLocationsCount();
}
