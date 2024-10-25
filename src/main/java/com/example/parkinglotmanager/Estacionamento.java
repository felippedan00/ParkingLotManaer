package com.example.parkinglotmanager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Estacionamento {
    private Vaga[] vagas;
    private int totalClientes;
    private double totalFaturamento;

    public Estacionamento(int totalVagas) {
        this.vagas = new Vaga[totalVagas];
        this.totalClientes = 0;
        this.totalFaturamento = 0.0;
        for (int i = 0; i < totalVagas; i++) {
            vagas[i] = new Vaga(i + 1); // Inicializa as vagas com números de 1 a totalVagas
        }
    }

    public void registrarEntrada(Veiculo veiculo) {
        Vaga vaga = encontrarVagaDisponivel();
        if (vaga != null) {
            vaga.ocupar(veiculo);
            totalClientes++;
            System.out.println("Veículo estacionado na vaga " + vaga.getNumero());
        } else {
            System.out.println("Estacionamento cheio. Não há vagas disponíveis.");
        }
    }

    public void registrarSaida(String placa) {
        for (Vaga vaga : vagas) {
            if (vaga.isOcupada() && vaga.getVeiculo().getPlaca().equals(placa)) {
                Veiculo veiculo = vaga.getVeiculo();
                veiculo.registrarSaida(LocalDateTime.now());
                long tempoPermanencia = calcularTempoPermanencia(vaga.getHoraEntrada());
                double valorAPagar = calcularPagamento(tempoPermanencia);
                totalFaturamento += valorAPagar;
                vaga.liberar();
                System.out.println("Veículo " + placa + " saiu da vaga " + vaga.getNumero());
                System.out.println("Tempo de permanência: " + tempoPermanencia + " minutos");
                System.out.println("Valor a pagar: R$ " + valorAPagar);
                return;
            }
        }
        System.out.println("Saída não registrada.");
    }

    private long calcularTempoPermanencia(LocalDateTime horaEntrada) {
        return Duration.between(horaEntrada, LocalDateTime.now()).toMinutes();
    }

    private double calcularPagamento(long minutos) {
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

    // Método para encontrar uma vaga disponível
    public Vaga encontrarVagaDisponivel() {
        for (Vaga vaga : vagas) {
            if (!vaga.isOcupada()) {
                return vaga;
            }
        }
        return null; // Retorna null se não houver vagas disponíveis
    }
}
