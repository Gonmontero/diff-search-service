package com.diff.rest;

import com.diff.ApplicationTest;
import com.diff.exception.errors.ErrorCode;
import com.diff.util.Base64Encoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class RestDiffSearchServiceTest extends ApplicationTest {


    @Test
    public void testSuccessfulLeftData() throws Exception {

        byte[] encodedRequest = Base64Encoder.encode("{ \"test\": true }");
        MockHttpServletRequestBuilder requestBuild = MockMvcRequestBuilders
                .post("/v1/diff/test/left")
                .content(encodedRequest);

        super.executeCall(requestBuild)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("test")))
                .andExpect(jsonPath("$.left", is("ZXlBaWRHVnpkQ0k2SUhSeWRXVWdmUT09")))
                .andExpect(jsonPath("$.right", isEmptyOrNullString()))
                .andDo(print());
    }

    @Test
    public void testSuccessfulRightData() throws Exception {

        byte[] encodedRequest = Base64Encoder.encode("{ \"test\": true }");
        MockHttpServletRequestBuilder requestBuild = MockMvcRequestBuilders
                .post("/v1/diff/test/right")
                .content(encodedRequest);

        super.executeCall(requestBuild)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("test")))
                .andExpect(jsonPath("$.right", is("ZXlBaWRHVnpkQ0k2SUhSeWRXVWdmUT09")))
                .andExpect(jsonPath("$.left", isEmptyOrNullString()))
                .andDo(print());
    }

    @Test
    public void testInvalidSide() throws Exception {

        byte[] encodedRequest = Base64Encoder.encode("{ \"test\": true }");
        MockHttpServletRequestBuilder requestBuild = MockMvcRequestBuilders
                .post("/v1/diff/test/center")
                .content(encodedRequest);

        super.executeCall(requestBuild)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ErrorCode.FIELD_VALIDATION_ERROR.getDescription()))
                .andDo(print());
    }

    @Test
    public void testInvalidEncode() throws Exception {

        MockHttpServletRequestBuilder requestBuild = MockMvcRequestBuilders
                .post("/v1/diff/test/left")
                .content("{notEncoded: true}");

        super.executeCall(requestBuild)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ErrorCode.UNSUPPORTED_ENCODING.getDescription()))
                .andDo(print());
    }

    @Test
    public void testInvalidformat() throws Exception {

        byte[] encodedRequest = Base64Encoder.encode("this is not a json");
        MockHttpServletRequestBuilder requestBuild = MockMvcRequestBuilders
                .post("/v1/diff/test/left")
                .content(encodedRequest);

        super.executeCall(requestBuild)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ErrorCode.UNSUPPORTED_FORMAT.getDescription()))
                .andDo(print());
    }

    @Test
    public void testSucessfulEqualDiffProcess() throws Exception {

        byte[] encodedRequest = Base64Encoder.encode("{ \"test\": true }");
        MockHttpServletRequestBuilder requestLeftBuild = MockMvcRequestBuilders
                .post("/v1/diff/test/right")
                .content(encodedRequest);

        super.executeCall(requestLeftBuild)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("test")))
                .andExpect(jsonPath("$.right", is("ZXlBaWRHVnpkQ0k2SUhSeWRXVWdmUT09")))
                .andExpect(jsonPath("$.left", isEmptyOrNullString()))
                .andDo(print());

        MockHttpServletRequestBuilder requestRightBuild = MockMvcRequestBuilders
                .post("/v1/diff/test/left")
                .content(encodedRequest);

        super.executeCall(requestRightBuild)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("test")))
                .andExpect(jsonPath("$.right", is("ZXlBaWRHVnpkQ0k2SUhSeWRXVWdmUT09")))
                .andExpect(jsonPath("$.left",  is("ZXlBaWRHVnpkQ0k2SUhSeWRXVWdmUT09")))
                .andDo(print());

        MockHttpServletRequestBuilder processDiffRequest = MockMvcRequestBuilders
                .post("/v1/diff/test");

        super.executeCall(processDiffRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result",  is("EQUAL")))
                .andExpect(jsonPath("$.differences", isEmptyOrNullString()))
                .andDo(print());
    }

    @Test
    public void testSucessfulDifferentSizeDiffProcess() throws Exception {

        byte[] encodedRequest = Base64Encoder.encode("{ \"test\": true }");
        byte[] encodedLargeRequest = Base64Encoder.encode("{ \"test\": false, \"large\": true }");
        MockHttpServletRequestBuilder requestLeftBuild = MockMvcRequestBuilders
                .post("/v1/diff/test/right")
                .content(encodedRequest);

        super.executeCall(requestLeftBuild)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("test")))
                .andExpect(jsonPath("$.right", is("ZXlBaWRHVnpkQ0k2SUhSeWRXVWdmUT09")))
                .andExpect(jsonPath("$.left", isEmptyOrNullString()))
                .andDo(print());

        MockHttpServletRequestBuilder requestRightBuild = MockMvcRequestBuilders
                .post("/v1/diff/test/left")
                .content(encodedLargeRequest);

        super.executeCall(requestRightBuild)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("test")))
                .andExpect(jsonPath("$.right", is("ZXlBaWRHVnpkQ0k2SUhSeWRXVWdmUT09")))
                .andExpect(jsonPath("$.left",  is("ZXlBaWRHVnpkQ0k2SUdaaGJITmxMQ0FpYkdGeVoyVWlPaUIwY25WbElIMD0=")))
                .andDo(print());

        MockHttpServletRequestBuilder processDiffRequest = MockMvcRequestBuilders
                .post("/v1/diff/test");

        super.executeCall(processDiffRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result",  is("DIFFERENT_SIZE")))
                .andExpect(jsonPath("$.differences", isEmptyOrNullString()))
                .andDo(print());
    }

    @Test
    public void testSucessfulDifferentContentDiffProcess() throws Exception {

        byte[] encodedRightRequest = Base64Encoder.encode("{ \"test\": true }");
        byte[] encodedLeftRequest = Base64Encoder.encode("{ \"test\": false}");
        MockHttpServletRequestBuilder requestLeftBuild = MockMvcRequestBuilders
                .post("/v1/diff/test/right")
                .content(encodedRightRequest);

        super.executeCall(requestLeftBuild)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("test")))
                .andExpect(jsonPath("$.right", is("ZXlBaWRHVnpkQ0k2SUhSeWRXVWdmUT09")))
                .andExpect(jsonPath("$.left", isEmptyOrNullString()))
                .andDo(print());

        MockHttpServletRequestBuilder requestRightBuild = MockMvcRequestBuilders
                .post("/v1/diff/test/left")
                .content(encodedLeftRequest);

        super.executeCall(requestRightBuild)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("test")))
                .andExpect(jsonPath("$.right", is("ZXlBaWRHVnpkQ0k2SUhSeWRXVWdmUT09")))
                .andExpect(jsonPath("$.left",  is("ZXlBaWRHVnpkQ0k2SUdaaGJITmxmUT09")))
                .andDo(print());

        MockHttpServletRequestBuilder processDiffRequest = MockMvcRequestBuilders
                .post("/v1/diff/test");

        super.executeCall(processDiffRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result",  is("NOT_EQUAL")))
                .andExpect(jsonPath("$.differences[0].offset", is(19)))
                .andExpect(jsonPath("$.differences[0].length", is(7)))
                .andDo(print());
    }


    @Test
    public void testMissingDataDiffProcess() throws Exception {

        byte[] encodedRequest = Base64Encoder.encode("{ \"test\": true }");
        MockHttpServletRequestBuilder requestLeftBuild = MockMvcRequestBuilders
                .post("/v1/diff/test/right")
                .content(encodedRequest);

        super.executeCall(requestLeftBuild)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("test")))
                .andExpect(jsonPath("$.right", is("ZXlBaWRHVnpkQ0k2SUhSeWRXVWdmUT09")))
                .andExpect(jsonPath("$.left", isEmptyOrNullString()))
                .andDo(print());

        MockHttpServletRequestBuilder processDiffRequest = MockMvcRequestBuilders
                .post("/v1/diff/test");

        super.executeCall(processDiffRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ErrorCode.MISSING_DIFF_DATA.getDescription()))
                .andDo(print());
    }
}
