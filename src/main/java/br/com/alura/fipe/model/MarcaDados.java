package br.com.alura.fipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MarcaDados(@JsonAlias("codigo") String codigo,
                         @JsonAlias("nome") String marca) {
}
