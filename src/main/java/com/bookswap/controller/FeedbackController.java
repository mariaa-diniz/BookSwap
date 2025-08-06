package com.bookswap.controller;

import com.bookswap.dao.FeedbackDAO;
import com.bookswap.model.Feedback;
import com.bookswap.model.Proposta;
import com.bookswap.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;

public class FeedbackController {

    @FXML private Label infoTrocaLabel;
    @FXML private Slider notaSlider;
    @FXML private TextArea comentarioArea;
    @FXML private Label mensagemLabel;

    private Proposta propostaAvaliada;
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();

    public void iniciarDados(Proposta proposta) {
        this.propostaAvaliada = proposta;
        infoTrocaLabel.setText("Avaliando a troca do livro: \"" + proposta.getTituloLivroSolicitado() + "\"");
    }

    @FXML
    void handleEnviarFeedbackAction(ActionEvent event) {
        int nota = (int) notaSlider.getValue();
        String comentario = comentarioArea.getText();

        Feedback feedback = new Feedback();
        feedback.setIdTroca(propostaAvaliada.getIdTroca());
        feedback.setIdUsuarioAvaliador(SessaoUsuario.getUsuarioLogado().getId());
        feedback.setNota(nota);
        feedback.setComentario(comentario);

        try {
            feedbackDAO.inserir(feedback);
            mensagemLabel.setTextFill(Color.GREEN);
            mensagemLabel.setText("Avaliação enviada com sucesso!");
            Stage stage = (Stage) mensagemLabel.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            mensagemLabel.setTextFill(Color.RED);
            if (e.getErrorCode() == 1062) { // Erro de entrada duplicada
                mensagemLabel.setText("Você já avaliou esta troca.");
            } else {
                mensagemLabel.setText("Erro ao salvar avaliação.");
                e.printStackTrace();
            }
        }
    }
}