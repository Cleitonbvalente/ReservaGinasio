package com.reservaginasiooficial.reservaginasiooficial;

import com.reservaginasiooficial.reservaginasiooficial.model.dao.DaoFactory;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.ReservaDAO;
import com.reservaginasiooficial.reservaginasiooficial.model.entities.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class VisualizarReservasController {

    // Componentes da tabela
    @FXML private TableView<Reserva> tableView;
    @FXML private TableColumn<Reserva, String> colResponsavel;
    @FXML private TableColumn<Reserva, String> colEsporte;
    @FXML private TableColumn<Reserva, LocalDate> colData;
    @FXML private TableColumn<Reserva, String> colHorario;

    // Componentes de filtro
    @FXML private DatePicker datePicker;
    @FXML private TextField tfNome;
    @FXML private TextField tfEsporte;

    private final ReservaDAO reservaDAO = DaoFactory.createReservaDAO();
    private final ObservableList<Reserva> reservas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarDados();
    }

    private void configurarColunas() {
        // Configuração das colunas
        colResponsavel.setCellValueFactory(new PropertyValueFactory<>("nomeUsuario"));
        colEsporte.setCellValueFactory(new PropertyValueFactory<>("esporte"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colHorario.setCellValueFactory(new PropertyValueFactory<>("horario"));
    }

    private void carregarDados() {
        reservas.setAll(reservaDAO.buscarTodos());
        tableView.setItems(reservas);
    }

    @FXML
    public void onFiltrarClicked() {
        LocalDate data = datePicker.getValue();
        String nome = tfNome.getText().trim();
        String esporte = tfEsporte.getText().trim();

        if (data != null) {
            reservas.setAll(reservaDAO.buscarPorData(data));
        } else {
            reservas.setAll(reservaDAO.buscarTodos());
        }

        // Aplicar filtros adicionais
        if (!nome.isEmpty()) {
            reservas.removeIf(r -> !r.getNomeUsuario().toLowerCase().contains(nome.toLowerCase()));
        }
        if (!esporte.isEmpty()) {
            reservas.removeIf(r -> !r.getEsporte().equalsIgnoreCase(esporte));
        }
    }
}
