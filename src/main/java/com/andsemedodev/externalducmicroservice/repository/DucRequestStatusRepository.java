package com.andsemedodev.externalducmicroservice.repository;

import com.andsemedodev.externalducmicroservice.model.DucRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DucRequestStatusRepository extends JpaRepository<DucRequestStatus, String> {
    boolean existsByRequestId(String requestId);
}
