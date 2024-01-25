package br.com.alura.fipe.principal;

import br.com.alura.fipe.model.*;
import br.com.alura.fipe.service.ConsumoApi;
import br.com.alura.fipe.service.ConversorDados;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    Scanner scan = new Scanner(System.in);
    private final static String MARCAS = "/marcas/";
    private final static String MODELOS = "/modelos/";
    private final static String ANOS = "/anos/";
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConversorDados conversor = new ConversorDados();
    private static final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeMenu(){
        System.out.println("Escolha uma opção: ");
        System.out.println("CARROS \nMOTOS \nCAMINHOES");
        String opVeiculo = scan.nextLine().toLowerCase();

        var json = consumoApi.obterDados(URL_BASE+ opVeiculo + MARCAS);
        List<MarcaDados> listaMarcas = Arrays.asList(conversor
                .converteDados(json, MarcaDados[].class));
        listaMarcas.forEach(m -> System.out.println(
                "Marca: " + m.marca() + " - Código: " + m.codigo()));

        System.out.println("\nEscolha uma marca, digite o código: ");
        Integer marca = scan.nextInt();
        scan.nextLine();

        var json2 = consumoApi.obterDados(URL_BASE+opVeiculo+MARCAS+marca+MODELOS);
        ModeloResponse modeloResponse = conversor.converteDados(json2, ModeloResponse.class);
        List<ModelosDados> modelos = modeloResponse.getModelos();
        modelos.forEach(m -> System.out.println(
                "Modelo: " + m.modelo() + " - Código: " + m.codigo()
        ));

        System.out.println("\nEscolha o modelo, digite o nome ou trecho dele: ");
        String modelo = scan.nextLine().toUpperCase();

        System.out.println("Modelos com esse nome: ");
        modelos.stream()
                .filter(m -> m.modelo().toUpperCase().contains(modelo))
                .forEach(m -> System.out.println(
                        "Modelo: " + m.modelo() + " - Código: " + m.codigo()
                ));

        System.out.println("\nEscolha um modelo, digite o código: ");
        Integer modeloCodigo = scan.nextInt();
        scan.nextLine();

        var json3 = consumoApi.obterDados(URL_BASE+opVeiculo+MARCAS+marca+MODELOS+modeloCodigo+ANOS);
        List<AnosDados> anos = Arrays.asList(conversor.converteDados(json3, AnosDados[].class));
        List<String> anosLista = anos.stream().map(AnosDados::codigo)
                .collect(Collectors.toList());

        List<FipeDados> listaFipe = new ArrayList<>();

        for (String ano: anosLista) {
            var jsonFipe = consumoApi.obterDados(URL_BASE+opVeiculo+MARCAS+marca+MODELOS+modeloCodigo+ANOS+ano);
            FipeDados fipe = conversor.converteDados(jsonFipe, FipeDados.class);
            listaFipe.add(fipe);
        }

        listaFipe.forEach(f -> System.out.println(
                "Modelo: " + f.marca() + " " + f.modelo() + " Ano: " + f.ano() +
                        " Valor: " + f.valor() + " Combustível: " + f.combustivel()
        ));

    }
}
