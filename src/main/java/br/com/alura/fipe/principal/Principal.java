package br.com.alura.fipe.principal;

import br.com.alura.fipe.model.*;
import br.com.alura.fipe.service.ConsumoApi;
import br.com.alura.fipe.service.ConversorDados;

import javax.sound.sampled.BooleanControl;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    Scanner scan = new Scanner(System.in);
    private final List<String> veiculos = Arrays.asList("carros", "motos", "caminhoes");
    private final static String MARCAS = "/marcas/";
    private final static String MODELOS = "/modelos/";
    private final static String ANOS = "/anos/";
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConversorDados conversor = new ConversorDados();
    private static final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeMenu() throws IOException, RuntimeException, Exception {
        System.out.println("Escolha uma opção: ");
        System.out.println("- CARROS \n- MOTOS \n- CAMINHOES");
        String opVeiculo = scan.nextLine().toLowerCase();

        var json = escolherVeiculo(opVeiculo);

        List<MarcaDados> listaMarcas = Arrays.asList(conversor
                .converteDados(json, MarcaDados[].class));
        listaMarcas.forEach(m -> System.out.println(
                "Marca: " + m.marca() + " - Código: " + m.codigo()));

        System.out.println("\nEscolha uma marca, digite o código: ");
        String marca = scan.nextLine();

        ModeloResponse modeloResponse = escolherMarca(marca, opVeiculo);
        List<ModelosDados> modelos = modeloResponse.getModelos();
        modelos.forEach(m -> System.out.println(
                "Modelo: " + m.modelo() + " - Código: " + m.codigo()
        ));

        System.out.println("\nEscolha o modelo, digite o nome ou trecho dele: ");
        String modelo = scan.nextLine().toUpperCase();

        var opcoes = modelos.stream()
                .filter(m -> m.modelo().toUpperCase().contains(modelo)).findAny();

        if(opcoes.isPresent()){
            System.out.println("Modelos com esse nome: ");
            modelos.stream()
                    .filter(m -> m.modelo().toUpperCase().contains(modelo))
                    .forEach(m -> System.out.println(
                            "Modelo: " + m.modelo() + " - Código: " + m.codigo()
                    ));
        } else {
            throw new RuntimeException("Não foi possível achar esse modelo na lista.");
        }

        System.out.println("\nEscolha um modelo, digite o código: ");
        String modeloCodigo = scan.nextLine();


        var anos = escolherModelo(modeloCodigo, marca, opVeiculo);
        List<String> anosLista = anos.stream().map(AnosDados::codigo)
                .collect(Collectors.toList());

        List<FipeDados> listaFipe = new ArrayList<>();

        for (String ano : anosLista) {
            var jsonFipe = consumoApi.obterDados(URL_BASE + opVeiculo + MARCAS + marca + MODELOS + modeloCodigo + ANOS + ano);
            FipeDados fipe = conversor.converteDados(jsonFipe, FipeDados.class);
            listaFipe.add(fipe);
        }

        listaFipe.forEach(f -> System.out.println(
                "Modelo: " + f.marca() + " " + f.modelo() + " Ano: " + f.ano() +
                        " Valor: " + f.valor() + " Combustível: " + f.combustivel()
        ));

    }

    public String escolherVeiculo(String option) {

        if (veiculos.contains(option)) {
            var json = consumoApi.obterDados(URL_BASE + option + MARCAS);
            return json;
        } else {
            throw new RuntimeException("Escolha uma opção válida.");
        }
    }

    public ModeloResponse escolherMarca(String numero, String veiculo) {
        try {
            Integer num = Integer.parseInt(numero);

            if (num == 0){
                throw new RuntimeException("O número não pode ser nulo.");
            }

            String json = consumoApi.obterDados(URL_BASE + veiculo + MARCAS + num + MODELOS);
            var dados = conversor.converteDados(json, ModeloResponse.class);

            if (!json.contains("error")){
                return dados;
            } else {
                throw new RuntimeException("Não foi possível achar a marca com esse número. Número: " + numero);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("A opção deve ser um código em número.");
        }
    }

    public List<AnosDados> escolherModelo(String modelo, String marca, String veiculo) {
        try {
            Integer num = Integer.parseInt(modelo);

            if (num == 0){
                throw new RuntimeException("O número não pode ser nulo.");
            }

            String json = consumoApi.obterDados(URL_BASE + veiculo + MARCAS + marca + MODELOS + num + ANOS);
            if(json.contains("error")){
                throw new RuntimeException("Não foi possivel achar dados desse modelo.");
            }
            List<AnosDados> anos = Arrays.asList(conversor.converteDados(json, AnosDados[].class));
            return anos;

        }catch (NumberFormatException e){
            throw new RuntimeException("A opção deve ser um código em número.");
        }
    }

}
