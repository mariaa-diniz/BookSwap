package com.bookswap.controller;

import com.bookswap.MainApp;
import com.bookswap.model.Livro;
import com.bookswap.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DetalhesLivroController {

    @FXML private ImageView capaImageView;
    @FXML private Label tituloLabel;
    @FXML private Label autorLabel;
    @FXML private Label generoLabel;
    @FXML private Label estadoLabel;
    @FXML private Button proporTrocaButton;

    private Livro livroAtual;

    public void iniciarDados(Livro livro) {
        this.livroAtual = livro;
        tituloLabel.setText(livro.getTitulo());
        autorLabel.setText("por " + livro.getAutor());
        generoLabel.setText("Gênero: " + livro.getGenero());
        estadoLabel.setText("Estado: " + livro.getEstadoConservacao());

        // Verif se o usuário logado é o dono do livro
        if (SessaoUsuario.getUsuarioLogado() != null && SessaoUsuario.getUsuarioLogado().getId() == livro.getIdUsuario()) {
            proporTrocaButton.setVisible(false);
            proporTrocaButton.setManaged(false);
        } else {
            proporTrocaButton.setVisible(true); // Livros visíveis p outros
            proporTrocaButton.setManaged(true);
        }

        String nomeArquivoImagem = livro.getFotoUrl();
        if (nomeArquivoImagem != null && !nomeArquivoImagem.isEmpty()) {
            try {
                String caminhoImagem = "/images/covers/" + nomeArquivoImagem;
                Image capa = new Image(getClass().getResourceAsStream(caminhoImagem));
                capaImageView.setImage(capa);
            } catch (Exception e) {
                System.err.println("Não foi possível carregar a imagem do detalhe: " + nomeArquivoImagem);
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleVoltarAction(ActionEvent event) {
        MainApp.showMainScreen();
    }

    @FXML
    void handleProporTrocaAction(ActionEvent event) {
        if (livroAtual != null) {
            MainApp.showPropostaTrocaScreen(livroAtual);
        }
    }
}