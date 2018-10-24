import java.util.Collections;
import java.util.List;

public abstract class AbstractProblem implements IProblem {

    private List<Location> locations;
    private Path bestPath;
    private int locationsCount;

    public AbstractProblem() {}

    public AbstractProblem(List<Location> locations) {
        setLocations(locations);
    }

    protected void setLocations(List<Location> locations) {
        if (this.locations == null) {
            this.locations = Collections.unmodifiableList(locations);
            this.locationsCount = locations.size();
        } else {
            throw new RuntimeException("Locations already set");
        }
    }

    @Override
    public List<Location> getLocations() {
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
