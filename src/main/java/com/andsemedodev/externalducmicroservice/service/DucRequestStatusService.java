package com.andsemedodev.externalducmicroservice.service;

public interface DucRequestStatusService {
    boolean hasFailed(String requestId);
    void markAsFailed(String requestId);
}
