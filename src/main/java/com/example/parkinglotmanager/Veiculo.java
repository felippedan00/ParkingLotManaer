package com.example.parkinglotmanager;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;

public class Veiculo {
    private StringProperty placa;
    private StringProperty modelo;
    private LocalDateTime entrada; // Mudado para LocalDateTime
    private StringProperty vaga;
    private LocalDateTime dataSaida;

    public Veiculo(String placa, String modelo, LocalDateTime entrada, String vaga) {
        this.placa = new SimpleStringProperty(placa);
        this.modelo = new SimpleStringProperty(modelo);
        this.entrada = entrada; // Agora é LocalDateTime
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

    public LocalDateTime getEntrada() {
        return entrada;
    }

    // Método para retornar a propriedade entrada
    public ObjectProperty<LocalDateTime> entradaProperty() {
        return new SimpleObjectProperty<>(entrada);
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
