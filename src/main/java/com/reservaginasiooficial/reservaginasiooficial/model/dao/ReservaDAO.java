package com.reservaginasiooficial.reservaginasiooficial.model.dao;
import com.reservaginasiooficial.reservaginasiooficial.model.entities.Reserva;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public interface ReservaDAO {
    void inserir(Reserva reserva);
    void deletarPorId(Integer id);
    List<Reserva> buscarTodos();
    List<Reserva> buscarPorData(LocalDate data);
    List<Reserva> buscarPorUsuario(Integer usuarioId);
    List<Reserva> buscarComFiltros(Integer usuarioId, LocalDate data, String esporte);
    List<String> getHorariosDisponiveis(LocalDate data);
    void deletarPorUsuario(Integer usuarioId) throws SQLException;
}
