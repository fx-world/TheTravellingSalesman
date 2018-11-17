package de.fxworld.thetravelingsalesman;
import java.util.List;

public interface IProblem<L> {

    List<L> getLocations();

    IPath<L> getBestPath();

    void setBestPath(IPath<L> bestPath);

    void resetBestPath();

    void calculateLengths(List<IPath<L>> paths);

    int getLocationsCount();

	IPath<L> createPath(int[] result);

	IPath<L> createPath();

	void setFixedFirstLocation(int fixedFirstLocation);
}
