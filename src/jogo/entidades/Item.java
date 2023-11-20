package jogo.entidades;

import java.util.Random;
import java.util.Scanner;

public class Item {
    public String descricao;
    public char tipoPersonagem;
    public char modalidade;
    public int bonusAtaque;
    public int bonusDefesa;
    public int precoItem;

    private static final Random aleatorio = new Random();

    public int geraBonusAtaque() {
        return aleatorio.nextInt(9) + 2;
    }

    public int geraBonusDefesa() {
        return aleatorio.nextInt(9) + 2;
    }

    public int calcularPreco() {
        return 10;
    }
}
