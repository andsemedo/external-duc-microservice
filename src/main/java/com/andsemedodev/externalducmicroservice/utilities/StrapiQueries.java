package com.andsemedodev.externalducmicroservice.utilities;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StrapiQueries {

    public String getRubricasRecebedoria(String codigoRubrica, List<String> rubricas) {
        String rubricasFormatted = rubricas.stream()
                .map(r -> "\"" + r + "\"")
                .collect(Collectors.joining(", "));

        return String.format("""
                query Recebedorias {
                     recebedorias(filters: { codigo: { eq: "%s" } }) {
                         codigo
                         departamento
                         email
                         instituicao
                         rubricas(filters: { rubrica_id: { in: [%s] } }) {
                             rubrica_id
                             descricao
                         }
                     }
                 }
                """, codigoRubrica, rubricasFormatted);
    }

}
