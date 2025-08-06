package com.bookswap.controller;

import com.bookswap.MainApp;
import com.bookswap.dao.UsuarioDAO;
import com.bookswap.model.Usuario;
import com.bookswap.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;
    @FXML private Label mensagemLabel;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    void handleLoginButtonAction(ActionEvent event) {
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            mensagemLabel.setText("Por favor, preencha todos os campos.");
            return;
        }

        Usuario usuarioDoBanco = usuarioDAO.buscarPorEmail(email);

        if (usuarioDoBanco != null && BCrypt.checkpw(senha, usuarioDoBanco.getSenha())) {
            SessaoUsuario.setUsuarioLogado(usuarioDoBanco);

            MainApp.showMainScreen();
        } else {
            mensagemLabel.setText("Email ou senha inválidos.");
        }
    }

    @FXML
    void handleCadastroLinkAction(ActionEvent event) {
        MainApp.showCadastroScreen();
    }
}