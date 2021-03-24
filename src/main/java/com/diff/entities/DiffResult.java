package com.diff.entities;

import com.diff.entities.enums.Result;

import java.util.List;

public class DiffResult {

    private Diff diff;
    private Result result;
    private List<Difference> differences;

    public DiffResult(Diff diff, Result result) {
        this.diff = diff;
        this.result = result;
    }

    public DiffResult(Diff diff, Result result, List<Difference> differences) {
        this.diff = diff;
        this.result = result;
        this.differences = differences;
    }

    public Diff getDiff() {
        return diff;
    }

    public void setDiff(Diff diff) {
        this.diff = diff;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<Difference> getDifferences() {
        return differences;
    }

    public void setDifferences(List<Difference> differences) {
        this.differences = differences;
    }
}
