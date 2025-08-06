package com.bookswap.controller;

import com.bookswap.MainApp;
import com.bookswap.dao.LivroDAO;
import com.bookswap.model.Livro;
import com.bookswap.model.Usuario;
import com.bookswap.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PerfilController implements Initializable {

    @FXML private Label nomeUsuarioLabel;
    @FXML private Label emailUsuarioLabel;
    @FXML private TilePane meusLivrosContainer;

    private final LivroDAO livroDAO = new LivroDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarDadosUsuario();
        carregarMeusLivros();
    }

    private void carregarDadosUsuario() {
        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();
        if (usuarioLogado != null) {
            nomeUsuarioLabel.setText(usuarioLogado.getNome());
            emailUsuarioLabel.setText(usuarioLogado.getEmail());
        }
    }

    private void carregarMeusLivros() {
        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();
        if (usuarioLogado != null) {
            meusLivrosContainer.getChildren().clear();
            List<Livro> meusLivros = livroDAO.listarPorUsuarioId(usuarioLogado.getId());
            for (Livro livro : meusLivros) {
                VBox cardBox = criarCardLivro(livro);
                meusLivrosContainer.getChildren().add(cardBox);
            }
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
        capaImageView.setPreserveRatio(true); // Ajusta a imagem sem distorcer
        capaImageView.setSmooth(true);

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

        Button btnRemover = new Button("Remover");
        btnRemover.setOnAction(event -> handleRemoverLivro(livro));
        btnRemover.getStyleClass().add("remover-button");

        card.getChildren().addAll(capaImageView, tituloLabel, autorLabel, btnRemover);

        return card;
    }

    private void handleRemoverLivro(Livro livro) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar Remoção");
        alerta.setHeaderText("Você tem certeza que deseja remover o livro '" + livro.getTitulo() + "' da sua estante?");
        alerta.setContentText("O livro ficará indisponível para novas trocas.");

        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                livroDAO.marcarComoIndisponivel(livro.getId());
                carregarMeusLivros();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert erroAlert = new Alert(Alert.AlertType.ERROR);
                erroAlert.setTitle("Erro no Banco de Dados");
                erroAlert.setHeaderText("Não foi possível remover o livro.");
                erroAlert.showAndWait();
            }
        }
    }

    @FXML
    void handleVoltarAction(ActionEvent event) {
        MainApp.showMainScreen();
    }
}