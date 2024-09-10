package com.andsemedodev.externalducmicroservice.service.impl.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DucByIdRubrica {
    @JsonProperty("p_saida")
    private String pSaida;

    public DucByIdRubrica() {
    }

    public String getpSaida() {
        return pSaida;
    }

    public void setpSaida(String pSaida) {
        this.pSaida = pSaida;
    }
}
