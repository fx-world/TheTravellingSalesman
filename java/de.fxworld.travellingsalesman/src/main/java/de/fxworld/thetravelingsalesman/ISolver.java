package de.fxworld.thetravelingsalesman;

public interface ISolver<T> {

    IPath solve();
    IProblem<T> getProblem();
}
