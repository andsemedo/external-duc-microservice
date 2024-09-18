package com.andsemedodev.externalducmicroservice.repository;

import com.andsemedodev.externalducmicroservice.model.DucRubrica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DucRubricaRepository extends JpaRepository<DucRubrica, UUID> {
}
