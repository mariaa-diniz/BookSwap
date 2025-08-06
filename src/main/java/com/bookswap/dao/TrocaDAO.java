package com.bookswap.dao;

import com.bookswap.model.Proposta;
import com.bookswap.model.Troca;
import com.bookswap.util.ConexaoMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrocaDAO {

    public void criarProposta(Troca troca) throws SQLException {
        String sql = "INSERT INTO trocas (id_livro_solicitado, id_usuario_solicitante, id_livro_ofertado, id_usuario_ofertado, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, troca.getIdLivroSolicitado());
            stmt.setInt(2, troca.getIdUsuarioSolicitante());
            stmt.setInt(3, troca.getIdLivroOfertado());
            stmt.setInt(4, troca.getIdUsuarioOfertado());
            stmt.setString(5, "PENDENTE");

            stmt.executeUpdate();
        }
    }

    public List<Proposta> listarPropostasRecebidas(int idUsuario) {
        String sql = "SELECT t.id, l_solicitado.titulo AS livro_solicitado, l_ofertado.titulo AS livro_ofertado, u_solicitante.nome AS nome_usuario, t.data_proposta, t.status " +
                "FROM trocas t " +
                "JOIN livros l_solicitado ON t.id_livro_solicitado = l_solicitado.id " +
                "JOIN livros l_ofertado ON t.id_livro_ofertado = l_ofertado.id " +
                "JOIN usuarios u_solicitante ON t.id_usuario_solicitante = u_solicitante.id " +
                "WHERE t.id_usuario_ofertado = ? AND t.status = 'PENDENTE'";

        return getPropostas(idUsuario, sql);
    }

    public List<Proposta> listarPropostasEnviadas(int idUsuario) {
        String sql = "SELECT t.id, l_solicitado.titulo AS livro_solicitado, l_ofertado.titulo AS livro_ofertado, u_ofertado.nome AS nome_usuario, t.data_proposta, t.status " +
                "FROM trocas t " +
                "JOIN livros l_solicitado ON t.id_livro_solicitado = l_solicitado.id " +
                "JOIN livros l_ofertado ON t.id_livro_ofertado = l_ofertado.id " +
                "JOIN usuarios u_ofertado ON t.id_usuario_ofertado = u_ofertado.id " +
                "WHERE t.id_usuario_solicitante = ? AND t.status = 'PENDENTE'";

        return getPropostas(idUsuario, sql);
    }

    private List<Proposta> getPropostas(int idUsuario, String sql) {
        List<Proposta> propostas = new ArrayList<>();
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Proposta proposta = new Proposta();
                proposta.setIdTroca(rs.getInt("id"));
                proposta.setTituloLivroSolicitado(rs.getString("livro_solicitado"));
                proposta.setTituloLivroOfertado(rs.getString("livro_ofertado"));
                proposta.setNomeOutroUsuario(rs.getString("nome_usuario"));
                proposta.setDataProposta(rs.getTimestamp("data_proposta").toLocalDateTime());
                proposta.setStatus(rs.getString("status"));
                propostas.add(proposta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return propostas;
    }

    public void atualizarStatus(int idTroca, String novoStatus) throws SQLException {
        String sql = "UPDATE trocas SET status = ? WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, idTroca);
            stmt.executeUpdate();
        }
    }

    public List<Proposta> listarTrocasConcluidas(int idUsuario) {
        String sql = "SELECT t.id, l_solicitado.titulo AS livro_solicitado, l_ofertado.titulo AS livro_ofertado, u_ofertado.nome AS nome_usuario, t.data_proposta, t.status " +
                "FROM trocas t " +
                "JOIN livros l_solicitado ON t.id_livro_solicitado = l_solicitado.id " +
                "JOIN livros l_ofertado ON t.id_livro_ofertado = l_ofertado.id " +
                "JOIN usuarios u_ofertado ON t.id_usuario_ofertado = u_ofertado.id " +
                "WHERE (t.id_usuario_solicitante = ? OR t.id_usuario_ofertado = ?) AND t.status = 'CONCLUIDA'";

        List<Proposta> propostas = new ArrayList<>();
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Proposta proposta = new Proposta();
                proposta.setIdTroca(rs.getInt("id"));
                proposta.setTituloLivroSolicitado(rs.getString("livro_solicitado"));
                proposta.setTituloLivroOfertado(rs.getString("livro_ofertado"));
                proposta.setNomeOutroUsuario(rs.getString("nome_usuario"));
                proposta.setDataProposta(rs.getTimestamp("data_proposta").toLocalDateTime());
                proposta.setStatus(rs.getString("status"));
                propostas.add(proposta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return propostas;
    }
}