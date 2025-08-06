package com.bookswap;

import com.bookswap.controller.DetalhesLivroController;
import com.bookswap.controller.FeedbackController;
import com.bookswap.controller.PropostaTrocaController;
import com.bookswap.model.Livro;
import com.bookswap.model.Proposta;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("BookSwap");
        showLoginScreen();
    }

    public static void showLoginScreen() {
        try {
            Parent root = loadFXML("TelaLogin");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Falha ao carregar a tela de login.");
            e.printStackTrace();
        }
    }

    public static void showCadastroScreen() {
        try {
            Parent root = loadFXML("TelaCadastro");
            Stage stage = new Stage();
            stage.setTitle("Criar Nova Conta");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.err.println("Falha ao carregar a tela de cadastro.");
            e.printStackTrace();
        }
    }

    public static void showMainScreen() {
        try {
            Parent root = loadFXML("TelaPrincipal");
            Scene scene = new Scene(root, 1000, 600);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Falha ao carregar a tela principal.");
            e.printStackTrace();
        }
    }

    public static void showDetalhesLivroScreen(Livro livro) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/bookswap/view/TelaDetalhesLivro.fxml"));
            Parent root = loader.load();
            DetalhesLivroController controller = loader.getController();
            controller.iniciarDados(livro);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Falha ao carregar a tela de detalhes do livro.");
            e.printStackTrace();
        }
    }

    public static void showPropostaTrocaScreen(Livro livroSolicitado) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/bookswap/view/TelaPropostaTroca.fxml"));
            Parent root = loader.load();
            PropostaTrocaController controller = loader.getController();
            controller.iniciarDados(livroSolicitado);
            Stage stage = new Stage();
            stage.setTitle("Fazer Proposta de Troca");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Falha ao carregar a tela de proposta de troca.");
            e.printStackTrace();
        }
    }

    public static void showMinhasTrocasScreen() {
        try {
            Parent root = loadFXML("TelaMinhasTrocas");
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Falha ao carregar a tela de Minhas Trocas.");
            e.printStackTrace();
        }
    }

    public static void showMeuPerfilScreen() {
        try {
            Parent root = loadFXML("TelaPerfil");
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Falha ao carregar a tela de Perfil.");
            e.printStackTrace();
        }
    }

    public static void showAdicionarLivroScreen() {
        try {
            Parent root = loadFXML("TelaAdicionarLivro");
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Falha ao carregar a tela de Adicionar Livro.");
            e.printStackTrace();
        }
    }

    public static void showFeedbackScreen(Proposta proposta) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/bookswap/view/TelaFeedback.fxml"));
            Parent root = loader.load();
            FeedbackController controller = loader.getController();
            controller.iniciarDados(proposta);
            Stage stage = new Stage();
            stage.setTitle("Avaliar Troca");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Falha ao carregar a tela de Feedback.");
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxmlFileName) throws IOException {
        String pathToFxml = "/com/bookswap/view/" + fxmlFileName + ".fxml";
        URL fxmlUrl = MainApp.class.getResource(pathToFxml);
        if (fxmlUrl == null) {
            throw new IOException("Não foi possível encontrar o arquivo FXML: " + pathToFxml);
        }
        return FXMLLoader.load(fxmlUrl);
    }

    public static void main(String[] args) {
        launch(args);
    }
}