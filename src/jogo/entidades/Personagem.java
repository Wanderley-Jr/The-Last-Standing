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

    // Equipa um item no personagem, subtraindo o seu custo.
    public void equiparItem(Item item) {
        if (item != null) {
            pontosVida -= item.precoItem;
        }

        this.item = item;
    }
}
