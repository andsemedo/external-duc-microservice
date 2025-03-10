package com.andsemedodev.externalducmicroservice.service.impl.responses.strapi;

public class StrapiRubrica {
    private String rubrica_id;
    private String descricao;

    public StrapiRubrica() {
    }

    public String getRubrica_id() {
        return rubrica_id;
    }

    public void setRubrica_id(String rubrica_id) {
        this.rubrica_id = rubrica_id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
