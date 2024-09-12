package com.andsemedodev.externalducmicroservice.dto;

import com.andsemedodev.externalducmicroservice.validator.ValidDucRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@ValidDucRequest
public class DucRequestDto {
    @NotNull(message = "pValor é obrigatório")
    private Integer pValor;
    private Integer pRecebedoria;
    private Integer pNif;
    private String pObs;
    private List<Rubricas> rubricas;
    @NotNull(message = "flagCreationType é obrigatório")
    private Boolean flagCreationType;
    private String instituicao;
    private String departamento;
    private String plataforma;
    private String notas;

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

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
