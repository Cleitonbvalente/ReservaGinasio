package com.reservaginasiooficial.reservaginasiooficial.model.entities;

import java.time.LocalDate;

public class Reserva {
    private Integer id;
    private Integer idUsuario;
    private String nomeUsuario;
    private String esporte;
    private LocalDate data;
    private String horario;

    public Reserva() {
    }

    public Reserva(Integer idUsuario, String esporte, LocalDate data, String horario) {
        this.idUsuario = idUsuario;
        this.esporte = esporte;
        this.data = data;
        this.horario = horario;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEsporte() {
        return esporte;
    }

    public void setEsporte(String esporte) {
        this.esporte = esporte;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", esporte='" + esporte + '\'' +
                ", data=" + data +
                ", horario='" + horario + '\'' +
                '}';
    }
}
