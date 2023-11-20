package jogo.dados;

import jogo.entidades.Guerreiro;

import java.sql.*;

/**
 * Classe destinada a gerenciar a tabela "Guerreiros" na database.
 */
public class RepositorioGuerreiros {
    private final Connection connection;

    public RepositorioGuerreiros(Connection connection) {
        this.connection = connection;
    }

    public Guerreiro consultarGuerreiro(String nome) {
        //language=sql
        String sql = "SELECT * FROM guerreiro WHERE nome=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nome);
            ResultSet set = statement.executeQuery();

            // Se não houver nenhum guerreiro, retornar null
            if (!set.next()) {
                return null;
            }

            // Caso contrário, retornar novo guerreiro
            Guerreiro guerreiro = new Guerreiro();

            guerreiro.nome = set.getString("nome");
            guerreiro.pontosVida = set.getInt("vida");
            guerreiro.pontosAtaque = set.getInt("ataque");
            guerreiro.pontosDefesa = set.getInt("defesa");
            guerreiro.pontosForca = set.getInt("forca");
            guerreiro.pontosRecuperacao = set.getInt("recuperacao");

            return guerreiro;
        }
        catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
            return null;
        }
    }

    public boolean salvarGuerreiro(Guerreiro guerreiro) {
        // Caso já exista um guerreiro com esse nome, não salvar o guerreiro
        if (consultarGuerreiro(guerreiro.nome) != null) {
            return false;
        }

        // language=sql
        String sql = "INSERT INTO guerreiro(nome, vida, ataque, defesa, forca, recuperacao) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, guerreiro.nome);
            statement.setInt(2, guerreiro.pontosVida);
            statement.setInt(3, guerreiro.pontosAtaque);
            statement.setInt(4, guerreiro.pontosDefesa);
            statement.setInt(5, guerreiro.pontosForca);
            statement.setInt(6, guerreiro.pontosRecuperacao);

            statement.execute();
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
            return false;
        }
    }
}
