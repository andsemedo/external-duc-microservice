package com.andsemedodev.externalducmicroservice.service.impl.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenerateDucResponse {

    private DucByIdRubricas ducByIdRubricas;

    public GenerateDucResponse() {
    }

    public DucByIdRubricas getDucByIdRubricas() {
        return ducByIdRubricas;
    }

    public void setDucByIdRubricas(DucByIdRubricas ducByIdRubricas) {
        this.ducByIdRubricas = ducByIdRubricas;
    }


}
