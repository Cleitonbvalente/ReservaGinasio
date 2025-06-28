package com.reservaginasiooficial.reservaginasiooficial;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import java.io.IOException;

public class HelloController {
    private static boolean usuarioLogado = false;
    private static String telaRedirecionamento = null;

    @FXML
    public void onVisualizarReservasClicked() {
        try {
            HelloApplication.criarTela("visualizar-reservas-view.fxml");
        } catch (IOException e) {
            mostrarErro("Erro ao visualizar reservas", e);
        }
    }

    @FXML
    public void onLoginClicked() {
        try {
            HelloApplication.criarTela("login-view.fxml");
        } catch (IOException e) {
            mostrarErro("Erro ao abrir login", e);
        }
    }

    @FXML
    public void onMinhasReservasClicked() {
        if (usuarioLogado) {
            try {
                HelloApplication.criarTela("minhas-reservas-view.fxml");
            } catch (IOException e) {
                mostrarErro("Erro ao abrir reservas", e);
            }
        } else {
            redirecionarParaLogin("minhas-reservas-view.fxml");
        }
    }

    @FXML
    public void onNovaReservaClicked() {
        if (usuarioLogado) {
            abrirNovaReserva();
        } else {
            redirecionarParaLogin("nova-reserva-view.fxml");
        }
    }

    private void abrirNovaReserva() {
        try {
            HelloApplication.criarTela("nova-reserva-view.fxml");
        } catch (IOException e) {
            mostrarErro("Erro ao criar reserva", e);
        }
    }

    private void redirecionarParaLogin(String telaDestino) {
        telaRedirecionamento = telaDestino;
        try {
            HelloApplication.criarTela("login-view.fxml");
        } catch (IOException e) {
            mostrarErro("Erro ao abrir login", e);
        }
    }

    public static void setUsuarioLogado(boolean status) {
        usuarioLogado = status;
        if (usuarioLogado && telaRedirecionamento != null) {
            try {
                HelloApplication.criarTela(telaRedirecionamento);
                telaRedirecionamento = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mostrarErro(String titulo, Exception e) {
        System.err.println(titulo + ": " + e.getMessage());
        e.printStackTrace();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText("Erro no sistema");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
