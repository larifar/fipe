package br.com.alura.fipe.service;

public interface IConverteDados {
    <T> T converteDados (String json, Class<T> classe);
}
