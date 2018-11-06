package de.fxworld.travellingsalesman.gpu;

import java.util.List;

import com.aparapi.Kernel;
import com.aparapi.Range;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.impl.IntegerArrayProblem;
import de.fxworld.thetravelingsalesman.impl.IntegerPath;

public class IntegerArrayGpuProblem<T> extends IntegerArrayProblem<T> {

	public IntegerArrayGpuProblem(List<T> locations, int[][] distances) {
		super(locations, distances);
	}

	@Override
	public void calculateLengths(final List<IPath> paths) {

		int[][] allLocations = new int[paths.size()][];
		int[] lengths = new int[paths.size()];
		int[] results = new int[paths.size()];
		int locationCount = getLocationsCount();
		int[] distances = this.distances;
		
		for (int i = 0; i < paths.size(); i++) {
			IPath path = paths.get(i);
			allLocations[i] = path.getLocations();
			lengths[i] = allLocations[i].length;
		}
		
		Kernel kernel = new Kernel() {
		    @Override
		    public void run() {
		        int id = getGlobalId();
	            
		        int length = lengths[id];
		        int result = 0;
	            int[] locations = allLocations[id];
	            for (int i = 1; i < length; i++) {
	                int from = locations[i - 1];
	                int to = locations[i];
	                result += distances[from * locationCount + to];
	            }
	            results[id] = result;	            
		    }
		};
		
		Range range = Range.create(paths.size());
		kernel.execute(range);
		kernel.dispose();
		//System.out.println("Execution mode = "+kernel.getTargetDevice().getShortDescription());
		
		for (int i = 0; i < paths.size(); i++) {
			IPath path = paths.get(i);
			((IntegerPath) path).setLength(results[i]);
		}
	}
}
