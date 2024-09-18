package com.andsemedodev.externalducmicroservice.service.impl.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DucByTransacaoResponse {
    @JsonProperty("p_saida")
    private DucPSaida ducPSaida;

    public DucPSaida getPSaida() {
        return ducPSaida;
    }

    public void setPSaida(DucPSaida ducPSaida) {
        this.ducPSaida = ducPSaida;
    }
}
