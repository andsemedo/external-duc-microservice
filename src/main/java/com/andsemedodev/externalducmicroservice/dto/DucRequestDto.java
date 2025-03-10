package com.andsemedodev.externalducmicroservice.dto;

import com.andsemedodev.externalducmicroservice.validator.ValidDucRequest;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@ValidDucRequest
public class DucRequestDto {

    private String requestId;

    @NotNull(message = "pValor é obrigatório")
    private Integer pValor;
    @NotNull(message = "pRecebedoria é obrigatório")
    private String pRecebedoria;
    @NotNull(message = "pNif é obrigatório")
    private BigInteger pNif;
    private String pObs;
    private String pCodTransacao;
    private String pCodTransacao1;
    private Integer pValor1;
    private String pCodTransacao2;
    private Integer pValor2;
    private List<RubricasDto> rubricas;
    @NotNull(message = "flagIsByTransacao é obrigatório")
    private Boolean flagIsByTransacao;
    private String instituicao;
    private String departamento;
    private String plataforma;
    private String notas;

    public DucRequestDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getpValor() {
        return pValor;
    }

    public void setpValor(Integer pValor) {
        this.pValor = pValor;
    }

    public String getpRecebedoria() {
        return pRecebedoria;
    }

    public void setpRecebedoria(String pRecebedoria) {
        this.pRecebedoria = pRecebedoria;
    }

    public BigInteger getpNif() {
        return pNif;
    }

    public void setpNif(BigInteger pNif) {
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

    public Boolean getFlagIsByTransacao() {
        return flagIsByTransacao;
    }

    public void setFlagIsByTransacao(Boolean flagIsByTransacao) {
        this.flagIsByTransacao = flagIsByTransacao;
    }

    public List<RubricasDto> getRubricas() {
        return rubricas;
    }

    public void setRubricas(List<RubricasDto> rubricas) {
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

    @Override
    public String toString() {
        return "DucRequestDto{" +
                "pValor=" + pValor +
                ", pRecebedoria='" + pRecebedoria + '\'' +
                ", pNif=" + pNif +
                ", pObs='" + pObs + '\'' +
                ", pCodTransacao='" + pCodTransacao + '\'' +
                ", pCodTransacao1='" + pCodTransacao1 + '\'' +
                ", pValor1=" + pValor1 +
                ", pCodTransacao2='" + pCodTransacao2 + '\'' +
                ", pValor2=" + pValor2 +
                ", rubricas=" + rubricas +
                ", flagIsByTransacao=" + flagIsByTransacao +
                ", instituicao='" + instituicao + '\'' +
                ", departamento='" + departamento + '\'' +
                ", plataforma='" + plataforma + '\'' +
                ", notas='" + notas + '\'' +
                '}';
    }
}
