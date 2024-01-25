package br.com.alura.fipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FipeDados(@JsonAlias("Modelo") String modelo,
                        @JsonAlias("Marca") String marca,
                        @JsonAlias("AnoModelo") Integer ano,
                        @JsonAlias("Valor") String valor,
                        @JsonAlias("Combustivel") String combustivel) {
}
