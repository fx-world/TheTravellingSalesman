package de.fxworld.thetravelingsalesman;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ParallelBranchBoundSolver<T> implements ISolver<T> {

    private IProblem<T> problem;

    private ThreadPoolExecutor pool;

    private BlockingQueue<Runnable> workQueue;

    private Path bestPath;

    private int threadCount;

    public ParallelBranchBoundSolver(IProblem<T> problem) {
        this(problem, Runtime.getRuntime().availableProcessors() + 1);
    }

    public ParallelBranchBoundSolver(IProblem<T> problem, int threadCount) {
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

    protected void calculateShortestPath(IProblem<T> problem) {
    	List<Integer> leftToVisit = new ArrayList<Integer>();
    	
    	for (int i = 0; i < problem.getLocationsCount(); i++) {
    		leftToVisit.add(i);
    	}
    	
        calculateShortestPath(problem, new Path(problem), leftToVisit);
    }

    protected void calculateShortestPath(IProblem problem, Path startPath, List<Integer> leftToVisit) {
        if (leftToVisit.size() == 1) {
            Path path = startPath.to(leftToVisit.get(0));
            setBestPath(path);

        } else {
            List<Path> nextPaths = new ArrayList<>();
            Path globalBestPath = problem.getBestPath();

            for (Integer nextLocation : leftToVisit) {
                Path nextPath = startPath.to(nextLocation);

                if (globalBestPath == null || nextPath.getLength() < globalBestPath.getLength()) {
                    nextPaths.add(nextPath);
                }
            }

            problem.calculateLengths(nextPaths);
            Collections.sort(nextPaths);

            if (workQueue.size() < threadCount && problem.getLocationsCount() > startPath.getLocationsCount() * 2) {
                for (Path nextPath : nextPaths) {
                    List<Integer> newLeftToVisit = new ArrayList<>(leftToVisit);
                    Integer nextLocation = nextPath.getLast();
                    newLeftToVisit.remove(nextLocation);

                    pool.submit(() -> calculateShortestPath(problem, nextPath, newLeftToVisit));
                }
            } else {
                for (Path nextPath : nextPaths) {
                    List<Integer> newLeftToVisit = new ArrayList<>(leftToVisit);
                    Integer nextLocation = nextPath.getLast();
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
