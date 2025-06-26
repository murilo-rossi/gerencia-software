package telas;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class Ordem implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(Ordem.class.getName());
    private static final long serialVersionUID = 1L;
    private List<Produto> itens;
    private double total;

    // Construtor que inicializa a lista de itens e o total
    public Ordem() {
        this.itens = new ArrayList<>();
        this.total = 0.0;
    }

    // Adiciona um item ao pedido
    public void addItem(Produto produto, int quantidade) {
        if (produto.getQuantidade() >= quantidade) {
            produto.setQuantidade(produto.getQuantidade() - quantidade);
            for (int i = 0; i < quantidade; i++) {
                itens.add(produto);
            }
            total += produto.getPreco() * quantidade;
        } else {
            JOptionPane.showMessageDialog(null, "Nao sera possivel, temos apenas " + produto.getQuantidade() + " unidades \n no estoque para o produto " + produto.getNome(), "Estoque Insuficiente", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Remove um item do pedido
    public void removerItem(String nomeProduto, int quantidade) {
        int count = 0;
        for (int i = itens.size() - 1; i >= 0; i--) {
            Produto p = itens.get(i);
            if (p.getNome().equals(nomeProduto) && count < quantidade) {
                itens.remove(i);
                p.setQuantidade(p.getQuantidade() + 1);
                total -= p.getPreco();
                count++;
            }
        }
        if (count < quantidade) {
            JOptionPane.showMessageDialog(null, "A quantidade para remoção excede a quantidade no pedido", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Retorna a lista de itens do pedido
    public List<Produto> getItens() {
        return itens;
    }

    // Calcula e retorna o total do pedido
    public double calTotal() {
        return total;
    }

    // Finaliza o pedido (exibe o total no console)
    public void finalizarPedido() {
        LOGGER.log(Level.INFO, "Pedido finalizado. Total: R$ {0}", calTotal());
        this.itens.clear();
    }

    // Salva os detalhes do pedido em um arquivo
    public void salvarPedido(String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            writer.write("\n\n*******------------------------------*******\n");
            writer.write("Data do Pedido: " + sdf.format(new Date()));
            writer.newLine();
            writer.newLine();

            // Agrupa itens por nome para evitar duplicatas
            Map<String, Integer> quantidadeMap = new HashMap<>();
            Map<String, Double> precoMap = new HashMap<>();

            for (Produto p : itens) {
                quantidadeMap.put(p.getNome(), quantidadeMap.getOrDefault(p.getNome(), 0) + 1);
                precoMap.put(p.getNome(), p.getPreco());
            }

            for (Map.Entry<String, Integer> entry : quantidadeMap.entrySet()) {
                String nome = entry.getKey();
                Integer quantidade = entry.getValue(); // Obtém a quantidade diretamente

                writer.write(String.format("%nProduto: %s%nPreço Unitário: %.2f%nUnidades: %d",
                         nome, precoMap.get(nome), quantidade));
                writer.newLine();
            }

            writer.newLine();
            writer.write("Valor             R$ " + String.format("%.2f", total));
            writer.newLine();
            writer.newLine();
            writer.write("*******------------------------------*******");
        }
    }

    // Retorna a representação em string do pedido
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido:\n");
        for (Produto p : itens) {
            sb.append(p.getNome()).append(" - R$ ").append(p.getPreco()).append("\n");
        }
        return sb.toString();
    }
}
