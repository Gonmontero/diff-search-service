package com.diff.service;

import com.diff.entities.Diff;
import com.diff.entities.DiffResult;
import com.diff.entities.enums.Side;

public interface DiffSearchService {
    Diff saveDiff(String id, Side side, byte[] data);

    Diff getDiff(String id);

    DiffResult processDiff(Diff diff);
}
