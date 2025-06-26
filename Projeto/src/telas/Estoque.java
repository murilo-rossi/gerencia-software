package telas;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Estoque implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Produto> produtos;

    // Construtor que inicializa o mapa de produtos
    public Estoque() {
        produtos = new HashMap<>();
    }

    // Adiciona um produto ao mapa
    public void addProduto(Produto produto) {
        produtos.put(produto.getNome(), produto);
    }

    // Pesquisa um produto pelo nome
    public Produto pesquisarProduto(String nome) {
        return produtos.get(nome);
    }

    // Deleta um produto do mapa pelo nome
    public void deletarProduto(String nomeProduto) {
        produtos.remove(nomeProduto);
    }

    // Carrega o inventário de um arquivo e retorna uma instância de Estoque
    public static Estoque carregarInventario(String caminhoArquivo) throws IOException {
        Estoque estoque = new Estoque();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                String nome = partes[0];
                double preco = Double.parseDouble(partes[1]);
                int quantidade = Integer.parseInt(partes[2]);
                estoque.addProduto(new Produto(nome, preco, quantidade));
            }
        }
        return estoque;
    }

    // Salva o inventário em um arquivo
    public void salvarInv(String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (Produto produto : produtos.values()) {
                writer.write(produto.getNome() + "," + produto.getPreco() + "," + produto.getQuantidade());
                writer.newLine();
            }
        }
    }

    // Retorna o mapa de produtos
    public Map<String, Produto> getProdutos() {
        return produtos;
    }
}
