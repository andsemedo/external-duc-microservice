package com.andsemedodev.externalducmicroservice.service.impl.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DucByIdRubricas {
    @JsonProperty("ducByIdRubrica")
    private DucPSaida ducPSaida;

    public DucByIdRubricas() {
    }

    public DucPSaida getDucByIdRubrica() {
        return ducPSaida;
    }

    public void setDucByIdRubrica(DucPSaida ducPSaida) {
        this.ducPSaida = ducPSaida;
    }
}
