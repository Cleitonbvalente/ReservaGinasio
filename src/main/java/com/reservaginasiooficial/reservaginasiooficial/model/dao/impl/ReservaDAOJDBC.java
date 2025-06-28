package com.reservaginasiooficial.reservaginasiooficial.model.dao.impl;

import com.reservaginasiooficial.reservaginasiooficial.db.DB;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.ReservaDAO;
import com.reservaginasiooficial.reservaginasiooficial.model.entities.Reserva;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAOJDBC implements ReservaDAO {
    private final Connection conn;

    public ReservaDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserir(Reserva reserva) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO reservas (id_usuario, esporte, data, horario) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            st.setInt(1, reserva.getIdUsuario());
            st.setString(2, reserva.getEsporte());
            st.setDate(3, Date.valueOf(reserva.getData()));
            st.setString(4, reserva.getHorario());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    reserva.setId(rs.getInt(1));
                }
                DB.closeResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir reserva: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deletarPorId(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM reservas WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar reserva: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Reserva> buscarTodos() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT r.*, u.nome as nome_usuario FROM reservas r " +
                            "JOIN usuarios u ON r.id_usuario = u.id ORDER BY r.data, r.horario"
            );
            rs = st.executeQuery();

            List<Reserva> lista = new ArrayList<>();
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setId(rs.getInt("id"));
                reserva.setIdUsuario(rs.getInt("id_usuario"));
                reserva.setNomeUsuario(rs.getString("nome_usuario"));
                reserva.setEsporte(rs.getString("esporte"));
                reserva.setData(rs.getDate("data").toLocalDate());
                reserva.setHorario(rs.getString("horario"));
                lista.add(reserva);
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todas as reservas: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Reserva> buscarPorUsuario(Integer usuarioId) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM reservas WHERE id_usuario = ? ORDER BY data, horario"
            );
            st.setInt(1, usuarioId);
            rs = st.executeQuery();

            List<Reserva> lista = new ArrayList<>();
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setId(rs.getInt("id"));
                reserva.setIdUsuario(rs.getInt("id_usuario"));
                reserva.setEsporte(rs.getString("esporte"));
                reserva.setData(rs.getDate("data").toLocalDate());
                reserva.setHorario(rs.getString("horario"));
                lista.add(reserva);
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reservas do usuário: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Reserva> buscarPorData(LocalDate data) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT r.*, u.nome as nome_usuario FROM reservas r " +
                            "JOIN usuarios u ON r.id_usuario = u.id WHERE r.data = ?"
            );
            st.setDate(1, Date.valueOf(data));
            rs = st.executeQuery();

            List<Reserva> lista = new ArrayList<>();
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setId(rs.getInt("id"));
                reserva.setIdUsuario(rs.getInt("id_usuario"));
                reserva.setNomeUsuario(rs.getString("nome_usuario"));
                reserva.setEsporte(rs.getString("esporte"));
                reserva.setData(rs.getDate("data").toLocalDate());
                reserva.setHorario(rs.getString("horario"));
                lista.add(reserva);
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reservas por data: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Reserva> buscarComFiltros(Integer usuarioId, LocalDate data, String esporte) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            StringBuilder sql = new StringBuilder(
                    "SELECT * FROM reservas WHERE id_usuario = ?"
            );

            if (data != null) {
                sql.append(" AND data = ?");
            }
            if (esporte != null && !esporte.isEmpty()) {
                sql.append(" AND esporte = ?");
            }
            sql.append(" ORDER BY data, horario");

            st = conn.prepareStatement(sql.toString());
            st.setInt(1, usuarioId);

            int paramIndex = 2;
            if (data != null) {
                st.setDate(paramIndex++, Date.valueOf(data));
            }
            if (esporte != null && !esporte.isEmpty()) {
                st.setString(paramIndex, esporte);
            }

            rs = st.executeQuery();

            List<Reserva> lista = new ArrayList<>();
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setId(rs.getInt("id"));
                reserva.setIdUsuario(rs.getInt("id_usuario"));
                reserva.setEsporte(rs.getString("esporte"));
                reserva.setData(rs.getDate("data").toLocalDate());
                reserva.setHorario(rs.getString("horario"));
                lista.add(reserva);
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reservas com filtros: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<String> getHorariosDisponiveis(LocalDate data) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT horario FROM reservas WHERE data = ?"
            );
            st.setDate(1, Date.valueOf(data));
            rs = st.executeQuery();

            List<String> horariosOcupados = new ArrayList<>();
            while (rs.next()) {
                horariosOcupados.add(rs.getString("horario"));
            }

            String[] todosHorarios = {
                    "07:00-09:00", "09:00-11:00", "11:00-13:00",
                    "13:00-15:00", "15:00-17:00", "17:00-19:00",
                    "19:00-21:00", "21:00-23:00"
            };

            List<String> disponiveis = new ArrayList<>();
            for (String horario : todosHorarios) {
                if (!horariosOcupados.contains(horario)) {
                    disponiveis.add(horario);
                }
            }
            return disponiveis;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar horários: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
