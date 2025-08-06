package com.bookswap.util;

import com.bookswap.model.Usuario;

public class SessaoUsuario {

    private static Usuario usuarioLogado;

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuario) {
        SessaoUsuario.usuarioLogado = usuario;
    }

    public static void limparSessao() {
        usuarioLogado = null;
    }
}