package jogo.dados;

import jogo.entidades.Mistico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositorioMisticos {
    private final Connection connection;

    public RepositorioMisticos(Connection connection) {
        this.connection = connection;
    }

    public Mistico consultarMistico(String nome) {
        //language=sql
        String sql = "SELECT * FROM mistico WHERE nome=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nome);
            ResultSet set = statement.executeQuery();

            // Se não houver nenhum mistico, retornar null
            if (!set.next()) {
                return null;
            }

            // Caso contrário, retornar novo mistico
            Mistico mistico = new Mistico();

            mistico.nome = set.getString("nome");
            mistico.pontosVida = set.getInt("vida");
            mistico.pontosAtaque = set.getInt("ataque");
            mistico.pontosDefesa = set.getInt("defesa");
            mistico.pontosInteligencia = set.getInt("inteligencia");
            mistico.pontosVelocidade = set.getInt("velocidade");

            return mistico;
        }
        catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
            return null;
        }
    }

    public boolean salvarMistico(Mistico mistico) {
        // Caso já exista um mistico com esse nome, não salvar o mistico
        if (consultarMistico(mistico.nome) != null) {
            return false;
        }

        // language=sql
        String sql = "INSERT INTO mistico(nome, vida, ataque, defesa, inteligencia, velocidade) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, mistico.nome);
            statement.setInt(2, mistico.pontosVida);
            statement.setInt(3, mistico.pontosAtaque);
            statement.setInt(4, mistico.pontosDefesa);
            statement.setInt(5, mistico.pontosInteligencia);
            statement.setInt(6, mistico.pontosVelocidade);
            statement.execute();

            return true;
        }
        catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
            return false;
        }
    }
}
