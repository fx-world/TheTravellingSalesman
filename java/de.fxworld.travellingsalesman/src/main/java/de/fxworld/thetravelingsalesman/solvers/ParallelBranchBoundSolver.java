package de.fxworld.thetravelingsalesman.solvers;
import java.util.*;
import java.util.concurrent.*;

import de.fxworld.thetravelingsalesman.*;

public class ParallelBranchBoundSolver<T> implements ISolver<T> {

    private IProblem<T> problem;

    private ThreadPoolExecutor pool;

    private BlockingQueue<Runnable> workQueue;

    private volatile IPath<T> bestPath;

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
    public IPath<T> solve() {
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
    public IProblem<T> getProblem() {
        return problem;
    }

    protected void calculateShortestPath(IProblem<T> problem) {
    	List<Integer> leftToVisit = new ArrayList<Integer>();
    	
    	for (int i = 0; i < problem.getLocationsCount(); i++) {
    		leftToVisit.add(i);
    	}
    	
        calculateShortestPath(problem, problem.createPath(), leftToVisit);
    }

    protected void calculateShortestPath(IProblem<T> problem, IPath<T> startPath, List<Integer> leftToVisit) {
        if (leftToVisit.size() == 1) {
        	IPath<T> path = startPath.to(leftToVisit.get(0).intValue());
            setBestPath(path);

        } else {
            List<IPath<T>> nextPaths = new ArrayList<>();
            IPath<T> globalBestPath = problem.getBestPath();

            for (Integer nextLocation : leftToVisit) {
            	IPath<T> nextPath = startPath.to(nextLocation.intValue());

                //if (globalBestPath == null || nextPath.getLength() < globalBestPath.getLength()) {
            	if (nextPath.isBetter(globalBestPath)) {
                    nextPaths.add(nextPath);
                }
            }

            problem.calculateLengths(nextPaths);
            Collections.sort(nextPaths);

            if (workQueue.size() < threadCount && problem.getLocationsCount() > startPath.getLocationsCount() * 2) {
                for (IPath<T> nextPath : nextPaths) {
                    List<Integer> newLeftToVisit = new ArrayList<>(leftToVisit);
                    Integer nextLocation = nextPath.getLast();
                    newLeftToVisit.remove(nextLocation);

                    pool.submit(() -> calculateShortestPath(problem, nextPath, newLeftToVisit));
                }
            } else {
                for (IPath<T> nextPath : nextPaths) {
                    List<Integer> newLeftToVisit = new ArrayList<>(leftToVisit);
                    Integer nextLocation = nextPath.getLast();
                    newLeftToVisit.remove(nextLocation);

                    calculateShortestPath(problem, nextPath, newLeftToVisit);
                }
            }
        }
    }

    protected synchronized void setBestPath(IPath<T> path) {
        if (path != null && path.isBetter(bestPath)) {
            bestPath = path;
            problem.setBestPath(path);
        }
    }

    @Override
    public String toString() {
        return "ParallelBranchBoundSolver [threadCount=" + threadCount + "]";
    }
}
