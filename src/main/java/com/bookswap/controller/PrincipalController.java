package com.bookswap.controller;

import com.bookswap.MainApp;
import com.bookswap.dao.LivroDAO;
import com.bookswap.model.Livro;
import com.bookswap.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {

    @FXML
    private TilePane livrosContainer;
    @FXML
    private Label nomeUsuarioLabel;
    @FXML
    private Label tituloPaginaLabel;

    private final LivroDAO livroDAO = new LivroDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (SessaoUsuario.getUsuarioLogado() != null) {
            nomeUsuarioLabel.setText(SessaoUsuario.getUsuarioLogado().getNome());
        }
        carregarLivros();
    }

    private void carregarLivros() {
        livrosContainer.getChildren().clear();
        List<Livro> livros = livroDAO.listarTodosDisponiveis();
        for (Livro livro : livros) {
            VBox cardBox = criarCardLivro(livro);
            livrosContainer.getChildren().add(cardBox);
        }
    }

    private VBox criarCardLivro(Livro livro) {
        VBox card = new VBox(10);
        card.setPrefWidth(150);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("card-livro");

        ImageView capaImageView = new ImageView();
        capaImageView.setFitWidth(120);
        capaImageView.setFitHeight(160);
        capaImageView.setPreserveRatio(false);

        String nomeArquivoImagem = livro.getFotoUrl();
        if (nomeArquivoImagem != null && !nomeArquivoImagem.isEmpty()) {
            try {
                String caminhoImagem = "/images/covers/" + nomeArquivoImagem;
                Image capa = new Image(getClass().getResourceAsStream(caminhoImagem));
                capaImageView.setImage(capa);
            } catch (Exception e) {
                System.err.println("Não foi possível carregar a imagem: " + nomeArquivoImagem);
            }
        }

        Label tituloLabel = new Label(livro.getTitulo());
        tituloLabel.getStyleClass().add("titulo-livro");
        tituloLabel.setWrapText(true);
        tituloLabel.setAlignment(Pos.CENTER);

        Label autorLabel = new Label(livro.getAutor());
        autorLabel.getStyleClass().add("autor-livro");
        autorLabel.setAlignment(Pos.CENTER);

        card.getChildren().addAll(capaImageView, tituloLabel, autorLabel);
        card.setOnMouseClicked(event -> handleCardClick(livro));
        return card;
    }

    private void handleCardClick(Livro livroSelecionado) {
        MainApp.showDetalhesLivroScreen(livroSelecionado);
    }

    @FXML
    void handleInicioAction(ActionEvent event) {
        tituloPaginaLabel.setText("Livros Disponíveis");
        carregarLivros();
    }

    @FXML
    void handleMinhasTrocasAction(ActionEvent event) {
        MainApp.showMinhasTrocasScreen();
    }

    @FXML
    void handleMeuPerfilAction(ActionEvent event) {
        MainApp.showMeuPerfilScreen();
    }

    @FXML
    void handleAdicionarLivroAction(ActionEvent event) {
        MainApp.showAdicionarLivroScreen();
    }

    @FXML
    void handleSairAction(ActionEvent event) {
        SessaoUsuario.limparSessao();
        MainApp.showLoginScreen();
    }
}