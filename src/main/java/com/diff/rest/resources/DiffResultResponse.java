package com.diff.rest.resources;

import com.diff.entities.Diff;
import com.diff.entities.Difference;
import com.diff.entities.enums.Result;
import java.util.List;

public class DiffResultResponse {

    private Diff diff;
    private Result result;
    private List<Difference> differences;

    public DiffResultResponse() {

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

