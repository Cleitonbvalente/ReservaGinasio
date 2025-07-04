package com.reservaginasiooficial.reservaginasiooficial.model.dao;
import com.reservaginasiooficial.reservaginasiooficial.model.entities.Usuario;
import java.util.List;


public interface UsuarioDAO {
    void inserir(Usuario usuario);
    void atualizar(Usuario usuario);
    void deletarPorId(Integer id);
    Usuario buscarPorId(Integer id);
    Usuario autenticar(String email, String senha);
    List<Usuario> buscarTodos();
}
