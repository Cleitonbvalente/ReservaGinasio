package com.reservaginasiooficial.reservaginasiooficial;

import com.reservaginasiooficial.reservaginasiooficial.model.dao.DaoFactory;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.ReservaDAO;
import com.reservaginasiooficial.reservaginasiooficial.model.entities.Reserva;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class NovaReservaController {
    @FXML private ComboBox<String> cbEsporte;
    @FXML private DatePicker dpData;
    @FXML private ComboBox<String> cbHorario;

    private final ReservaDAO reservaDAO = DaoFactory.createReservaDAO();

    @FXML
    public void initialize() {
        configurarComboboxes();
        configurarDatePicker();
    }

    private void configurarComboboxes() {
        cbEsporte.setItems(FXCollections.observableArrayList(
                "Futsal", "Vôlei", "Basquete", "Handebol", "Badminton"
        ));

        cbHorario.setItems(FXCollections.observableArrayList(
                "07:00-09:00", "09:00-11:00", "11:00-13:00",
                "13:00-15:00", "15:00-17:00", "17:00-19:00",
                "19:00-21:00", "21:00-23:00"
        ));
    }

    private void configurarDatePicker() {
        dpData.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    @FXML
    public void onReservarClicked() {
        if (validarCampos()) {
            fazerReserva();
        }
    }

    @FXML
    public void onCancelarClicked() {
        fecharJanela();
    }

    private void fazerReserva() {
        Reserva reserva = new Reserva(
                // ID do usuário logado (substitua pelo valor real)
                1,
                cbEsporte.getValue(),
                dpData.getValue(),
                cbHorario.getValue()
        );

        try {
            reservaDAO.inserir(reserva);
            mostrarAlerta("Sucesso", "Reserva realizada com sucesso!");
            fecharJanela();
        } catch (Exception e) {
            mostrarAlerta("Erro", "Horário já reservado!");
        }
    }

    private boolean validarCampos() {
        // Implemente validações dos campos
        return true;
    }

    private void fecharJanela() {
        cbEsporte.getScene().getWindow().hide();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
