package com.reservaginasiooficial.reservaginasiooficial;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PerfilController {
    @FXML private Label lblNome;
    @FXML private Label lblEmail;

    @FXML
    public void initialize() {
        carregarDadosUsuario();
    }

    private void carregarDadosUsuario() {
        if (SessaoUsuario.isLogado()) {
            lblNome.setText(SessaoUsuario.getUsuario().getNome());
            lblEmail.setText(SessaoUsuario.getUsuario().getEmail());
        }
    }

    @FXML
    public void onSairClicked() {
        SessaoUsuario.logout();
        HelloController.getInstancia().atualizarInterfaceUsuario();
        fecharJanela();
    }

    @FXML
    public void onFecharClicked() {
        fecharJanela();
    }

    private void fecharJanela() {
        ((Stage) lblNome.getScene().getWindow()).close();
    }
}
