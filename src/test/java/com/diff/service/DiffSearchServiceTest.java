package com.diff.service;

import com.diff.ApplicationTest;
import com.diff.entities.Diff;
import com.diff.entities.DiffResult;
import com.diff.entities.enums.Result;
import com.diff.entities.enums.Side;
import com.diff.exception.ApplicationException;
import com.diff.exception.errors.ErrorCode;
import com.diff.util.Base64Encoder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class DiffSearchServiceTest extends ApplicationTest {

    @Autowired
    private DiffSearchService diffSearchService;

    private Diff setupDiff(String id, byte[] left, byte[] right) {
       Diff diff = new Diff(id);
       diff.setLeft(left);
       diff.setRight(right);

       return diff;
    }

    @Test
    public void processEqualDataTest() {
        String stringData = "{\"test\" : true}";
        final byte[] data = stringData.getBytes();

        Diff diff = setupDiff("test", data, data);
        DiffResult result = diffSearchService.processDiff(diff);

        assertEquals(Result.EQUAL, result.getResult());
    }

    @Test
    public void processDifferentSizeDataTest() {
        String stringLeftData = "{\"test\" : true}";
        String stringRightData = "{\"test\" : false, \"large\": true}";
        final byte[] left = stringLeftData.getBytes();
        final byte[] right = stringRightData.getBytes();

        Diff diff = setupDiff("test", left, right);
        DiffResult result = diffSearchService.processDiff(diff);

        assertEquals(Result.DIFFERENT_SIZE, result.getResult());
    }

    @Test
    public void processDifferenceDataTest() {
        String stringLeftData = "{ \"test\": true }";
        String stringRightData = "{\"test\" : false}";
        final byte[] left = stringLeftData.getBytes();
        final byte[] right = stringRightData.getBytes();

        Diff diff = setupDiff("test", left, right);
        DiffResult result = diffSearchService.processDiff(diff);

        assertEquals(Result.NOT_EQUAL, result.getResult());
    }

    @Test
    public void processMissingDataTest() {
        String stringLeftData = "{ \"test\": true }";
        final byte[] left = stringLeftData.getBytes();

        Diff diff = setupDiff("test", left, null);

        try {
            diffSearchService.processDiff(diff);
            assert false;
        } catch (ApplicationException e) {
            assertEquals(ErrorCode.MISSING_DIFF_DATA, e.getErrorCode());
        }
    }

    @Test
    public void saveDiffTest() {
        String stringData = "{ \"test\": true }";
        byte[] testData = Base64Encoder.encode(stringData);
        String id = "test";

        assertNull(diffSearchService.getDiff(id));

        Diff diff = diffSearchService.saveDiff(id, Side.LEFT, testData);

        assertEquals(id, diff.getId());
        assertArrayEquals(testData, diff.getLeft());
        assertNull(diff.getRight());

        diff = diffSearchService.saveDiff(id, Side.RIGHT, testData);

        assertEquals(id, diff.getId());
        assertArrayEquals(testData, diff.getLeft());
        assertArrayEquals(testData, diff.getRight());

        Diff savedDiff = diffSearchService.getDiff(id);

        assertEquals(diff.getId(), savedDiff.getId());
        assertArrayEquals(diff.getLeft(), savedDiff.getLeft());
        assertArrayEquals(diff.getRight(), savedDiff.getRight());
    }

    @Test
    public void saveInvalidDiffTest() {
        String stringData = "{ not json }";
        String id = "test";

        try {
            diffSearchService.saveDiff(id, Side.LEFT, stringData.getBytes());
            assert false;
        } catch (ApplicationException e) {
            assertEquals(ErrorCode.UNSUPPORTED_ENCODING, e.getErrorCode());
        }

        byte[] testData = Base64Encoder.encode(stringData);

        try {
            diffSearchService.saveDiff(id, Side.LEFT, testData);
            assert false;
        } catch (ApplicationException e) {
            assertEquals(ErrorCode.UNSUPPORTED_FORMAT, e.getErrorCode());
        }
    }
}
