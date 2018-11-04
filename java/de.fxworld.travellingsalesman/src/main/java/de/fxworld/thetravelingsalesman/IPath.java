package de.fxworld.thetravelingsalesman;

public interface IPath extends Comparable<IPath> {

	IPath to(int nextLocation);

	IProblem<?> getProblem();

	int[] getLocations();

	int getLast();

	int getLocationsCount();

	boolean contains(int location);

	boolean isBetter(IPath globalBestPath);

}