package com.reservaginasiooficial.reservaginasiooficial;

import com.reservaginasiooficial.reservaginasiooficial.model.dao.DaoFactory;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.UsuarioDAO;
import com.reservaginasiooficial.reservaginasiooficial.model.entities.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML private TextField txtEmailLogin;
    @FXML private PasswordField txtSenhaLogin;
    @FXML private TextField txtNomeCadastro;
    @FXML private TextField txtEmailCadastro;
    @FXML private PasswordField txtSenhaCadastro;
    @FXML private PasswordField txtConfirmarSenha;
    @FXML private TabPane tabPane;

    private final UsuarioDAO usuarioDAO = DaoFactory.createUsuarioDAO();

    @FXML
    public void onLoginClicked() {
        String email = txtEmailLogin.getText();
        String senha = txtSenhaLogin.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos!");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(email, senha);
            if (usuario != null) {
                HelloController.setUsuarioLogado(true);
                fecharJanela();
            } else {
                mostrarAlerta("Erro", "E-mail ou senha incorretos!");
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha na autenticação: " + e.getMessage());
        }
    }

    @FXML
    public void onCadastrarClicked() {
        String nome = txtNomeCadastro.getText();
        String email = txtEmailCadastro.getText();
        String senha = txtSenhaCadastro.getText();
        String confirmacao = txtConfirmarSenha.getText();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos!");
            return;
        }

        if (!senha.equals(confirmacao)) {
            mostrarAlerta("Erro", "As senhas não coincidem!");
            return;
        }

        try {
            Usuario novoUsuario = new Usuario(nome, email, senha);
            usuarioDAO.inserir(novoUsuario);
            mostrarAlerta("Sucesso", "Cadastro realizado com sucesso!");
            limparCamposCadastro();
            tabPane.getSelectionModel().select(0); // Volta para login
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha no cadastro: " + e.getMessage());
        }
    }

    private void limparCamposCadastro() {
        txtNomeCadastro.clear();
        txtEmailCadastro.clear();
        txtSenhaCadastro.clear();
        txtConfirmarSenha.clear();
    }

    private void fecharJanela() {
        txtEmailLogin.getScene().getWindow().hide();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
