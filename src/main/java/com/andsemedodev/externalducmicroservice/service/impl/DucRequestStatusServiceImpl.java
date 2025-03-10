package com.andsemedodev.externalducmicroservice.service.impl;

import com.andsemedodev.externalducmicroservice.model.DucRequestStatus;
import com.andsemedodev.externalducmicroservice.repository.DucRequestStatusRepository;
import com.andsemedodev.externalducmicroservice.service.DucRequestStatusService;
import org.springframework.stereotype.Service;

@Service
public class DucRequestStatusServiceImpl implements DucRequestStatusService {
    private final DucRequestStatusRepository ducRequestStatusRepository;

    public DucRequestStatusServiceImpl(DucRequestStatusRepository ducRequestStatusRepository) {
        this.ducRequestStatusRepository = ducRequestStatusRepository;
    }

    @Override
    public boolean hasFailed(String requestId) {
        return ducRequestStatusRepository.existsByRequestId(requestId);
    }

    @Override
    public void markAsFailed(String requestId) {
        DucRequestStatus ducRequestStatus = new DucRequestStatus();
        ducRequestStatus.setRequestId(requestId);
        ducRequestStatus.setFailed(true);
        ducRequestStatusRepository.save(ducRequestStatus);
    }
}
