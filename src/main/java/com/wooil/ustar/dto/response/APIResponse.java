package com.wooil.ustar.dto.response;

import com.wooil.ustar.enums.ErrorCode;
import lombok.Getter;

@Getter
public class APIResponse<T> {
    private boolean isOk;
    private T data;
    private ErrorCode errorCode;
    private String errorMessage;

    /// for success no data
    public APIResponse(boolean isOk) {
        this.isOk = isOk;
        this.data = null;
        this.errorCode = null;
        this.errorMessage = null;
    }

    /// for success
    public APIResponse(boolean isOk, T data) {
        this.isOk = isOk;
        this.data = data;
        this.errorCode = null;
        this.errorMessage = null;
    }

    /// for failed
    public APIResponse(boolean isOk, ErrorCode errorCode, String errorMessage) {
        this.isOk = isOk;
        this.data = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /// for failed 2
    public APIResponse(boolean isOk, ErrorCode errorCode, String errorMessage, T data) {
        this.isOk = isOk;
        this.data = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
