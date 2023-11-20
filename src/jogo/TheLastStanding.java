package jogo;

import jogo.dados.RepositorioGuerreiros;
import jogo.dados.RepositorioMisticos;
import jogo.entidades.Guerreiro;
import jogo.entidades.Mistico;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TheLastStanding {
    private static final Scanner teclado = new Scanner(System.in);

    // Database
    private static final Connection connection = inicializarDatabase("localhost", "3304", "root", "");
    private static final RepositorioGuerreiros repositorioGuerreiros = new RepositorioGuerreiros(connection);
    private static final RepositorioMisticos repositorioMisticos = new RepositorioMisticos(connection);

    // Inicializa a conexão, e cria a database para a execução do jogo.
    public static Connection inicializarDatabase(String host, String port, String user, String password) {
        // language=sql
        String sql;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/", user, password);

            Statement statement = connection.createStatement();

            sql = "CREATE DATABASE IF NOT EXISTS the_last_standing;";
            statement.execute(sql);

            sql = "USE the_last_standing;";
            statement.execute(sql);

            sql = """
                CREATE TABLE IF NOT EXISTS mistico(
                    id_mistico INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                    nome VARCHAR(255) NOT NULL,
                    vida INT NOT NULL,
                    ataque INT NOT NULL,
                    defesa INT NOT NULL,
                    inteligencia INT NOT NULL,
                    velocidade INT NOT NULL,
                    UNIQUE(nome)
                );
            """.trim();
            statement.execute(sql);

            sql = """
                CREATE TABLE IF NOT EXISTS guerreiro(
                    id_barbaro INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(255) NOT NULL,
                    vida INT NOT NULL,
                    ataque INT NOT NULL,
                    defesa INT NOT NULL,
                    forca INT NOT NULL,
                    recuperacao INT NOT NULL,
                    UNIQUE(nome)
                );
            """.trim();
            statement.execute(sql);

            return connection;
        }
        catch (SQLException | ClassNotFoundException exception) {
            System.err.println("ERRO: " + exception.getMessage());
            System.exit(-1);
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("""
                <===| The Last Standing |===>
                | Opções:                   |
                | 0- Sair                   |
                | 1- Registrar guerreiro    |
                | 2- Registrar místico      |
                | 3- Realizar batalha       |
                <===|======|====|=======|===>
                 """.trim());

        int opcao = -1;
        while (opcao != 0) {
            System.out.print("\nEscolha uma opção: ");
            opcao = teclado.nextInt();
            teclado.nextLine();

            switch (opcao) {
                case 1 -> criarGuerreiro();
                case 2 -> criarMistico();
                //case 3 -> criarItem();
                case 3 -> {
                    Batalha batalha = new Batalha(repositorioGuerreiros, repositorioMisticos);
                    batalha.iniciarBatalha();
                }
                case 0 -> System.out.println("Saindo do programa...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    public static void criarGuerreiro() {
        Guerreiro guerreiro = new Guerreiro();

        System.out.print("Escolha o nome de seu guerreiro: ");
        guerreiro.nome = teclado.nextLine();

        // Sortear atributos
        guerreiro.geraPontosVida();
        guerreiro.geraPontosAtaque();
        guerreiro.geraPontosDefesa();
        guerreiro.geraPontosForca();
        guerreiro.geraPontosRecuperacao();

        // Salvar guerreiro
        if (!repositorioGuerreiros.salvarGuerreiro(guerreiro)) {
            System.out.println("Já existe um guerreiro com esse nome!");
        } else {
            System.out.println("Guerreiro criado!");
        }
    }

    public static void criarMistico() {
        Mistico mistico = new Mistico();

        System.out.print("Escolha o nome de seu místico: ");
        mistico.nome = teclado.nextLine();

        // Sortear atributos
        mistico.geraPontosVida();
        mistico.geraPontosAtaque();
        mistico.geraPontosDefesa();
        mistico.geraPontosInteligencia();
        mistico.geraPontosVelocidade();

        // Salvar místico
        if (!repositorioMisticos.salvarMistico(mistico)) {
            System.out.println("Já existe um místico com esse nome!");
        } else {
            System.out.println("Místico criado!");
        }
    }
}