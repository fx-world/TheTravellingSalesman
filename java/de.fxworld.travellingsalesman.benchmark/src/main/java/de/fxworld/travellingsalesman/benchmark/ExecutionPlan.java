package de.fxworld.travellingsalesman.benchmark;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class ExecutionPlan {

    @Param({ "5", "10", "15", "18" })
    public int iterations;
}