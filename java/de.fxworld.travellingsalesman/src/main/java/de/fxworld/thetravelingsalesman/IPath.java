package de.fxworld.thetravelingsalesman;

public interface IPath<T> extends Comparable<IPath<T>> {

	IPath<T> to(int nextLocation);

	IProblem<T> getProblem();

	int[] getLocations();

	int getLast();

	int getLocationsCount();

	boolean contains(int location);

	boolean isBetter(IPath<T> globalBestPath);

}