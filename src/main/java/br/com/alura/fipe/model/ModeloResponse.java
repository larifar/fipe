package br.com.alura.fipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModeloResponse {
    private List<ModelosDados> modelos;

    public List<ModelosDados> getModelos() {
        return modelos;
    }
}
