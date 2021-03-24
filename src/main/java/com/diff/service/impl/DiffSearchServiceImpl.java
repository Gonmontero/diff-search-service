package com.diff.service.impl;

import com.diff.dao.DiffDAO;
import com.diff.entities.Diff;
import com.diff.entities.DiffResult;
import com.diff.entities.Difference;
import com.diff.entities.enums.Result;
import com.diff.entities.enums.Side;
import com.diff.exception.ApplicationException;
import com.diff.exception.errors.ErrorCode;
import com.diff.service.DecoderService;
import com.diff.service.DiffSearchService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiffSearchServiceImpl implements DiffSearchService {

    @Autowired
    private DecoderService decoderService;

    @Autowired
    private DiffDAO diffDAO;

    @Override
    public Diff saveDiff(String id, Side side, byte[] data) {

        //Validate data can be processed.
        validateData(data);
        // Find an existent one or create a new one.
        Diff diff = diffDAO.findById(id).orElse(new Diff(id));

        //Assign the data submitted to the requested side.
        if (Side.LEFT == side) {
            diff.setLeft(data);
        } else {
            diff.setRight(data);
        }

        // Save and returned Diff Object
        return diffDAO.save(diff);
    }

    @Override
    public Diff getDiff(String id) {
        //fetch Diff from DB, if it is not found, then let the controller return content not found.
        return diffDAO.findById(id).orElse(null);
    }

    @Override
    public DiffResult processDiff(Diff diff) {
        byte[] left = diff.getLeft();
        byte[] right = diff.getRight();

        //Diff must have both sides assigned, otherwise process cannot be completed.
        if (left == null || right == null) {
            throw new ApplicationException(ErrorCode.MISSING_DIFF_DATA);
        }

        // If sizes are different, then we have our first result.
        if (left.length != right.length) {
            return new DiffResult(diff, Result.DIFFERENT_SIZE);
        }

        // As the content length are equal, we must check for any differences within it's content.
        List<Difference> differences = searchDifferences(left, right);

        // If the process finds any differences, we return the not equal content result, with their difference's offsets and lengths.
        if (!differences.isEmpty()) {
            return new DiffResult(diff, Result.NOT_EQUAL, differences);
        }

        //By reaching this line, process concluded and did not find any differences, hence we return the equal content.
        return new DiffResult(diff, Result.EQUAL);
    }

    /***
     * This will validate that the data submitted can be processed.
     * @param data
     */
    private void validateData(byte[] data) {
        byte[] decodedData;
        // Data has to be validated for submitting.
        try {
            decodedData = decoderService.decode(data);
            new JSONObject(new String(decodedData));
        } catch (IllegalArgumentException e) {
            // Unsupported encodes and wrong formats are rejected
            throw new ApplicationException(ErrorCode.UNSUPPORTED_ENCODING);
        } catch (JSONException e) {
            throw new ApplicationException(ErrorCode.UNSUPPORTED_FORMAT);
        }
    }

    /***
     * Iterates over an array of bytes, comparing and tracking differences.
     *
     * @param left side submitted for comparison
     * @param right side subitted for comparison
     * @return A List of differences with their offsets and length.
     */
    private List<Difference> searchDifferences(byte[] left, byte[] right) {
        List<Difference> differences = new ArrayList<>();
        int offset = 0;
        int length = 0;

        // Iterate the array and compare one to other
        for (int i=0; i < left.length; i++) {
            if (left[i] == right[i]) {
                //if they are equal we skip to the next one, unless we were counting the length from a previous difference.
                if (length != 0) {
                    //Register the offset of the difference and  it's length before resetting tracker.
                    differences.add(new Difference(offset, length));
                    length = 0;
                }
                continue;
            }

            //If it's different we start counting the length of the difference unless we are already counting.
            if (length != 0) {
                //Set the offset where the difference was found.
                offset = i;
            }
            //Start tracking length
            length++;
        }

        //If the length tracker was still counting after process finished the comparison, then there is one more registry to add.
        if (length != 0) {
            differences.add(new Difference(offset, length));
        }

        return differences;
    }
}
