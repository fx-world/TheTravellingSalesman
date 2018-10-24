package de.fxworld.thetravelingsalesman;
public interface ISolver<T> {

    Path solve();
    IProblem<T> getProblem();
}
