package de.fxworld.thetravelingsalesman.impl;
import java.util.Collections;
import java.util.List;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.IProblem;

public abstract class AbstractProblem<L> implements IProblem<L> {

    private List<L> locations;
    private volatile IPath bestPath;
    private int locationsCount;

    public AbstractProblem() {}

    public AbstractProblem(List<L> locations) {
        setLocations(locations);
    }

    protected void setLocations(List<L> locations) {
        if (this.locations == null) {
            this.locations = Collections.unmodifiableList(locations);
            this.locationsCount = locations.size();
        } else {
            throw new RuntimeException("Locations already set");
        }
    }

    @Override
    public List<L> getLocations() {
        return locations;
    }

    @Override
    public IPath getBestPath() {
        return bestPath;
    }

    @Override
    public synchronized void setBestPath(IPath bestPath) {
        if (bestPath != null && bestPath.isBetter(this.bestPath)) {
            this.bestPath = bestPath;
        }
    }

    @Override
    public void resetBestPath() {
        this.bestPath = null;
    }

    @Override
    public int getLocationsCount() {
        return locationsCount;
    }
}
