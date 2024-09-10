package com.andsemedodev.externalducmicroservice.service.impl.rubrica_request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RubricaRequest {
    @JsonProperty("ProcessBancaArrayId")
    private ProcessBancaArrayId processBancaArrayId;

    public RubricaRequest() {
    }

    public RubricaRequest(ProcessBancaArrayId processBancaArrayId) {
        this.processBancaArrayId = processBancaArrayId;
    }

    public ProcessBancaArrayId getProcessBancaArrayId() {
        return processBancaArrayId;
    }

    public void setProcessBancaArrayId(ProcessBancaArrayId processBancaArrayId) {
        this.processBancaArrayId = processBancaArrayId;
    }
}
