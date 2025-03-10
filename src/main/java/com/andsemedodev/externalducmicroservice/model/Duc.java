package com.andsemedodev.externalducmicroservice.model;

import com.andsemedodev.externalducmicroservice.dto.RubricasDto;
import com.andsemedodev.externalducmicroservice.shared.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Duc extends BaseEntity {

    private Integer pValor;
    private String pMoeda;
    private String pRecebedoria;
    private String pEmail;
    private BigInteger pNif;
    @Column(length = 500)
    private String pObs;
    private String pCodTransacao;
    private String pCodTransacao1;
    private Integer pValor1;
    private String pCodTransacao2;
    private Integer pValor2;
    private String instituicao;
    private String departamento;
    private String plataforma;
    @Column(columnDefinition = "TEXT")
    private String notas;
    @OneToMany(
            mappedBy = "duc",
            cascade = CascadeType.ALL
    )
    private List<DucRubrica> rubricaList;


    public Duc() {
    }

    public Duc(Integer pValor, String pMoeda, String pRecebedoria, String pEmail, BigInteger pNif, String pObs, String pCodTransacao, String pCodTransacao1, Integer pValor1, String pCodTransacao2, Integer pValor2, String instituicao, String departamento, String plataforma, String notas) {
        this.pValor = pValor;
        this.pMoeda = pMoeda;
        this.pRecebedoria = pRecebedoria;
        this.pEmail = pEmail;
        this.pNif = pNif;
        this.pObs = pObs;
        this.pCodTransacao = pCodTransacao;
        this.pCodTransacao1 = pCodTransacao1;
        this.pValor1 = pValor1;
        this.pCodTransacao2 = pCodTransacao2;
        this.pValor2 = pValor2;
        this.instituicao = instituicao;
        this.departamento = departamento;
        this.plataforma = plataforma;
        this.notas = notas;
    }

    public Integer getpValor() {
        return pValor;
    }

    public void setpValor(Integer pValor) {
        this.pValor = pValor;
    }

    public String getpMoeda() {
        return pMoeda;
    }

    public void setpMoeda(String pMoeda) {
        this.pMoeda = pMoeda;
    }

    public String getpRecebedoria() {
        return pRecebedoria;
    }

    public void setpRecebedoria(String pRecebedoria) {
        this.pRecebedoria = pRecebedoria;
    }

    public String getpEmail() {
        return pEmail;
    }

    public void setpEmail(String pEmail) {
        this.pEmail = pEmail;
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

    public List<DucRubrica> getRubricaList() {
        return rubricaList;
    }

    public void setRubricaList(List<DucRubrica> rubricaList) {
        this.rubricaList = rubricaList;
    }
}
