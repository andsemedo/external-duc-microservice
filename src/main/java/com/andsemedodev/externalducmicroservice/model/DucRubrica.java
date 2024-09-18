package com.andsemedodev.externalducmicroservice.model;

import com.andsemedodev.externalducmicroservice.shared.BaseEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "duc_rubrica")
public class DucRubrica extends BaseEntity {
    private String codRubrica;
    private Integer valor;
    @ManyToOne
    @JoinColumn(
            name = "id_duc",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_duc_id")
    )
    private Duc duc;

    public DucRubrica() {
    }

    public DucRubrica(String codRubrica, Integer valor, Duc duc) {
        this.codRubrica = codRubrica;
        this.valor = valor;
        this.duc = duc;
    }

    public String getCodRubrica() {
        return codRubrica;
    }

    public void setCodRubrica(String codRubrica) {
        this.codRubrica = codRubrica;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Duc getDuc() {
        return duc;
    }

    public void setDuc(Duc duc) {
        this.duc = duc;
    }

    @Override
    public String toString() {
        return "DucRubrica{" +
                "id=" + getId() +
                ", codRubrica='" + codRubrica + '\'' +
                ", valor=" + valor +
                '}';
    }
}
