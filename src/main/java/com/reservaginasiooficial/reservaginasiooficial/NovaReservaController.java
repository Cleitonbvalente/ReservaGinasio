package com.reservaginasiooficial.reservaginasiooficial;

import com.reservaginasiooficial.reservaginasiooficial.model.dao.DaoFactory;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.ReservaDAO;
import com.reservaginasiooficial.reservaginasiooficial.model.entities.Reserva;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class NovaReservaController {
    @FXML private ComboBox<String> cbEsporte;
    @FXML private DatePicker dpData;
    @FXML private ComboBox<String> cbHorario;

    private final ReservaDAO reservaDAO = DaoFactory.createReservaDAO();

    @FXML
    public void initialize() {
        cbEsporte.getItems().addAll("Futsal", "Vôlei", "Basquete", "Handebol", "Badminton", "Outros");

        dpData.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        cbHorario.getItems().addAll(
                "07:00-09:00", "09:00-11:00", "11:00-13:00",
                "13:00-15:00", "15:00-17:00", "17:00-19:00",
                "19:00-21:00", "21:00-23:00"
        );

        dpData.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                atualizarHorariosDisponiveis(newDate);
            }
        });
    }

    private void atualizarHorariosDisponiveis(LocalDate data) {
        try {
            List<String> horariosDisponiveis = reservaDAO.getHorariosDisponiveis(data);
            cbHorario.getItems().clear();
            cbHorario.getItems().addAll(horariosDisponiveis);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Não foi possível carregar os horários disponíveis");
        }
    }

    @FXML
    public void onReservarClicked() {
        if (validarCampos()) {
            Reserva reserva = new Reserva(
                    SessaoUsuario.getUsuarioId(),
                    cbEsporte.getValue(),
                    dpData.getValue(),
                    cbHorario.getValue()
            );

            try {
                reservaDAO.inserir(reserva);
                mostrarAlerta("Sucesso", "Reserva realizada com sucesso!");
                fecharJanela();
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao realizar reserva: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onCancelarClicked() {
        fecharJanela();
    }

    private boolean validarCampos() {
        if (cbEsporte.getValue() == null) {
            mostrarAlerta("Atenção", "Selecione um esporte");
            return false;
        }
        if (dpData.getValue() == null) {
            mostrarAlerta("Atenção", "Selecione uma data");
            return false;
        }
        if (cbHorario.getValue() == null) {
            mostrarAlerta("Atenção", "Selecione um horário");
            return false;
        }
        return true;
    }

    private void fecharJanela() {
        ((Stage) cbEsporte.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
