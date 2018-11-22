package de.fxworld.thetravelingsalesman;

public interface ISolver<T> {

    IPath<T> solve();
    IProblem<T> getProblem();
}
