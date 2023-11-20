package jogo.entidades;

public abstract class Personagem {
    public String nome;
    public int pontosVida;
    public int pontosAtaque;
    public int pontosDefesa;
    public Item item;

    public abstract int geraPontosVida();
    public abstract int geraPontosAtaque();
    public abstract int geraPontosDefesa();
}
