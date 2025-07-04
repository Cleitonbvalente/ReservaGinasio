package com.reservaginasiooficial.reservaginasiooficial;

import com.reservaginasiooficial.reservaginasiooficial.model.dao.DaoFactory;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.ReservaDAO;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.UsuarioDAO;
import com.reservaginasiooficial.reservaginasiooficial.model.entities.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public class PerfilController {
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private ImageView imgFotoPerfil;
    @FXML private Button btnEditar;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;
    @FXML private Button btnAlterarFoto;
    @FXML private Button btnSair;
    @FXML private Button btnExcluirConta;
    @FXML private StackPane fotoContainer;

    private Usuario usuario;
    private File arquivoFotoSelecionada;
    private final UsuarioDAO usuarioDAO = DaoFactory.createUsuarioDAO();

    @FXML
    public void initialize() {
        carregarDadosUsuario();
        configurarEventos();
    }

    private void carregarDadosUsuario() {
        usuario = SessaoUsuario.getUsuario();
        if (usuario != null) {
            txtNome.setText(usuario.getNome());
            txtEmail.setText(usuario.getEmail());

            if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
                Image image = new Image(new ByteArrayInputStream(usuario.getFoto()));
                imgFotoPerfil.setImage(image);
            }
        }
    }

    private void configurarEventos() {
        btnEditar.setOnAction(e -> habilitarEdicao());
        btnSalvar.setOnAction(e -> salvarAlteracoes());
        btnCancelar.setOnAction(e -> cancelarEdicao());
        btnAlterarFoto.setOnAction(e -> selecionarFoto());
        btnSair.setOnAction(e -> sair());
        btnExcluirConta.setOnAction(e -> confirmarExclusaoConta());
    }

    private void habilitarEdicao() {
        txtNome.setEditable(true);
        txtSenha.setVisible(true);
        btnAlterarFoto.setVisible(true);
        btnEditar.setVisible(false);
        btnSalvar.setVisible(true);
        btnCancelar.setVisible(true);
    }

    private void salvarAlteracoes() {
        try {
            usuario.setNome(txtNome.getText());

            if (!txtSenha.getText().isEmpty()) {
                usuario.setSenha(txtSenha.getText());
            }

            if (arquivoFotoSelecionada != null) {
                byte[] fotoBytes = Files.readAllBytes(arquivoFotoSelecionada.toPath());
                usuario.setFoto(fotoBytes);
            }

            usuarioDAO.atualizar(usuario);
            SessaoUsuario.login(usuario);
            desabilitarEdicao();
            mostrarAlerta("Sucesso", "Perfil atualizado com sucesso!");

        } catch (Exception e) {
            mostrarAlerta("Erro", "Não foi possível atualizar o perfil: " + e.getMessage());
        }
    }

    private void cancelarEdicao() {
        carregarDadosUsuario();
        desabilitarEdicao();
    }

    private void desabilitarEdicao() {
        txtNome.setEditable(false);
        txtSenha.setVisible(false);
        txtSenha.clear();
        btnAlterarFoto.setVisible(false);
        btnEditar.setVisible(true);
        btnSalvar.setVisible(false);
        btnCancelar.setVisible(false);
        arquivoFotoSelecionada = null;
    }

    private void selecionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Foto");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );
        arquivoFotoSelecionada = fileChooser.showOpenDialog(btnAlterarFoto.getScene().getWindow());

        if (arquivoFotoSelecionada != null) {
            try {
                Image image = new Image(arquivoFotoSelecionada.toURI().toString());
                imgFotoPerfil.setImage(image);
            } catch (Exception e) {
                mostrarAlerta("Erro", "Não foi possível carregar a imagem selecionada");
            }
        }
    }

    private void confirmarExclusaoConta() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Tem certeza que deseja excluir sua conta?");
        confirmacao.setContentText("Esta ação é irreversível e todos os seus dados serão perdidos.");

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Confirmação Final");
            dialog.setHeaderText("Para confirmar a exclusão, digite sua senha:");
            dialog.setContentText("Senha:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(senha -> {
                if (validarSenha(senha)) {
                    excluirConta();
                } else {
                    mostrarAlerta("Erro", "Senha incorreta. A exclusão foi cancelada.");
                }
            });
        }
    }

    private boolean validarSenha(String senha) {
        return usuario != null && usuario.getSenha().equals(senha);
    }

    private void excluirConta() {
        try {
            ReservaDAO reservaDAO = DaoFactory.createReservaDAO();
            reservaDAO.deletarPorUsuario(SessaoUsuario.getUsuarioId());

            usuarioDAO.deletarPorId(SessaoUsuario.getUsuarioId());
            SessaoUsuario.logout();

            Stage stage = (Stage) btnExcluirConta.getScene().getWindow();
            stage.close();

            mostrarAlerta("Conta Excluída", "Sua conta foi excluída com sucesso.");
            HelloApplication.criarTela("hello-view.fxml");

        } catch (Exception e) {
            mostrarAlerta("Erro", "Não foi possível excluir a conta: " + e.getMessage());
        }
    }

    @FXML
    private void sair() {
        try {
            SessaoUsuario.logout();
            Stage stage = (Stage) btnSair.getScene().getWindow();
            stage.close();
            HelloApplication.criarTela("hello-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível abrir a tela inicial: " + e.getMessage());
        }
    }



    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
