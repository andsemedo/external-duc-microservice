package com.andsemedodev.externalducmicroservice.utilities;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class APIResponse<T> {
    private boolean status;
    private String statusText;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T details;

    public APIResponse(buildAPIResponse<T> build) {
        this.status = build.status;
        this.statusText = build.statusText;
        this.details = build.details;
    }

    public APIResponse() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public T getDetails() {
        return details;
    }

    public void setDetails(T details) {
        this.details = details;
    }

    public static class buildAPIResponse<T> {
        boolean status;
        String statusText;
        T details;

        public buildAPIResponse<T> setStatus(boolean status) {
            this.status = status;
            return this;
        }

        public buildAPIResponse<T> setStatusText(String statusText) {
            this.statusText = statusText;
            return this;
        }

        public buildAPIResponse<T> setDetails(T details) {
            this.details = details;
            return this;
        }

        public APIResponse<T> builder() {
            return new APIResponse(this);
        }
    }
}
