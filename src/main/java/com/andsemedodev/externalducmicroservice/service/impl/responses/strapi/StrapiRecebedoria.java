package com.andsemedodev.externalducmicroservice.service.impl.responses.strapi;

import java.util.ArrayList;

public class StrapiRecebedoria {
    private String codigo;
    private String departamento;
    private String email;
    private String instituicao;
    private ArrayList<StrapiRubrica> rubricas;

    public StrapiRecebedoria() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public ArrayList<StrapiRubrica> getRubricas() {
        return rubricas;
    }

    public void setRubricas(ArrayList<StrapiRubrica> rubricas) {
        this.rubricas = rubricas;
    }
}
