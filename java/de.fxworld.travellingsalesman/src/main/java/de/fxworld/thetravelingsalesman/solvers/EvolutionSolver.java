package de.fxworld.thetravelingsalesman.solvers;

import java.util.*;

import de.fxworld.thetravelingsalesman.*;
import de.fxworld.thetravelingsalesman.impl.DoublePath;

public class EvolutionSolver<T> implements ISolver<T> {

	private IProblem<T> problem;
	private List<IPath> population = new ArrayList<>();
	
	private Random random = new Random();
	
	private int populationCount = 500;
	private int generationCount = 10000;
	private int eliteCount = 250;
	private int eliteDescendendCount = 25;
	private int mutatePercent = 15;
	private int maxNoImprovement = 1000;
	
	private int noImprovement = 0;
	
	public EvolutionSolver(IProblem<T> problem) {
		this.problem = problem;
	}

	@Override
	public IPath solve() {
		//Path result = createRandom();
		IPath result = createNearest();
		
		for (int i = 0; i < 10; i++) {
			population.add(result);
		}
				
		
		for (int i = 0; i < populationCount - 10; i++) {
			population.add(createRandom());
		}
		
		for (int generation = 0; generation < generationCount; generation++) {
			List<IPath> descendants = new ArrayList<>();			
			
			Collections.sort(population);
			
			for (int i = 0; i < eliteCount; i++) {
				IPath elite = population.get(i);
				
				for (int j = 0; j < eliteDescendendCount; j++) {
					IPath descendant = evolve(elite);
				
					if (random.nextInt(100) < mutatePercent) {
						descendant = mutate(descendant);
					}
					
					descendants.add(descendant);
				}
			}
			
			problem.calculateLengths(descendants);
			
			population.addAll(descendants);
			Collections.sort(population);
			
			while (population.size() > populationCount) {
				population.remove(population.size() - 1);
			}
			
			IPath best = population.get(0);
			if (best.isBetter(result)) {
				result = best;
				problem.setBestPath(best);
			} else {
				noImprovement++;
				
				if (noImprovement >= maxNoImprovement) {
					System.out.println("kill no imporvment");
					break;
				}
			}
		}

		return result;
	}

	private IPath evolve(IPath path) {
		int[] locations = path.getLocations();
		int[] result = Arrays.copyOf(locations, locations.length);
		int from = random.nextInt(locations.length - 1);		
		
		exchange(result, from, from + 1);
		
		return problem.createPath(result);
	}
	
	private IPath mutate(IPath path) {
		int[] locations = path.getLocations();
		int[] result = Arrays.copyOf(locations, locations.length);
		int from = random.nextInt(locations.length);		
		int to = random.nextInt(locations.length);
		
		exchange(result, from, to);
		
		return problem.createPath(result);
	}
	
	private IPath createRandom() {
		int[] result = new int[problem.getLocationsCount()];
		List<Integer> locations = new ArrayList<>();
		
		for (int i = 0; i < problem.getLocationsCount(); i++) {
			locations.add(i);
		}
		
		for (int i = 0; i < result.length; i++) {
			int l = random.nextInt(locations.size());
			result[i] = locations.remove(l);
		}
		
		return problem.createPath(result);
	}
	
	private IPath createNearest() {
		IPath result = problem.createPath();
    	
    	for (int j = 0; j < problem.getLocationsCount(); j++) {
	    	List<IPath> neighbors = new ArrayList<>();
	    	
	    	for (int i = 0; i < problem.getLocationsCount(); i++) {
	    		if (!result.contains(i)) {
	    			neighbors.add(result.to(i));    			
	    		}    		
	    	}
	    	
	    	problem.calculateLengths(neighbors);
	    	Collections.sort(neighbors);
	    	
	    	result = neighbors.get(0);
    	}
	    	
        return result;
	}

	private void exchange(int[] result, int from, int to) {
		int temp = result[from];
		result[from] = result[to];
		result[to] = temp;
	}

	@Override
	public IProblem<T> getProblem() {
		return problem;
	}

	@Override
	public String toString() {
		return "EvolutionSolver [populationCount=" + populationCount + ", generationCount=" + generationCount
				+ ", eliteCount=" + eliteCount + ", eliteDescendendCount=" + eliteDescendendCount + ", mutatePercent="
				+ mutatePercent + "]";
	}
}
