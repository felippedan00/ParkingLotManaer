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
import javafx.beans.property.SimpleStringProperty;

public class Main extends Application {

    private ObservableList<Veiculo> veiculosEstacionados = FXCollections.observableArrayList();
    private Scene cenaInicial;
    private Estacionamento estacionamento = new Estacionamento(50); // Estacionamento com 50 vagas
    private int totalClientes = 0; // Contagem total de clientes que passaram pelo estacionamento
    private double totalFaturamento = 0.0; // Faturamento acumulado

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
        colunaEntrada.setCellValueFactory(cellData -> {
            LocalDateTime entrada = cellData.getValue().getEntrada();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return new SimpleStringProperty(entrada != null ? entrada.format(formatter) : "");
        });

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
                    Vaga vaga = estacionamento.encontrarVagaDisponivel();

                    if (vaga != null) {
                        Veiculo veiculo = new Veiculo(placa, modelo, agora, String.valueOf(vaga.getNumero()));
                        veiculosEstacionados.add(veiculo);
                        vaga.ocupar(veiculo);
                        placaInput.clear();
                        modeloInput.clear();

                        totalClientes++; // Incrementa o total de clientes
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
        colunaEntrada.setCellValueFactory(cellData -> {
            LocalDateTime entrada = cellData.getValue().getEntrada();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return new SimpleStringProperty(entrada != null ? entrada.format(formatter) : "");
        });
        TableColumn<Veiculo, String> colunaVaga = new TableColumn<>("Vaga");
        colunaVaga.setCellValueFactory(cellData -> cellData.getValue().vagaProperty());

        tabelaVeiculos.getColumns().addAll(colunaPlaca, colunaModelo, colunaEntrada, colunaVaga);

        // Ação do botão buscar
        btnBuscar.setOnAction(e -> {
            String placa = placaInput.getText();
            if (!placa.isEmpty()) {
                registrarSaida(placa);
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

    private void registrarSaida(String placa) {
        for (Veiculo veiculo : veiculosEstacionados) {
            if (veiculo.getPlaca().equals(placa)) {
                veiculo.registrarSaida(LocalDateTime.now()); // Define a data de saída

                // Calcula o pagamento e acumula no faturamento total
                double valorAPagar = calcularPagamento(veiculo.getEntrada());
                totalFaturamento += valorAPagar; // Acumula no faturamento

                // Exibe o valor a pagar
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Pagamento");
                alerta.setHeaderText("Valor a pagar para o veículo com placa " + placa);
                alerta.setContentText(String.format("Total a pagar: R$ %.2f", valorAPagar));
                alerta.showAndWait();

                veiculosEstacionados.remove(veiculo); // Remove o veículo da lista de estacionados
                System.out.println("Veículo saiu: " + veiculo.getPlaca());
                return;
            }
        }
        System.out.println("Veículo não encontrado.");
    }

    private void mostrarTelaResumo(Stage primaryStage) {
        Label labelResumo = new Label("Resumo de Faturamento");
        Label labelPeriodo = new Label("Período:");
        Label labelNumeroClientes = new Label("Número de Clientes:");
        Label labelFaturamento = new Label("Faturamento:");

        // Campos de texto para exibir resultados
        TextField campoClientes = new TextField();
        campoClientes.setEditable(false); // Apenas leitura
        TextField campoFaturamento = new TextField();
        campoFaturamento.setEditable(false); // Apenas leitura

        // ComboBox para selecionar o período
        ComboBox<String> comboBoxPeriodo = new ComboBox<>();
        comboBoxPeriodo.getItems().addAll("Semanal", "Mensal", "Trimestral", "Semestral", "Anual");
        comboBoxPeriodo.setValue("Semanal"); // Valor padrão

        Button btnCalcular = new Button("Calcular");
        Button btnVoltar = new Button("Voltar");

        // Exibir valores totais no início
        campoClientes.setText(String.valueOf(totalClientes)); // Total de clientes
        campoFaturamento.setText(String.format("R$ %.2f", totalFaturamento)); // Total de faturamento

        // Ação do botão calcular
        btnCalcular.setOnAction(e -> {
            String periodoSelecionado = comboBoxPeriodo.getValue();
            LocalDateTime agora = LocalDateTime.now();

            int numeroClientesPeriodo = calcularNumeroClientes(periodoSelecionado, agora);
            double faturamentoPeriodo = calcularFaturamento(periodoSelecionado, agora);

            campoClientes.setText(String.valueOf(numeroClientesPeriodo));
            campoFaturamento.setText(String.format("R$ %.2f", faturamentoPeriodo));
        });

        // Layout da tela de resumo
        VBox layoutResumo = new VBox(10, labelResumo, labelPeriodo, comboBoxPeriodo, btnCalcular,
                labelNumeroClientes, campoClientes, labelFaturamento, campoFaturamento);
        layoutResumo.setAlignment(Pos.CENTER);

        BorderPane layoutResumoTela = new BorderPane();
        layoutResumoTela.setTop(btnVoltar);
        layoutResumoTela.setCenter(layoutResumo);
        BorderPane.setAlignment(btnVoltar, Pos.TOP_LEFT);

        Scene cenaResumo = new Scene(layoutResumoTela, 600, 400);
        primaryStage.setScene(cenaResumo);

        // Ação do botão voltar
        btnVoltar.setOnAction(e -> primaryStage.setScene(cenaInicial));
    }

    private int calcularNumeroClientes(String periodo, LocalDateTime agora) {
        int numeroClientes = 0;

        for (Veiculo veiculo : estacionamento.getTodosOsVeiculos()) {
            LocalDateTime entrada = veiculo.getEntrada();
            if (pertenceAoPeriodo(entrada, periodo, agora)) {
                numeroClientes++;
            }
        }

        return numeroClientes;
    }

    private double calcularFaturamento(String periodo, LocalDateTime agora) {
        double faturamento = 0.0;

        for (Veiculo veiculo : estacionamento.getTodosOsVeiculos()) {
            LocalDateTime entrada = veiculo.getEntrada();
            if (pertenceAoPeriodo(entrada, periodo, agora)) {
                faturamento += calcularPagamento(entrada);
            }
        }

        return faturamento;
    }

    private boolean pertenceAoPeriodo(LocalDateTime entrada, String periodo, LocalDateTime agora) {
        switch (periodo.toLowerCase()) {
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
        long segundos = Duration.between(horaEntrada, LocalDateTime.now()).getSeconds();
        double taxaPorSegundo = 0.004; // Taxa em reais por segundo
        return segundos * taxaPorSegundo;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
