package com.reservaginasiooficial.reservaginasiooficial;

import com.reservaginasiooficial.reservaginasiooficial.model.dao.DaoFactory;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.ReservaDAO;
import com.reservaginasiooficial.reservaginasiooficial.model.entities.Reserva;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;

import java.io.IOException;
import java.time.LocalDate;

public class MinhasReservasController {
    @FXML private TableView<Reserva> tableView;
    @FXML private TableColumn<Reserva, String> colEsporte;
    @FXML private TableColumn<Reserva, LocalDate> colData;
    @FXML private TableColumn<Reserva, String> colHorario;
    @FXML private TableColumn<Reserva, Void> colAcoes;
    @FXML private DatePicker dpFiltroData;
    @FXML private ComboBox<String> cbFiltroEsporte;

    private final ReservaDAO reservaDAO = DaoFactory.createReservaDAO();
    private final ObservableList<Reserva> reservas = FXCollections.observableArrayList();
    private final int usuarioLogadoId = SessaoUsuario.getUsuarioId();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarEsportes();
        carregarReservas();
    }

    private void configurarColunas() {
        colEsporte.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEsporte()));

        colData.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getData()));

        colHorario.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHorario()));

        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnCancelar = new Button("Cancelar");

            {
                btnCancelar.setOnAction(event -> {
                    Reserva reserva = getTableView().getItems().get(getIndex());
                    cancelarReserva(reserva);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnCancelar);
                }
            }
        });
    }

    private void carregarEsportes() {
        cbFiltroEsporte.getItems().addAll(
                "Todos", "Futsal", "Vôlei", "Basquete", "Handebol", "Badminton"
        );
        cbFiltroEsporte.getSelectionModel().selectFirst();
    }

    private void carregarReservas() {
        reservas.setAll(reservaDAO.buscarPorUsuario(usuarioLogadoId));
        tableView.setItems(reservas);
    }

    @FXML
    public void onFiltrarClicked() {
        LocalDate data = dpFiltroData.getValue();
        String esporte = cbFiltroEsporte.getValue();

        if (data != null || !"Todos".equals(esporte)) {
            reservas.setAll(reservaDAO.buscarComFiltros(usuarioLogadoId, data,
                    "Todos".equals(esporte) ? null : esporte));
        } else {
            carregarReservas();
        }
    }

    @FXML
    public void onLimparFiltrosClicked() {
        dpFiltroData.setValue(null);
        cbFiltroEsporte.getSelectionModel().selectFirst();
        carregarReservas();
    }

    @FXML
    public void onVoltarClicked() {
        ((Stage) tableView.getScene().getWindow()).close();
    }

    @FXML
    public void onNovaReservaClicked() {
        try {
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("nova-reserva-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage novaReservaStage = new Stage();
            novaReservaStage.setTitle("Nova Reserva");
            novaReservaStage.setScene(scene);

            novaReservaStage.initModality(Modality.APPLICATION_MODAL);

            novaReservaStage.showAndWait();

            carregarReservas();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível abrir a tela de nova reserva");
        }
    }

    private void cancelarReserva(Reserva reserva) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar cancelamento");
        confirmacao.setHeaderText("Deseja cancelar esta reserva?");
        confirmacao.setContentText(reserva.getEsporte() + " - " + reserva.getData() + " " + reserva.getHorario());

        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                reservaDAO.deletarPorId(reserva.getId());
                carregarReservas();
                mostrarAlerta("Sucesso", "Reserva cancelada com sucesso!");
            }
        });
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
