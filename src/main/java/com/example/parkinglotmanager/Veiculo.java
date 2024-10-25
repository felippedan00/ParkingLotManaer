package com.example.parkinglotmanager;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;


public class Veiculo {
    private StringProperty placa;
    private StringProperty modelo;
    private StringProperty entrada;
    private StringProperty vaga;
    private LocalDateTime dataSaida;

    public Veiculo(String placa, String modelo, String entrada, String vaga) {
        this.placa = new SimpleStringProperty(placa);
        this.modelo = new SimpleStringProperty(modelo);
        this.entrada = new SimpleStringProperty(entrada);
        this.vaga = new SimpleStringProperty(vaga);
        this.dataSaida = null;
    }

    public StringProperty placaProperty() {
        return placa;
    }

    public String getPlaca() {
        return placa.get();
    }

    public StringProperty modeloProperty() {
        return modelo;
    }

    public String getModelo() {
        return modelo.get();
    }

    public StringProperty entradaProperty() {
        return entrada;
    }

    public String getEntrada() {
        return entrada.get();
    }

    public StringProperty vagaProperty() {
        return vaga;
    }

    public String getVaga() {
        return vaga.get();
    }

    public void registrarSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }
}
