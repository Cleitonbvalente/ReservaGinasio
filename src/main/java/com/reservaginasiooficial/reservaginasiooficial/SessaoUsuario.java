package com.reservaginasiooficial.reservaginasiooficial;

import com.reservaginasiooficial.reservaginasiooficial.model.entities.Usuario;

public class SessaoUsuario {
    private static Usuario usuarioLogado;

    public static void login(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static void logout() {
        usuarioLogado = null;
    }

    public static boolean isLogado() {
        return usuarioLogado != null;
    }

    public static Usuario getUsuario() {
        return usuarioLogado;
    }

    public static Integer getUsuarioId() {
        return usuarioLogado != null ? usuarioLogado.getId() : null;
    }
}
