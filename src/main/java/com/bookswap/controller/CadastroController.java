package com.bookswap.controller;

import com.bookswap.dao.UsuarioDAO;
import com.bookswap.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.SQLException;

public class CadastroController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField senhaField;
    @FXML
    private Label mensagemLabel;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    void handleCadastroButtonAction(ActionEvent event) {
        System.out.println(">>> 1. Botão de cadastro foi clicado!");

        String nome = nomeField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            mensagemLabel.setTextFill(Color.RED);
            mensagemLabel.setText("Todos os campos são obrigatórios.");
            return;
        }

        Usuario novoUsuario = new Usuario(nome, email, senha);

        System.out.println(">>> 2. Preparando para chamar o DAO e cadastrar no banco...");

        try {
            usuarioDAO.cadastrar(novoUsuario);

            System.out.println(">>> 3. DAO executou com sucesso!");

            mensagemLabel.setTextFill(Color.GREEN);
            mensagemLabel.setText("Usuário cadastrado com sucesso!");

        } catch (Exception e) {
            System.err.println(">>> 4. Ocorreu um erro na camada do DAO ou de conexão!");

            mensagemLabel.setTextFill(Color.RED);

            // Verif se o erro é de email duplicado
            if (e instanceof SQLException && ((SQLException) e).getErrorCode() == 1062) {
                mensagemLabel.setText("Este email já está em uso.");
            } else {
                mensagemLabel.setText("Erro de banco de dados. Verifique o console.");
                e.printStackTrace();
            }   
        }
    }
}