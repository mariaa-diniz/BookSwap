package com.bookswap.controller;

import com.bookswap.MainApp;
import com.bookswap.dao.LivroDAO;
import com.bookswap.model.Livro;
import com.bookswap.util.SessaoUsuario;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;

public class AdicionarLivroController implements Initializable {

    @FXML private TextField tituloField;
    @FXML private TextField autorField;
    @FXML private TextField generoField;
    @FXML private ComboBox<String> estadoComboBox;
    @FXML private Label mensagemLabel;

    private final LivroDAO livroDAO = new LivroDAO();
    private File arquivoImagemSelecionada;
    private String nomeArquivoFinal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        estadoComboBox.setItems(FXCollections.observableArrayList(
                "NOVO", "SEMINOVO", "BOM", "COM_AVARIAS"
        ));
    }

    @FXML
    void handleSelecionarImagemAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione uma Imagem de Capa");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.png", "*.jpeg")
        );
        File arquivo = fileChooser.showOpenDialog(tituloField.getScene().getWindow());
        if (arquivo != null) {
            this.arquivoImagemSelecionada = arquivo;
            System.out.println(">>> IMAGEM: Arquivo selecionado: " + arquivo.getAbsolutePath());
            mensagemLabel.setTextFill(Color.BLACK);
            mensagemLabel.setText("Imagem selecionada: " + arquivo.getName());
        }
    }

    @FXML
    void handleAdicionarLivroAction(ActionEvent event) {
        nomeArquivoFinal = null;

        if (tituloField.getText().isEmpty() || autorField.getText().isEmpty() || estadoComboBox.getValue() == null) {
            mensagemLabel.setTextFill(Color.RED);
            mensagemLabel.setText("Título, Autor e Estado são obrigatórios.");
            return;
        }

        if (arquivoImagemSelecionada != null) {
            System.out.println(">>> IMAGEM: Processando arquivo " + arquivoImagemSelecionada.getName());
            try {
                URL resourceUrl = getClass().getResource("/images/covers");
                if (resourceUrl == null) {
                    throw new IOException("Pasta de resources '/images/covers' não encontrada. Verifique a estrutura de pastas.");
                }
                File pastaDestino = new File(resourceUrl.toURI());

                String extensao = arquivoImagemSelecionada.getName().substring(arquivoImagemSelecionada.getName().lastIndexOf("."));
                nomeArquivoFinal = UUID.randomUUID().toString() + extensao;

                Path caminhoDestino = new File(pastaDestino, nomeArquivoFinal).toPath();
                Path caminhoOrigem = arquivoImagemSelecionada.toPath();

                System.out.println(">>> IMAGEM: Copiando de '" + caminhoOrigem + "' para '" + caminhoDestino + "'");
                Files.copy(caminhoOrigem, caminhoDestino, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(">>> IMAGEM: Cópia bem-sucedida!");

            } catch (IOException | URISyntaxException e) {
                System.err.println(">>> ERRO CRÍTICO AO COPIAR IMAGEM!");
                e.printStackTrace();
                mensagemLabel.setText("Erro ao salvar a imagem. Verifique o console.");
                return;
            }
        }

        Livro novoLivro = new Livro();
        novoLivro.setTitulo(tituloField.getText());
        novoLivro.setAutor(autorField.getText());
        novoLivro.setGenero(generoField.getText());
        novoLivro.setEstadoConservacao(estadoComboBox.getValue());
        novoLivro.setIdUsuario(SessaoUsuario.getUsuarioLogado().getId());
        novoLivro.setFotoUrl(nomeArquivoFinal);

        try {
            livroDAO.inserir(novoLivro);
            mensagemLabel.setTextFill(Color.GREEN);
            mensagemLabel.setText("Livro adicionado com sucesso!");
            limparCampos();
        } catch (SQLException e) {
            mensagemLabel.setTextFill(Color.RED);
            mensagemLabel.setText("Erro ao salvar o livro no banco de dados.");
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        tituloField.clear();
        autorField.clear();
        generoField.clear();
        estadoComboBox.getSelectionModel().clearSelection();
        arquivoImagemSelecionada = null;
        nomeArquivoFinal = null;
    }

    @FXML
    void handleVoltarAction(ActionEvent event) {
        MainApp.showMainScreen();
    }
}