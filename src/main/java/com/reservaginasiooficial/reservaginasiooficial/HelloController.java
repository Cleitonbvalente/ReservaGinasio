package com.reservaginasiooficial.reservaginasiooficial;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    private static HelloController instancia;

    @FXML private VBox vbOpcoesUsuario;
    @FXML private Label lblMensagemLogin;
    @FXML private Button btnVisualizarReservas;
    @FXML private Button btnNovaReserva;
    @FXML private Button btnMinhasReservas;
    @FXML private Button btnAreaUsuario;
    @FXML private Button btnPerfil;

    public static HelloController getInstancia() {
        return instancia;
    }

    @FXML
    public void initialize() {
        instancia = this;
        btnPerfil.setVisible(false);
        atualizarInterfaceUsuario();
    }

    public void atualizarInterfaceUsuario() {
        boolean logado = SessaoUsuario.isLogado();

        btnVisualizarReservas.setVisible(true);
        btnNovaReserva.setVisible(logado);
        btnMinhasReservas.setVisible(logado);
        btnAreaUsuario.setVisible(!logado);
        btnPerfil.setVisible(logado);

        lblMensagemLogin.setVisible(!logado);
        lblMensagemLogin.setText("Para Fazer Reserva ou Gerenciar suas reservas,\nfaça login ou cadastre-se");
    }

    @FXML
    public void onVisualizarReservasClicked() {
        abrirTela("visualizar-reservas-view.fxml", "Visualizar Reservas");
    }

    @FXML
    public void onNovaReservaClicked() {
        if (SessaoUsuario.isLogado()) {
            abrirTela("nova-reserva-view.fxml", "Nova Reserva");
        } else {
            abrirTela("login-view.fxml", "Área do Usuário");
        }
    }

    @FXML
    public void onMinhasReservasClicked() {
        if (SessaoUsuario.isLogado()) {
            abrirTela("minhas-reservas-view.fxml", "Minhas Reservas");
        } else {
            abrirTela("login-view.fxml", "Área do Usuário");
        }
    }

    @FXML
    public void onLoginClicked() {
        abrirTela("login-view.fxml", "Área do Usuário");
    }

    @FXML
    public void onPerfilClicked() {
        abrirTela("perfil-view.fxml", "Meu Perfil");
    }

    private void abrirTela(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            atualizarInterfaceUsuario();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
