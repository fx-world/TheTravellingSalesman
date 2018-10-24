import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ParallelBranchBoundSolver implements ISolver {

    private IProblem problem;

    private ThreadPoolExecutor pool;

    private BlockingQueue<Runnable> workQueue;

    private Path bestPath;

    private int threadCount;

    public ParallelBranchBoundSolver(IProblem problem) {
        this(problem, Runtime.getRuntime().availableProcessors() + 1);
    }

    public ParallelBranchBoundSolver(IProblem problem, int threadCount) {
        this.problem = problem;
        this.threadCount = threadCount;
        this.workQueue = new LinkedBlockingQueue<>();
        this.pool = new ThreadPoolExecutor(threadCount, threadCount, 5000, TimeUnit.MILLISECONDS, workQueue);
    }

    @Override
    public Path solve() {
        pool.submit(() -> calculateShortestPath(problem));

        while (pool.getTaskCount() != pool.getCompletedTaskCount()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {}
        }

        pool.shutdown();
        try {
            pool.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {}

        return bestPath;
    }

    @Override
    public IProblem getProblem() {
        return problem;
    }

    protected void calculateShortestPath(IProblem problem) {
        calculateShortestPath(problem, new Path(problem), problem.getLocations());
    }

    protected void calculateShortestPath(IProblem problem, Path startPath, List<Location> leftToVisit) {
        if (leftToVisit.size() == 1) {
            Path path = startPath.to(leftToVisit.get(0));
            setBestPath(path);

        } else {
            List<Path> nextPaths = new ArrayList<>();
            Path globalBestPath = problem.getBestPath();

            for (Location nextLocation : leftToVisit) {
                Path nextPath = startPath.to(nextLocation);

                if (globalBestPath == null || nextPath.getLength() < globalBestPath.getLength()) {
                    nextPaths.add(nextPath);
                }
            }

            problem.calculateLengths(nextPaths);
            Collections.sort(nextPaths);

            if (workQueue.size() < threadCount && problem.getLocationsCount() > startPath.getLocationsCount() * 2) {
                for (Path nextPath : nextPaths) {
                    List<Location> newLeftToVisit = new ArrayList<>(leftToVisit);
                    Location nextLocation = nextPath.getLast();
                    newLeftToVisit.remove(nextLocation);

                    pool.submit(() -> calculateShortestPath(problem, nextPath, newLeftToVisit));
                }
            } else {
                for (Path nextPath : nextPaths) {
                    List<Location> newLeftToVisit = new ArrayList<>(leftToVisit);
                    Location nextLocation = nextPath.getLast();
                    newLeftToVisit.remove(nextLocation);

                    calculateShortestPath(problem, nextPath, newLeftToVisit);
                }
            }
        }
    }

    protected synchronized void setBestPath(Path path) {
        if (path != null && (bestPath == null || path.getLength() < bestPath.getLength())) {
            bestPath = path;
            problem.setBestPath(path);
        }
    }

    @Override
    public String toString() {
        return "ParallelBranchBoundSolver [threadCount=" + threadCount + "]";
    }
}
