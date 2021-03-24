package com.diff.exception.errors;

public enum ErrorCode {

    CONTENT_NOT_FOUND ("0001", "Content not found", 404),

    UNSUPPORTED_FORMAT ("0002", "Content format is not supported.", 400),

    UNSUPPORTED_ENCODING ("0003", "Content encoding is unsupported and it cannot be decoded", 400),

    FIELD_VALIDATION_ERROR ("0004", "Field validation error", 400),

    MISSING_DIFF_DATA ("0005", "Diff must have both sides submitted", 400);

    private String code;
    private String description;
    private int httpStatusCode;


    ErrorCode(String code, String description, int httpStatusCode) {
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}