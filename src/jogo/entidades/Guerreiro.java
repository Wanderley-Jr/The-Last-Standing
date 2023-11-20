package jogo.entidades;

import java.util.Random;

public class Guerreiro extends Personagem {
    public int pontosForca;
    public int pontosRecuperacao;

    private static final Random aleatorio = new Random();

    @Override
    public int geraPontosVida() {
        return 100;
    }

    @Override
    public int geraPontosAtaque() {
        return aleatorio.nextInt(6) + 15;
    }

    @Override
    public int geraPontosDefesa() {
        return aleatorio.nextInt(6) + 10;
    }

    public int geraPontosForca() {
        return aleatorio.nextInt(9) + 2;
    }

    public int geraPontosRecuperacao() {
        return aleatorio.nextInt(9) + 2;
    }
}
