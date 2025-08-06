package com.bookswap.dao;

import com.bookswap.model.Feedback;
import com.bookswap.util.ConexaoMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeedbackDAO {
    public void inserir(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO feedbacks (id_troca, id_usuario_avaliador, nota, comentario) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feedback.getIdTroca());
            stmt.setInt(2, feedback.getIdUsuarioAvaliador());
            stmt.setInt(3, feedback.getNota());
            stmt.setString(4, feedback.getComentario());
            stmt.executeUpdate();
        }
    }
}