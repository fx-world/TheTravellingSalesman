---
# The Travelling Salesman

---

### Problem

Given a list of locations, what is the shortest route that contains every location.

[Wikipedia](https://en.wikipedia.org/wiki/Travelling_salesman_problem)

---

### Brute Force

```java
for (int i = 0; i < factorial(locations.size()); i++) { 
	Path path = permutation(factorial(locations.size()), i);
	
	if (path.isBetter(bestPath)) {
    	bestPath = path;
    }
}
```
@[1]
@[2]
@[4-6]
+++

### Brute Force - limit?

* time
* ???

+++

### Brute Force - limit?

* time
* data types

factorial(13) < Integer.MaxValue
6.227.020.800 < 2.147.483.647

+++

### Branch & Bound

```
Path solve(Path pathUntilNow, List<Location> leftLocations) {
	for (Location location in leftLocations) {
	    Path newPath = pathUntilNow.to(location)
	    List<Location> locations = leftLocations.remove(newPath)
	
	    if (newPath.isBetter(bestPath)) {
	        Path path = solve(newPath, locations)
	
	        if (path.isBetter(bestPath)) {
	            bestPath = path;
	        }
	    }
	}
}
```
@[1]
@[2]
@[3-4]
@[6]
@[7]

+++

### Others

* Evolution
* Ant
* Nearest Neighbor
* ...

---
### Run time

<canvas data-chart="bar">
<!-- 
{
 "data": {
  "labels": ["NearestNeighborSolver","BranchBoundSolver","ParallelBranchBoundSolver","EvolutionSolver"],
  "datasets": [
   {
    "data":[1,435968,43902,22355],
    "label":"","backgroundColor":"rgba(20,220,220,.8)"
   }
  ]
 }, 
 "options": { "responsive": "true" }
}
-->
</canvas>

+++

### 2 dimensional double array

```
public class Problem { 
	private double[][] distances;
}
```
+++

### Run time comparison

<canvas data-chart="bar">
<!-- 
{
 "data": {
  "labels": ["NearestNeighborSolver","BranchBoundSolver","ParallelBranchBoundSolver","EvolutionSolver"],
  "datasets": [
   {
    "data":[1,435968,43902,22355],
    "label":"double[][]","backgroundColor":"rgba(20,220,220,.8)"
   },
   {
    "data":[1,419558,39975,22188],
    "label":"double[]","backgroundColor":"rgba(20,20,220,.8)"
   }
  ]
 }, 
 "options": { "responsive": "true" }
}
-->
</canvas>

+++

### Run time comparison (double vs integer)

<canvas data-chart="bar">
<!-- 
{
 "data": {
  "labels": ["NearestNeighborSolver","BranchBoundSolver","ParallelBranchBoundSolver","EvolutionSolver"],
  "datasets": [
   {
    "data":[1,435968,43902,22355],
    "label":"double[][]","backgroundColor":"rgba(20,220,220,.8)"
   },
   {
    "data":[1,419558,39975,22188],
    "label":"double[]","backgroundColor":"rgba(20,20,220,.8)"
   },
   {
    "data":[1,401608,42075,22019],
    "label":"int[][]","backgroundColor":"rgba(220,20,220,.8)"
   },
   {
    "data":[1,393023,42513,22330],
    "label":"int[]","backgroundColor":"rgba(20,220,20,.8)"
   }
  ]
 }, 
 "options": { "responsive": "true" }
}
-->
</canvas>

---
### result comparison

<canvas data-chart="bar">
<!-- 
{
 "data": {
  "labels": ["NearestNeighborSolver","BranchBoundSolver","ParallelBranchBoundSolver","EvolutionSolver"],
  "datasets": [
   {
    "data":[2279670,1532916,1532916,1532916],
    "label":"length","backgroundColor":"rgba(20,220,220,.8)"
   }
  ]
 }, 
 "options": { "responsive": "true" }
}
-->
</canvas>
---

### Conclusion

* choise of algorithm
* choise of implementation 