package com.andsemedodev.externalducmicroservice.service.impl.responses.strapi;

import java.util.ArrayList;

public class StrapiRecebedoriaData {
    private ArrayList<StrapiRecebedoria> recebedorias;

    public StrapiRecebedoriaData() {
    }

    public ArrayList<StrapiRecebedoria> getRecebedorias() {
        return recebedorias;
    }

    public void setRecebedorias(ArrayList<StrapiRecebedoria> recebedorias) {
        this.recebedorias = recebedorias;
    }
}
