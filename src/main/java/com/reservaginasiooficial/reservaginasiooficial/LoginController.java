package com.reservaginasiooficial.reservaginasiooficial;

import com.reservaginasiooficial.reservaginasiooficial.model.dao.DaoFactory;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.UsuarioDAO;
import com.reservaginasiooficial.reservaginasiooficial.model.entities.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LoginController {
    @FXML private TextField txtEmailLogin;
    @FXML private PasswordField txtSenhaLogin;
    @FXML private TextField txtNomeCadastro;
    @FXML private TextField txtEmailCadastro;
    @FXML private PasswordField txtSenhaCadastro;
    @FXML private PasswordField txtConfirmarSenha;
    @FXML private TabPane tabPane;
    @FXML private Button btnSelecionarFoto;
    @FXML private Label lblNomeArquivo;

    private File arquivoFoto;
    private final UsuarioDAO usuarioDAO = DaoFactory.createUsuarioDAO();

    @FXML
    public void initialize() {
        lblNomeArquivo.setVisible(false);
    }

    @FXML
    public void onSelecionarFotoClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Foto");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );
        arquivoFoto = fileChooser.showOpenDialog(btnSelecionarFoto.getScene().getWindow());

        if (arquivoFoto != null) {
            lblNomeArquivo.setText(arquivoFoto.getName());
            lblNomeArquivo.setVisible(true);
        }
    }

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

                // Adiciona foto se foi selecionada
                if (arquivoFoto != null) {
                    byte[] fotoBytes = Files.readAllBytes(arquivoFoto.toPath());
                    novoUsuario.setFoto(fotoBytes);
                }

                usuarioDAO.inserir(novoUsuario);

                // Autentica automaticamente
                Usuario usuario = usuarioDAO.autenticar(email, senha);
                SessaoUsuario.login(usuario);

                // Fecha a tela de login/cadastro
                fecharJanela();

                // Atualiza a tela principal
                HelloController controller = HelloController.getInstancia();
                if (controller != null) {
                    controller.atualizarInterfaceUsuario();
                }

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
