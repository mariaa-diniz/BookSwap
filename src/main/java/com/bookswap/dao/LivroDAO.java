package com.bookswap.dao;

import com.bookswap.model.Livro;
import com.bookswap.util.ConexaoMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public List<Livro> listarTodosDisponiveis() {
        String sql = "SELECT * FROM livros WHERE disponivel = TRUE";
        List<Livro> livros = new ArrayList<>();
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setIdUsuario(rs.getInt("id_usuario"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setGenero(rs.getString("genero"));
                livro.setEstadoConservacao(rs.getString("estado_conservacao"));
                livro.setFotoUrl(rs.getString("foto_url"));
                livro.setDisponivel(rs.getBoolean("disponivel"));
                livros.add(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livros;
    }

    public List<Livro> listarPorUsuarioId(int idUsuario) {
        String sql = "SELECT * FROM livros WHERE id_usuario = ? AND disponivel = TRUE";
        List<Livro> livros = new ArrayList<>();
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setIdUsuario(rs.getInt("id_usuario"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setGenero(rs.getString("genero"));
                livro.setEstadoConservacao(rs.getString("estado_conservacao"));
                livro.setFotoUrl(rs.getString("foto_url"));
                livro.setDisponivel(rs.getBoolean("disponivel"));
                livros.add(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livros;
    }

    public void inserir(Livro livro) throws SQLException {
        String sql = "INSERT INTO livros (id_usuario, titulo, autor, genero, estado_conservacao, foto_url, disponivel) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livro.getIdUsuario());
            stmt.setString(2, livro.getTitulo());
            stmt.setString(3, livro.getAutor());
            stmt.setString(4, livro.getGenero());
            stmt.setString(5, livro.getEstadoConservacao());
            stmt.setString(6, livro.getFotoUrl());
            stmt.setBoolean(7, true); // Todo livro novo é cadastrado como disponível
            stmt.executeUpdate();
        }
    }

    public void marcarComoIndisponivel(int idLivro) throws SQLException {
        String sql = "UPDATE livros SET disponivel = FALSE WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }
}