package com.andsemedodev.externalducmicroservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DucRequestStatus {
    @Id
    private String requestId;
    private boolean failed;

    public DucRequestStatus() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }
}
