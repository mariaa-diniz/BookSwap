package com.bookswap.controller;

import com.bookswap.dao.LivroDAO;
import com.bookswap.dao.TrocaDAO;
import com.bookswap.model.Livro;
import com.bookswap.model.Troca;
import com.bookswap.model.Usuario;
import com.bookswap.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.List;

public class PropostaTrocaController {

    @FXML private Label infoLivroLabel;
    @FXML private ComboBox<Livro> meusLivrosComboBox;
    @FXML private Label mensagemLabel;

    private Livro livroSolicitado;
    private final LivroDAO livroDAO = new LivroDAO();
    private final TrocaDAO trocaDAO = new TrocaDAO();

    public void iniciarDados(Livro livro) {
        this.livroSolicitado = livro;
        infoLivroLabel.setText("Você está propondo uma troca pelo livro: \"" + livro.getTitulo() + "\"");
        carregarMeusLivros();
    }

    private void carregarMeusLivros() {
        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();
        if (usuarioLogado != null) {
            List<Livro> meusLivros = livroDAO.listarPorUsuarioId(usuarioLogado.getId());
            meusLivrosComboBox.getItems().setAll(meusLivros);

            meusLivrosComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Livro livro) {
                    return livro == null ? "" : livro.getTitulo();
                }
                @Override
                public Livro fromString(String string) {
                    return null;
                }
            });
        }
    }

    @FXML
    void handleConfirmarPropostaAction(ActionEvent event) {
        Livro livroOfertado = meusLivrosComboBox.getSelectionModel().getSelectedItem();
        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();

        if (livroOfertado == null) {
            mensagemLabel.setTextFill(Color.RED);
            mensagemLabel.setText("Por favor, selecione um livro para ofertar.");
            return;
        }

        Troca novaTroca = new Troca();
        novaTroca.setIdLivroSolicitado(livroSolicitado.getId());
        novaTroca.setIdUsuarioSolicitante(usuarioLogado.getId());
        novaTroca.setIdLivroOfertado(livroOfertado.getId());
        novaTroca.setIdUsuarioOfertado(livroSolicitado.getIdUsuario());

        try {
            trocaDAO.criarProposta(novaTroca);
            mensagemLabel.setTextFill(Color.GREEN);
            mensagemLabel.setText("Proposta enviada com sucesso!");
        } catch (SQLException e) {
            mensagemLabel.setTextFill(Color.RED);
            mensagemLabel.setText("Erro ao enviar proposta.");
            e.printStackTrace();
        }
    }
}