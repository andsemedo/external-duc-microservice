package com.andsemedodev.externalducmicroservice.repository;

import com.andsemedodev.externalducmicroservice.model.Duc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DucRepository extends JpaRepository<Duc, UUID> {
}
