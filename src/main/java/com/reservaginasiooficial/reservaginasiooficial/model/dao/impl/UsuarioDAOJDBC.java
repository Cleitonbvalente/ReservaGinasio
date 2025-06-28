package com.reservaginasiooficial.reservaginasiooficial.model.dao.impl;
import com.reservaginasiooficial.reservaginasiooficial.db.DB;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.UsuarioDAO;
import com.reservaginasiooficial.reservaginasiooficial.model.entities.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDAOJDBC implements UsuarioDAO {
    private Connection conn;

    public UsuarioDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserir(Usuario usuario) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO usuarios (nome, email, senha, foto) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, usuario.getNome());
            st.setString(2, usuario.getEmail());
            st.setString(3, usuario.getSenha());
            st.setBytes(4, usuario.getFoto());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE usuarios SET nome = ?, email = ?, senha = ?, foto = ? WHERE id = ?");

            st.setString(1, usuario.getNome());
            st.setString(2, usuario.getEmail());
            st.setString(3, usuario.getSenha());
            st.setBytes(4, usuario.getFoto());
            st.setInt(5, usuario.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deletarPorId(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM usuarios WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?");
            st.setInt(1, id);

            rs = st.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setFoto(rs.getBytes("foto"));
                return usuario;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM usuarios WHERE email = ? AND senha = ?");

            st.setString(1, email);
            st.setString(2, senha);

            rs = st.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setFoto(rs.getBytes("foto"));
                return usuario;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro na autenticação: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Usuario> buscarTodos() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM usuarios");
            rs = st.executeQuery();

            List<Usuario> usuarios = new ArrayList<>();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setFoto(rs.getBytes("foto"));
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
