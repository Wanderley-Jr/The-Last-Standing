package jogo;

import jogo.dados.RepositorioGuerreiros;
import jogo.dados.RepositorioMisticos;
import jogo.entidades.Guerreiro;
import jogo.entidades.Item;
import jogo.entidades.Mistico;
import jogo.entidades.Personagem;

import java.util.Random;
import java.util.Scanner;

public class Batalha {
    private final Scanner teclado = new Scanner(System.in);
    private final Random aleatorio = new Random();

    private final RepositorioGuerreiros repositorioGuerreiros;
    private final RepositorioMisticos repositorioMisticos;

    public String vencedor;

    public Batalha(RepositorioGuerreiros repositorioGuerreiros, RepositorioMisticos repositorioMisticos) {
        this.repositorioGuerreiros = repositorioGuerreiros;
        this.repositorioMisticos = repositorioMisticos;
    }
    public void sortearPersonagem(Personagem personagem) {
        personagem.pontosVida = personagem.geraPontosVida();
        personagem.pontosAtaque = personagem.geraPontosAtaque();
        personagem.pontosDefesa = personagem.geraPontosDefesa();

        if (personagem instanceof Guerreiro guerreiro) {
            guerreiro.pontosForca = guerreiro.geraPontosForca();
            guerreiro.pontosRecuperacao = guerreiro.geraPontosRecuperacao();
        }

        if (personagem instanceof Mistico mistico) {
            mistico.pontosInteligencia = mistico.geraPontosInteligencia();
            mistico.pontosVelocidade = mistico.geraPontosVelocidade();
        }
    }

    public int aplicarDano(Personagem atacante, Personagem atacado) {
        int bonusAtq = 1;

        // Aplicar bônuses dos personagens
        if (atacante instanceof Guerreiro guerreiro) {
            bonusAtq += guerreiro.pontosForca / 10;
        }
        else if (atacante instanceof Mistico mistico) {
            bonusAtq += mistico.pontosInteligencia / 10;
        }

        // Aplicar bônus do item
        bonusAtq += atacante.item.bonusAtaque / 10;

        int dano = atacante.pontosAtaque * bonusAtq;

        int bonusDef = 1;

        // Aplicar bônuses dos personagens
        if (atacado instanceof Guerreiro guerreiro) {
            bonusDef += guerreiro.pontosRecuperacao / 10;
        }
        else if (atacado instanceof Mistico mistico) {
            bonusDef += mistico.pontosVelocidade / 10;
        }

        // Aplicar bônus do item
        bonusDef += atacado.pontosDefesa * bonusDef;

        dano -= bonusDef;

        atacado.pontosVida -= dano;
        return dano;
    }

    public void iniciarBatalha() {
        Guerreiro guerreiro = obterGuerreiro();
        Mistico mistico = obterMistico();

        if (mistico == null || guerreiro == null) return;

        // Sorteio dos atributos
        sortearPersonagem(guerreiro);
        sortearPersonagem(mistico);

        // Inicia os 3 rounds
        for (int round = 1; round <= 3; round++) {
            System.out.println();
            listarCabecario("Round  " + round, 24);
            System.out.println();

            listaGuerreiro(guerreiro);
            listaMistico(mistico);

            boolean pJogador = roll();

            for (int i = 0; i < 2; i++) {
                Personagem atacante = pJogador ? guerreiro : mistico;
                Personagem atacado = pJogador ? mistico : guerreiro;

                System.out.println(atacante.nome + " ataca!");

                atacante.item = obterItem('A');
                if (atacante.item != null) {
                    atacante.pontosVida -= atacante.item.precoItem;
                }


                atacado.item = obterItem('D');
                if (atacado.item != null) {
                    atacante.pontosVida -= atacante.item.precoItem;
                }

                // Aplicar dano
                int dano = aplicarDano(atacante, atacado);
                System.out.println(atacado.nome + " sofre " + dano + " pontos de dano!\n");

                // Se alguém já está com menos de 0 de vida, acabar partida
                if (guerreiro.pontosVida <= 0 || mistico.pontosVida <= 0) {
                    break;
                }

                // O atacado é o próximo a atacar
                pJogador = !pJogador;
            }

            String novoVencedor = (guerreiro.pontosVida > mistico.pontosVida) ? guerreiro.nome : mistico.nome;
            System.out.printf("%s ganhou o %d° round!\n", novoVencedor, round);

            if (round == 2 && vencedor.equals(novoVencedor) || round == 3) {
                System.out.println(novoVencedor + " ganhou a batalha!");
            }

            System.out.print("Aperte ENTER para continuar...");
            teclado.nextLine();

            // Se estamos no segundo round e uma única pessoa ganhou os 2 rounds anteriores,
            // então ela vence por maioria (2/3 dos rounds ganhos) e pode pular.
            if (round == 2 && vencedor.equals(novoVencedor)) {
                break;
            }

            vencedor = novoVencedor;
        }
    }

    public Item obterItem(char modalidade) {
        System.out.print("Informe o nome do item que deseja equipar (ou N para nenhum): ");
        String nome = teclado.nextLine();

        if (nome.equalsIgnoreCase("N")) {
            return null;
        }

        Item item = new Item();
        item.modalidade = modalidade;
        item.descricao = nome;
        item.precoItem = item.calcularPreco();
        if (modalidade == 'A') {
            item.bonusAtaque = item.geraBonusAtaque();
        }
        else if (modalidade == 'D') {
            item.bonusDefesa = item.geraBonusDefesa();
        }

        return item;
    }

    /**
     * Sorteia o próximo jogador
     * @return true se o próximo for o guerreiro, false se o próximo for o místico
     */
    public boolean roll() {
        return aleatorio.nextBoolean();
    }

    public void listarCabecario(String texto, int tamanho) {
        // Cada lado do cabeçário terá metade do tamanho, menos 6 para levar em
        // conta os caracteres decorativos, como por exemplo: <===| Ex |===>
        int raio = (tamanho-texto.length()-6)/2;

        System.out.print("<");
        for (int i = 0; i < raio; i++) {
            System.out.print("=");
        }
        System.out.print("| " + texto + " |");
        for (int i = 0; i < raio; i++) {
            System.out.print("=");
        }
        System.out.println(">");
    }

    public void listaGuerreiro(Guerreiro guerreiro) {
        String cabecario = "G: ";
        if (guerreiro.nome.length() % 2 == 0) {
            cabecario += " ";
        }
        cabecario += guerreiro.nome;
        listarCabecario(cabecario, 24);
        System.out.printf("""
                |       HP:  %3s       |
                | ATK: %3s    DEF: %3s |
                | STR: %3s    STA: %3s |
                <===|====|====|====|===>
                """.trim(),
                guerreiro.pontosVida,
                guerreiro.pontosAtaque, guerreiro.pontosDefesa,
                guerreiro.pontosForca, guerreiro.pontosRecuperacao
        );
        System.out.println("\n");
    }

    public Guerreiro obterGuerreiro() {
        Guerreiro guerreiro = null;
        while (guerreiro == null) {
            System.out.print("Escolha o guerreiro (digite 0 para sair): ");
            String nome = teclado.nextLine();
            if (nome.equals("0")) {
                break;
            }
            guerreiro = repositorioGuerreiros.consultarGuerreiro(nome);
            if (guerreiro == null) {
                System.out.printf("Não há nenhum guerreiro cujo nome é %s!\n", nome);
            }
        }
        return guerreiro;
    }

    public void listaMistico(Mistico mistico) {
        String cabecario = "M: ";
        if (mistico.nome.length() % 2 == 0) {
            cabecario += " ";
        }
        cabecario += mistico.nome;

        listarCabecario(cabecario, 24);
        System.out.printf("""
                |       HP:  %3s       |
                | ATK: %3s    DEF: %3s |
                | INT: %3s    SPE: %3s |
                <===|====|====|====|===>
                """.trim(),
                mistico.pontosVida,
                mistico.pontosAtaque, mistico.pontosDefesa,
                mistico.pontosInteligencia, mistico.pontosVelocidade
        );

        System.out.println("\n");
    }

    public Mistico obterMistico() {
        Mistico mistico = null;
        while (mistico == null) {
            System.out.print("Escolha o místico (digite 0 para sair): ");
            String nome = teclado.nextLine();
            if (nome.equals("0")) {
                break;
            }
            mistico = repositorioMisticos.consultarMistico(nome);

            if (mistico == null) {
                System.out.printf("Não há nenhum místico cujo nome é %s!\n", nome);
            }
        }
        return mistico;
    }
}