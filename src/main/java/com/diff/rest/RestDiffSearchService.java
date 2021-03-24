package com.diff.rest;

import com.diff.entities.Diff;
import com.diff.entities.DiffResult;
import com.diff.entities.enums.Side;
import com.diff.exception.ApplicationException;
import com.diff.exception.errors.ErrorCode;
import com.diff.rest.exception.ApplicationExceptionHandler;
import com.diff.rest.resources.DiffResponse;
import com.diff.rest.resources.DiffResultResponse;
import com.diff.service.DiffSearchService;
import com.github.dozermapper.core.Mapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/diff")
public class RestDiffSearchService extends ApplicationExceptionHandler {

    @Autowired
    private DiffSearchService diffSearchService;

    @Autowired
    Mapper mapper;

    @ApiOperation(value = "Submit a side of a Diff for later processing")
    @PostMapping("{id}/{side}")
    public DiffResponse saveDiff(@PathVariable("id") String id, @PathVariable("side") String sideRequest, @RequestBody byte[] data) {

        Side side = Side.getFromSide(sideRequest);

        if (side == null) {
            // BadRequest
            throw new ApplicationException(ErrorCode.FIELD_VALIDATION_ERROR);
        }

        Diff diffSubmitted = diffSearchService.saveDiff(id, side, data);

        return mapper.map(diffSubmitted, DiffResponse.class);
    }

    @ApiOperation(value = "Start processing a Diff")
    @PostMapping("{id}")
    public DiffResultResponse processDiff(@PathVariable("id") String id) {
        Diff savedDiff = diffSearchService.getDiff(id);

        if (savedDiff == null) {
            throw new ApplicationException(ErrorCode.CONTENT_NOT_FOUND);
        }

        DiffResult result = diffSearchService.processDiff(savedDiff);

        return mapper.map(result, DiffResultResponse.class);
    }
}
