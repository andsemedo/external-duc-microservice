package com.andsemedodev.externalducmicroservice.dto;

import com.andsemedodev.externalducmicroservice.validator.ValidDucRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@ValidDucRequest
public class DucTransactionRequestDto {
    @NotNull(message = "pValor é obrigatório")
    private Integer pValor;
    private Integer pRecebedoria;
    private Integer pNif;
    private String pObs;
    private String pCodTransacao;
    private String pCodTransacao1;
    private Integer pValor1;
    private String pCodTransacao2;
    private Integer pValor2;
    private List<Rubricas> rubricas;
    @NotNull(message = "flagCreationType é obrigatório")
    private Boolean flagCreationType;

    public Integer getpValor() {
        return pValor;
    }

    public void setpValor(Integer pValor) {
        this.pValor = pValor;
    }

    public Integer getpRecebedoria() {
        return pRecebedoria;
    }

    public void setpRecebedoria(Integer pRecebedoria) {
        this.pRecebedoria = pRecebedoria;
    }

    public Integer getpNif() {
        return pNif;
    }

    public void setpNif(Integer pNif) {
        this.pNif = pNif;
    }

    public String getpObs() {
        return pObs;
    }

    public void setpObs(String pObs) {
        this.pObs = pObs;
    }

    public String getpCodTransacao() {
        return pCodTransacao;
    }

    public void setpCodTransacao(String pCodTransacao) {
        this.pCodTransacao = pCodTransacao;
    }

    public String getpCodTransacao1() {
        return pCodTransacao1;
    }

    public void setpCodTransacao1(String pCodTransacao1) {
        this.pCodTransacao1 = pCodTransacao1;
    }

    public Integer getpValor1() {
        return pValor1;
    }

    public void setpValor1(Integer pValor1) {
        this.pValor1 = pValor1;
    }

    public String getpCodTransacao2() {
        return pCodTransacao2;
    }

    public void setpCodTransacao2(String pCodTransacao2) {
        this.pCodTransacao2 = pCodTransacao2;
    }

    public Integer getpValor2() {
        return pValor2;
    }

    public void setpValor2(Integer pValor2) {
        this.pValor2 = pValor2;
    }

    public Boolean getFlagCreationType() {
        return flagCreationType;
    }

    public void setFlagCreationType(Boolean flagCreationType) {
        this.flagCreationType = flagCreationType;
    }

    public List<Rubricas> getRubricas() {
        return rubricas;
    }

    public void setRubricas(List<Rubricas> rubricas) {
        this.rubricas = rubricas;
    }
}
