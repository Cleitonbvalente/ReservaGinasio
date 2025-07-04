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

import java.time.LocalDate;

public class VisualizarReservasController {
    @FXML private TableView<Reserva> tableView;
    @FXML private TableColumn<Reserva, String> colResponsavel;
    @FXML private TableColumn<Reserva, String> colEsporte;
    @FXML private TableColumn<Reserva, LocalDate> colData;
    @FXML private TableColumn<Reserva, String> colHorario;
    @FXML private DatePicker datePicker;
    @FXML private TextField tfNome;
    @FXML private TextField tfEsporte;

    private final ReservaDAO reservaDAO = DaoFactory.createReservaDAO();
    private final ObservableList<Reserva> todasReservas = FXCollections.observableArrayList();
    private final ObservableList<Reserva> reservasFiltradas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarDados();
    }

    private void configurarColunas() {
        colResponsavel.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNomeUsuario()));

        colEsporte.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEsporte()));

        colData.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getData()));

        colHorario.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHorario()));

        String estiloColuna = "-fx-alignment: CENTER-LEFT; -fx-text-fill: black; -fx-font-size: 14px;";
        colResponsavel.setStyle(estiloColuna);
        colEsporte.setStyle(estiloColuna);
        colData.setStyle(estiloColuna);
        colHorario.setStyle(estiloColuna);

        colResponsavel.setPrefWidth(200);
        colEsporte.setPrefWidth(150);
        colData.setPrefWidth(120);
        colHorario.setPrefWidth(100);
    }

    private void carregarDados() {
        todasReservas.setAll(reservaDAO.buscarTodos());
        reservasFiltradas.setAll(todasReservas);
        tableView.setItems(reservasFiltradas);

        System.out.println("Total de reservas carregadas: " + todasReservas.size());
        todasReservas.forEach(r -> System.out.println(
                r.getNomeUsuario() + " | " + r.getEsporte() + " | " +
                        r.getData() + " | " + r.getHorario()));
    }

    @FXML
    public void onFiltrarClicked() {
        LocalDate data = datePicker.getValue();
        String nome = tfNome.getText().trim().toLowerCase();
        String esporte = tfEsporte.getText().trim().toLowerCase();

        reservasFiltradas.clear();

        for (Reserva reserva : todasReservas) {
            boolean atendeFiltros = true;

            if (data != null && !reserva.getData().equals(data)) {
                atendeFiltros = false;
            }

            if (!nome.isEmpty() && !reserva.getNomeUsuario().toLowerCase().contains(nome)) {
                atendeFiltros = false;
            }

            if (!esporte.isEmpty() && !reserva.getEsporte().toLowerCase().contains(esporte)) {
                atendeFiltros = false;
            }

            if (atendeFiltros) {
                reservasFiltradas.add(reserva);
            }
        }

        tableView.refresh();
    }

    @FXML
    public void onLimparFiltrosClicked() {
        datePicker.setValue(null);
        tfNome.clear();
        tfEsporte.clear();
        reservasFiltradas.setAll(todasReservas);
        tableView.refresh();
    }

    @FXML
    public void onFecharClicked() {
        ((Stage) tableView.getScene().getWindow()).close();
    }
}
