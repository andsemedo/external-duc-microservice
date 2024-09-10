package com.andsemedodev.externalducmicroservice.service.impl.rubrica_request;

import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessBancaArrayId {
    @JsonProperty("p_valor")
    private int pValor;
    @JsonProperty("p_moeda")
    private String pMoeda;
    @JsonProperty("p_recebedoria")
    private int pRecebedoria;
    @JsonProperty("p_email")
    private String pEmail;
    @JsonProperty("p_nif")
    private int pNif;
    @JsonProperty("p_obs")
    private String pObs;
    @JsonProperty("p_id_rubricas")
    private String pIdRubricas;
    @JsonProperty("p_vl_rubricas")
    private String pVlRubricas;

    public ProcessBancaArrayId() {
    }

    public ProcessBancaArrayId(int pValor, String pMoeda, int pRecebedoria, String pEmail, int pNif, String pObs, String pIdRubricas, String pVlRubricas) {
        this.pValor = pValor;
        this.pMoeda = pMoeda;
        this.pRecebedoria = pRecebedoria;
        this.pEmail = pEmail;
        this.pNif = pNif;
        this.pObs = pObs;
        this.pIdRubricas = pIdRubricas;
        this.pVlRubricas = pVlRubricas;
    }

    public int getpValor() {
        return pValor;
    }

    public void setpValor(int pValor) {
        this.pValor = pValor;
    }

    public String getpMoeda() {
        return pMoeda;
    }

    public void setpMoeda(String pMoeda) {
        this.pMoeda = pMoeda;
    }

    public int getpRecebedoria() {
        return pRecebedoria;
    }

    public void setpRecebedoria(int pRecebedoria) {
        this.pRecebedoria = pRecebedoria;
    }

    public String getpEmail() {
        return pEmail;
    }

    public void setpEmail(String pEmail) {
        this.pEmail = pEmail;
    }

    public int getpNif() {
        return pNif;
    }

    public void setpNif(int pNif) {
        this.pNif = pNif;
    }

    public String getpObs() {
        return pObs;
    }

    public void setpObs(String pObs) {
        this.pObs = pObs;
    }

    public String getpIdRubricas() {
        return pIdRubricas;
    }

    public void setpIdRubricas(String pIdRubricas) {
        this.pIdRubricas = pIdRubricas;
    }

    public String getpVlRubricas() {
        return pVlRubricas;
    }

    public void setpVlRubricas(String pVlRubricas) {
        this.pVlRubricas = pVlRubricas;
    }
}
