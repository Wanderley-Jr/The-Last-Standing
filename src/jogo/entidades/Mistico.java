package jogo.entidades;

import java.util.Random;

public class Mistico extends Personagem {
    public int pontosInteligencia;
    public int pontosVelocidade;

    private static final Random aleatorio = new Random();

    public int geraPontosVida() {
        return 100;
    }

    public int geraPontosAtaque() {
        return aleatorio.nextInt(6) + 20;
    }

    public int geraPontosDefesa() {
        return aleatorio.nextInt(6) + 5;
    }

    public int geraPontosInteligencia() {
        return aleatorio.nextInt(9) + 2;
    }

    public int geraPontosVelocidade() {
        return aleatorio.nextInt(9) + 2;
    }
}
