package com.reservaginasiooficial.reservaginasiooficial;

import com.reservaginasiooficial.reservaginasiooficial.model.entities.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;

public class PerfilController {
    @FXML private Label lblNome;
    @FXML private Label lblEmail;
    @FXML private ImageView imgFotoPerfil;
    @FXML private StackPane fotoContainer;

    // Constantes para o tamanho 3x4 (em pixels)
    private static final double LARGURA_FOTO = 150;
    private static final double ALTURA_FOTO = 200;

    @FXML
    public void initialize() {
        configurarContainerFoto();
        carregarDadosUsuario();
    }

    private void configurarContainerFoto() {
        // Configura o container para manter proporção 3x4
        fotoContainer.setMinSize(LARGURA_FOTO, ALTURA_FOTO);
        fotoContainer.setMaxSize(LARGURA_FOTO, ALTURA_FOTO);

        // Configura a ImageView
        imgFotoPerfil.setFitWidth(LARGURA_FOTO);
        imgFotoPerfil.setFitHeight(ALTURA_FOTO);
        imgFotoPerfil.setPreserveRatio(false); // Força preencher o espaço

        // Adiciona borda arredondada
        Rectangle clip = new Rectangle(LARGURA_FOTO, ALTURA_FOTO);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        imgFotoPerfil.setClip(clip);
    }

    private void carregarDadosUsuario() {
        if (SessaoUsuario.isLogado()) {
            Usuario usuario = SessaoUsuario.getUsuario();
            lblNome.setText(usuario.getNome());
            lblEmail.setText(usuario.getEmail());

            if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
                try {
                    Image image = new Image(
                            new ByteArrayInputStream(usuario.getFoto()),
                            LARGURA_FOTO, ALTURA_FOTO, false, true);

                    imgFotoPerfil.setImage(image);
                    imgFotoPerfil.setVisible(true);
                } catch (Exception e) {
                    System.err.println("Erro ao carregar foto: " + e.getMessage());
                    imgFotoPerfil.setVisible(false);
                }
            } else {
                imgFotoPerfil.setVisible(false);
            }
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
