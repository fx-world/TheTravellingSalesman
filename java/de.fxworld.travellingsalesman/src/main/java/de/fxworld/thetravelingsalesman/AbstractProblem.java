package de.fxworld.thetravelingsalesman;
import java.util.Collections;
import java.util.List;

public abstract class AbstractProblem<L> implements IProblem<L> {

    private List<L> locations;
    private Path bestPath;
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
    public Path getBestPath() {
        return bestPath;
    }

    @Override
    public synchronized void setBestPath(Path bestPath) {
        if (this.bestPath == null || (bestPath != null && bestPath.getLength() < this.bestPath.getLength())) {
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
