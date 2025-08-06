package com.bookswap.controller;

import com.bookswap.MainApp;
import com.bookswap.dao.TrocaDAO;
import com.bookswap.model.Proposta;
import com.bookswap.util.SessaoUsuario;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class MinhasTrocasController implements Initializable {

    // Aba "Recebidas"
    @FXML private TableView<Proposta> tabelaRecebidas;
    @FXML private TableColumn<Proposta, String> colRecebidasLivroDesejado;
    @FXML private TableColumn<Proposta, String> colRecebidasLivroOfertado;
    @FXML private TableColumn<Proposta, String> colRecebidasUsuario;
    @FXML private TableColumn<Proposta, LocalDateTime> colRecebidasData;
    @FXML private TableColumn<Proposta, Void> colRecebidasAcoes;

    // Aba "Enviadas"
    @FXML private TableView<Proposta> tabelaEnviadas;
    @FXML private TableColumn<Proposta, String> colEnviadasLivroDesejado;
    @FXML private TableColumn<Proposta, String> colEnviadasLivroOfertado;
    @FXML private TableColumn<Proposta, String> colEnviadasUsuario;
    @FXML private TableColumn<Proposta, LocalDateTime> colEnviadasData;
    @FXML private TableColumn<Proposta, String> colEnviadasStatus;

    // Histórico
    @FXML private TableView<Proposta> tabelaHistorico;
    @FXML private TableColumn<Proposta, String> colHistLivroTrocado1;
    @FXML private TableColumn<Proposta, String> colHistLivroTrocado2;
    @FXML private TableColumn<Proposta, String> colHistOutroUsuario;
    @FXML private TableColumn<Proposta, LocalDateTime> colHistData;
    @FXML private TableColumn<Proposta, String> colHistStatus;
    @FXML private TableColumn<Proposta, Void> colHistAcoes;

    private final TrocaDAO trocaDAO = new TrocaDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabelas();
        carregarDados();
    }

    private void configurarTabelas() {
        colRecebidasLivroDesejado.setCellValueFactory(new PropertyValueFactory<>("tituloLivroSolicitado"));
        colRecebidasLivroOfertado.setCellValueFactory(new PropertyValueFactory<>("tituloLivroOfertado"));
        colRecebidasUsuario.setCellValueFactory(new PropertyValueFactory<>("nomeOutroUsuario"));
        formatarColunaData(colRecebidasData);

        colRecebidasAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnAceitar = new Button("Aceitar");
            private final Button btnRecusar = new Button("Recusar");
            private final HBox pane = new HBox(10, btnAceitar, btnRecusar);
            {
                btnAceitar.setOnAction(event -> responderProposta(getProposta(), "ACEITA"));
                btnRecusar.setOnAction(event -> responderProposta(getProposta(), "RECUSADA"));
                pane.setAlignment(Pos.CENTER);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
            private Proposta getProposta() {
                return getTableView().getItems().get(getIndex());
            }
        });

        colEnviadasLivroDesejado.setCellValueFactory(new PropertyValueFactory<>("tituloLivroSolicitado"));
        colEnviadasLivroOfertado.setCellValueFactory(new PropertyValueFactory<>("tituloLivroOfertado"));
        colEnviadasUsuario.setCellValueFactory(new PropertyValueFactory<>("nomeOutroUsuario"));
        colEnviadasStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        formatarColunaData(colEnviadasData);

        colHistLivroTrocado1.setCellValueFactory(new PropertyValueFactory<>("tituloLivroSolicitado"));
        colHistLivroTrocado2.setCellValueFactory(new PropertyValueFactory<>("tituloLivroOfertado"));
        colHistOutroUsuario.setCellValueFactory(new PropertyValueFactory<>("nomeOutroUsuario"));
        colHistStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        formatarColunaData(colHistData);

        colHistAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnAvaliar = new Button("Avaliar");
            {
                btnAvaliar.setOnAction(event -> {
                    Proposta proposta = getTableView().getItems().get(getIndex());
                    MainApp.showFeedbackScreen(proposta);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnAvaliar);
            }
        });
    }

    private void carregarDados() {
        if (SessaoUsuario.getUsuarioLogado() == null) return;
        int idUsuarioLogado = SessaoUsuario.getUsuarioLogado().getId();

        tabelaRecebidas.setItems(FXCollections.observableArrayList(trocaDAO.listarPropostasRecebidas(idUsuarioLogado)));
        tabelaEnviadas.setItems(FXCollections.observableArrayList(trocaDAO.listarPropostasEnviadas(idUsuarioLogado)));
        tabelaHistorico.setItems(FXCollections.observableArrayList(trocaDAO.listarTrocasConcluidas(idUsuarioLogado)));
    }

    private void responderProposta(Proposta proposta, String status) {
        try {
            trocaDAO.atualizarStatus(proposta.getIdTroca(), status);
            carregarDados(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void formatarColunaData(TableColumn<Proposta, LocalDateTime> coluna) {
        coluna.setCellValueFactory(new PropertyValueFactory<>("dataProposta"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        coluna.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });
    }

    @FXML
    void handleVoltarAction(ActionEvent event) {
        MainApp.showMainScreen();
    }
}