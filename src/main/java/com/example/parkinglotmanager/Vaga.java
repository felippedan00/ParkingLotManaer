package com.example.parkinglotmanager;

import java.time.LocalDateTime;

public class Vaga {
    private int numero; // Número da vaga
    private boolean ocupada;
    private Veiculo veiculo;
    private LocalDateTime horaEntrada;

    // Construtor atualizado para definir o número da vaga
    public Vaga(int numero) {
        this.numero = numero;
        this.ocupada = false;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void ocupar(Veiculo veiculo) {
        this.ocupada = true;
        this.veiculo = veiculo;
        this.horaEntrada = LocalDateTime.now();
    }

    public void liberar() {
        this.ocupada = false;
        this.veiculo = null;
        this.horaEntrada = null;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public String getPlaca() {
        if (veiculo != null) {
            return veiculo.getPlaca();
        }
        return null;
    }

    // Método para obter o número da vaga
    public int getNumero() {
        return numero;
    }
}
