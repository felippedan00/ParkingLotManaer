package com.example.parkinglotmanager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.Duration;


public class Main extends Application {

    private ObservableList<Veiculo> veiculosEstacionados = FXCollections.observableArrayList();
    private Scene cenaInicial;
    private Estacionamento estacionamento = new Estacionamento(50); // Estacionamento com 50 vagas

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Gerenciamento de Estacionamento - JF PARK");

        // Botões da tela inicial
        Button btnEntrada = new Button("Entrada");
        Button btnSaida = new Button("Saída");
        Button btnResumo = new Button("Resumo");

        // Layout da tela inicial
        VBox layoutInicial = new VBox(10, btnEntrada, btnSaida, btnResumo);
        layoutInicial.setAlignment(Pos.CENTER);

        cenaInicial = new Scene(layoutInicial, 400, 300);
        primaryStage.setScene(cenaInicial);
        primaryStage.show();

        // Ações dos botões
        btnEntrada.setOnAction(e -> mostrarTelaEntrada(primaryStage));
        btnSaida.setOnAction(e -> mostrarTelaSaida(primaryStage));
        btnResumo.setOnAction(e -> mostrarTelaResumo(primaryStage));
    }

    private boolean validarPlaca(String placa) {
        String regexAntiga = "^[A-Z]{3}\\d{4}$"; // Modelo antigo
        String regexMercosul = "^[A-Z]{3}\\d[A-Z]\\d{2}$"; // Modelo Mercosul
        return placa.matches(regexAntiga) || placa.matches(regexMercosul);
    }

    private void mostrarTelaEntrada(Stage primaryStage) {
        // Componentes da tela de entrada
        Label labelEntrada = new Label("Registrar Entrada de Veículo");
        TextField placaInput = new TextField();
        placaInput.setPromptText("Digite a placa");
        TextField modeloInput = new TextField();
        modeloInput.setPromptText("Digite o modelo");
        Button btnRegistrar = new Button("Registrar Entrada");
        Button btnVoltar = new Button("Voltar");

        // Tabela para exibir veículos estacionados
        TableView<Veiculo> tabelaVeiculos = new TableView<>(veiculosEstacionados);
        TableColumn<Veiculo, String> colunaPlaca = new TableColumn<>("Placa");
        colunaPlaca.setCellValueFactory(cellData -> cellData.getValue().placaProperty());
        TableColumn<Veiculo, String> colunaModelo = new TableColumn<>("Modelo");
        colunaModelo.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
        TableColumn<Veiculo, String> colunaEntrada = new TableColumn<>("Hora de Entrada");
        colunaEntrada.setCellValueFactory(cellData -> cellData.getValue().entradaProperty());

        // Nova coluna para exibir a vaga
        TableColumn<Veiculo, String> colunaVaga = new TableColumn<>("Vaga");
        colunaVaga.setCellValueFactory(cellData -> cellData.getValue().vagaProperty());

        tabelaVeiculos.getColumns().addAll(colunaPlaca, colunaModelo, colunaEntrada, colunaVaga);

        // Ação do botão de registrar entrada
        btnRegistrar.setOnAction(e -> {
            String placa = placaInput.getText();
            String modelo = modeloInput.getText();
            if (!placa.isEmpty() && !modelo.isEmpty()) {
                if (validarPlaca(placa)) {
                    LocalDateTime agora = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    String horaEntrada = agora.format(formatter);

                    // Encontra uma vaga disponível
                    Vaga vaga = estacionamento.encontrarVagaDisponivel();
                    if (vaga != null) {
                        Veiculo veiculo = new Veiculo(placa, modelo, horaEntrada, String.valueOf(vaga.getNumero()));
                        veiculosEstacionados.add(veiculo);
                        vaga.ocupar(veiculo);
                        placaInput.clear();
                        modeloInput.clear();
                    } else {
                        System.out.println("Estacionamento cheio.");
                    }
                } else {
                    System.out.println("Placa inválida! Insira uma placa no modelo antigo ou Mercosul.");
                }
            } else {
                System.out.println("Placa e modelo são obrigatórios!");
            }
        });

        // Ação do botão voltar
        btnVoltar.setOnAction(e -> primaryStage.setScene(cenaInicial));

        // Layout da tela de entrada
        HBox inputs = new HBox(10, placaInput, modeloInput, btnRegistrar);
        inputs.setAlignment(Pos.CENTER);

        BorderPane layoutEntrada = new BorderPane();
        layoutEntrada.setTop(btnVoltar);
        layoutEntrada.setCenter(new VBox(20, labelEntrada, inputs, tabelaVeiculos));
        BorderPane.setAlignment(btnVoltar, Pos.TOP_LEFT);

        Scene cenaEntrada = new Scene(layoutEntrada, 600, 400);
        primaryStage.setScene(cenaEntrada);
    }

    private void mostrarTelaSaida(Stage primaryStage) {
        // Componentes da tela de saída
        Label labelSaida = new Label("Registrar Saída de Veículo");
        TextField placaInput = new TextField();
        placaInput.setPromptText("Digite a placa");
        Button btnBuscar = new Button("Buscar");
        Button btnVoltar = new Button("Voltar");

        // Tabela para exibir veículos estacionados
        TableView<Veiculo> tabelaVeiculos = new TableView<>(veiculosEstacionados);
        TableColumn<Veiculo, String> colunaPlaca = new TableColumn<>("Placa");
        colunaPlaca.setCellValueFactory(cellData -> cellData.getValue().placaProperty());
        TableColumn<Veiculo, String> colunaModelo = new TableColumn<>("Modelo");
        colunaModelo.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
        TableColumn<Veiculo, String> colunaEntrada = new TableColumn<>("Hora de Entrada");
        colunaEntrada.setCellValueFactory(cellData -> cellData.getValue().entradaProperty());
        TableColumn<Veiculo, String> colunaVaga = new TableColumn<>("Vaga");
        colunaVaga.setCellValueFactory(cellData -> cellData.getValue().vagaProperty());

        tabelaVeiculos.getColumns().addAll(colunaPlaca, colunaModelo, colunaEntrada, colunaVaga);

        // Ação do botão buscar
        btnBuscar.setOnAction(e -> {
            String placa = placaInput.getText();
            if (!placa.isEmpty()) {
                estacionamento.registrarSaida(placa);
                veiculosEstacionados.removeIf(veiculo -> veiculo.getPlaca().equals(placa));
                placaInput.clear();
            } else {
                System.out.println("Placa é obrigatória!");
            }
        });

        // Ação do botão voltar
        btnVoltar.setOnAction(e -> primaryStage.setScene(cenaInicial));

        // Layout da tela de saída
        HBox inputs = new HBox(10, placaInput, btnBuscar);
        inputs.setAlignment(Pos.CENTER);

        BorderPane layoutSaida = new BorderPane();
        layoutSaida.setTop(btnVoltar);
        layoutSaida.setCenter(new VBox(20, labelSaida, inputs, tabelaVeiculos));
        BorderPane.setAlignment(btnVoltar, Pos.TOP_LEFT);

        Scene cenaSaida = new Scene(layoutSaida, 600, 400);
        primaryStage.setScene(cenaSaida);
    }

    private void mostrarTelaResumo(Stage primaryStage) {
// Implementação da tela de resumo
        Label labelResumo = new Label("Resumo de Faturamento");
        Label labelNumeroClientes = new Label("Número de Clientes:");
        Label labelFaturamento = new Label("Faturamento:");
        TextField campoClientes = new TextField();
        campoClientes.setEditable(false); // Apenas leitura
        TextField campoFaturamento = new TextField();
        campoFaturamento.setEditable(false); // Apenas leitura
        Button btnDiario = new Button("Diário");
        Button btnSemanal = new Button("Semanal");
        Button btnMensal = new Button("Mensal");
        Button btnTrimestral = new Button("Trimestral");
        Button btnSemestral = new Button("Semestral");
        Button btnAnual = new Button("Anual");
        Button btnVoltar = new Button("Voltar");

        // Layout dos botões de período
        HBox botoesPeriodo = new HBox(10, btnDiario, btnSemanal, btnMensal, btnTrimestral, btnSemestral, btnAnual);
        botoesPeriodo.setAlignment(Pos.CENTER);

        VBox layoutResumo = new VBox(10, labelResumo, botoesPeriodo, labelNumeroClientes, campoClientes, labelFaturamento, campoFaturamento);
        layoutResumo.setAlignment(Pos.CENTER);

        BorderPane layoutResumoTela = new BorderPane();
        layoutResumoTela.setTop(btnVoltar);
        layoutResumoTela.setCenter(layoutResumo);
        BorderPane.setAlignment(btnVoltar, Pos.TOP_LEFT);

        Scene cenaResumo = new Scene(layoutResumoTela, 600, 400);
        primaryStage.setScene(cenaResumo);

        // Ação do botão voltar
        btnVoltar.setOnAction(e -> primaryStage.setScene(cenaInicial));

        // Ações dos botões de período
        btnDiario.setOnAction(e -> {
            int numClientes = calcularNumeroClientes("diario");
            double faturamento = calcularFaturamento("diario");
            campoClientes.setText(String.valueOf(numClientes));
            campoFaturamento.setText(String.format("R$ %.2f", faturamento));
        });

        btnSemanal.setOnAction(e -> {
            int numClientes = calcularNumeroClientes("semanal");
            double faturamento = calcularFaturamento("semanal");
            campoClientes.setText(String.valueOf(numClientes));
            campoFaturamento.setText(String.format("R$ %.2f", faturamento));
        });

        btnMensal.setOnAction(e -> {
            int numClientes = calcularNumeroClientes("mensal");
            double faturamento = calcularFaturamento("mensal");
            campoClientes.setText(String.valueOf(numClientes));
            campoFaturamento.setText(String.format("R$ %.2f", faturamento));
        });

        btnTrimestral.setOnAction(e -> {
            int numClientes = calcularNumeroClientes("trimestral");
            double faturamento = calcularFaturamento("trimestral");
            campoClientes.setText(String.valueOf(numClientes));
            campoFaturamento.setText(String.format("R$ %.2f", faturamento));
        });

        btnSemestral.setOnAction(e -> {
            int numClientes = calcularNumeroClientes("semestral");
            double faturamento = calcularFaturamento("semestral");
            campoClientes.setText(String.valueOf(numClientes));
            campoFaturamento.setText(String.format("R$ %.2f", faturamento));
        });

        btnAnual.setOnAction(e -> {
            int numClientes = calcularNumeroClientes("anual");
            double faturamento = calcularFaturamento("anual");
            campoClientes.setText(String.valueOf(numClientes));
            campoFaturamento.setText(String.format("R$ %.2f", faturamento));
        });
    }

    // Métodos para calcular o número de clientes e faturamento
    private int calcularNumeroClientes(String periodo) {
        int numeroClientes = 0;
        LocalDateTime agora = LocalDateTime.now();

        for (Veiculo veiculo : veiculosEstacionados) {
            if (pertenceAoPeriodo(veiculo.getEntrada(), periodo, agora)) {
                numeroClientes++;
            }
        }

        return numeroClientes;
    }

    private double calcularFaturamento(String periodo) {
        double faturamento = 0.0;
        LocalDateTime agora = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // Formato correto de data e hora
        for (Veiculo veiculo : veiculosEstacionados) {
            LocalDateTime entrada = LocalDateTime.parse(veiculo.getEntrada(), formatter); // Conversão correta
            if (pertenceAoPeriodo(entrada, periodo, agora)) {
                faturamento += calcularPagamento(entrada);
            }
        }
        return faturamento;
    }

    private boolean pertenceAoPeriodo(String dataEntrada, String periodo, LocalDateTime agora) {
        switch (periodo) {
            case "diario":
                return entrada.isAfter(agora.minusDays(1));
            case "semanal":
                return entrada.isAfter(agora.minusWeeks(1));
            case "mensal":
                return entrada.isAfter(agora.minusMonths(1));
            case "trimestral":
                return entrada.isAfter(agora.minusMonths(3));
            case "semestral":
                return entrada.isAfter(agora.minusMonths(6));
            case "anual":
                return entrada.isAfter(agora.minusYears(1));
            default:
                return false;
        }
    }

    private double calcularPagamento(LocalDateTime horaEntrada) {
        long minutos = Duration.between(horaEntrada, LocalDateTime.now()).toMinutes();
        if (minutos <= 15) {
            return 0.0;
        } else if (minutos <= 30) {
            return 7.0;
        } else if (minutos <= 60) {
            return 10.0;
        } else {
            return 15.0;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}