package de.fxworld.thetravelingsalesman;
import java.util.List;

import de.fxworld.thetravelingsalesman.impl.DoublePath;

public interface IProblem<L> {

    List<L> getLocations();

    IPath getBestPath();

    void setBestPath(IPath bestPath);

    void resetBestPath();

    void calculateLengths(List<IPath> paths);

    int getLocationsCount();

	IPath createPath(int[] result);

	default IPath createPath() {
		return createPath(new int[0]);
	}
}
