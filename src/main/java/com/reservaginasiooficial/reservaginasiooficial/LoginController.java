package com.reservaginasiooficial.reservaginasiooficial;

import com.reservaginasiooficial.reservaginasiooficial.model.dao.DaoFactory;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.UsuarioDAO;
import com.reservaginasiooficial.reservaginasiooficial.model.entities.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

        if (validarCamposLogin(email, senha)) {
            try {
                Usuario usuario = usuarioDAO.autenticar(email, senha);
                if (usuario != null) {
                    SessaoUsuario.login(usuario);
                    fecharJanela();

                    HelloController controller = HelloController.getInstancia();
                    if (controller != null) {
                        controller.atualizarInterfaceUsuario();
                    }
                } else {
                    mostrarAlerta("Erro", "E-mail ou senha incorretos!");
                }
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha na autenticação: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onCadastrarClicked() {
        String nome = txtNomeCadastro.getText();
        String email = txtEmailCadastro.getText();
        String senha = txtSenhaCadastro.getText();
        String confirmacao = txtConfirmarSenha.getText();

        if (validarCamposCadastro(nome, email, senha, confirmacao)) {
            try {
                Usuario novoUsuario = new Usuario(nome, email, senha);
                usuarioDAO.inserir(novoUsuario);
                mostrarAlerta("Sucesso", "Cadastro realizado com sucesso!");
                limparCamposCadastro();
                tabPane.getSelectionModel().select(0);
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha no cadastro: " + e.getMessage());
            }
        }
    }

    private boolean validarCamposLogin(String email, String senha) {
        if (email.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos!");
            return false;
        }
        return true;
    }

    private boolean validarCamposCadastro(String nome, String email, String senha, String confirmacao) {
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmacao.isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos!");
            return false;
        }

        if (!senha.equals(confirmacao)) {
            mostrarAlerta("Erro", "As senhas não coincidem!");
            return false;
        }
        return true;
    }

    private void limparCamposCadastro() {
        txtNomeCadastro.clear();
        txtEmailCadastro.clear();
        txtSenhaCadastro.clear();
        txtConfirmarSenha.clear();
    }

    private void fecharJanela() {
        ((Stage) txtEmailLogin.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
