package com.andsemedodev.externalducmicroservice.rabbitmq;

import java.io.Serializable;

public class FailedDucRequest implements Serializable {
    private String requestType; // "RUBRICA" or "TRANSAÇÃO"
    private String requestBody;

    public FailedDucRequest() {
    }

    public FailedDucRequest(String requestType, String requestBody) {
        this.requestType = requestType;
        this.requestBody = requestBody;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}
