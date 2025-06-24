package Telas;

import java.io.Serializable;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int quantia;
    private String nome;
    private float imposto;
    private double preco;

    // Construtor que inicializa os atributos do produto
    public Produto(String nome, double preco, int quantia) {
        this.nome = nome;
        this.imposto = (float) (0.1 * preco);
        this.preco = preco + imposto;
        this.quantia = quantia;
    }

    // Getters e setters
    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantia;
    }

    public void setQuantidade(int quantidade) {
        this.quantia = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    // Representação em string do produto
    public String toString() {
        return nome + " - R$ " + preco + " (Estoque: " + quantia + ")";
    }
}
