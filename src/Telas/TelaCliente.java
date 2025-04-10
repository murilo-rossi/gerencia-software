package Telas;

import Paths.Caminhos;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TelaCliente extends JFrame {
    private static Ordem ordemAtual;
    private static JTable tabelaPedido;
    private static DefaultTableModel tabelaModel;
    private static JLabel totalLabel;

    public TelaCliente(Estoque Estoque) {
        ordemAtual = new Ordem();

        setTitle("Comprador");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton finalizarButton = new JButton("Finalizar Pedido");

        // Modelo da tabela de pedidos, removendo a coluna "Remover"
        tabelaModel = new DefaultTableModel(new Object[]{"Produto", "Quantidade", "Valor"}, 0);
        tabelaPedido = new JTable(tabelaModel);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tabelaPedido.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        finalizarButton.addActionListener(e -> {
            if (ordemAtual.calTotal() > 0) {
                double total = ordemAtual.calTotal();
                JOptionPane.showMessageDialog(TelaCliente.this, "Pedido realizado com sucesso! Total: R$ " + String.format("%.2f", total));
                try {
                    ordemAtual.salvarPedido(Caminhos.PEDIDOS_FILE);
                    Estoque.salvarInv(Caminhos.INVENTARIO_FILE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ordemAtual.finalizarPedido();
                ordemAtual = new Ordem();
                atualizarAreaPedido();
            } else {
                JOptionPane.showMessageDialog(TelaCliente.this, "Nenhum produto foi selecionado! Adicione um produto ou aperte em 'Sair'", "Houve um Erro", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel productsPanel = new JPanel();
        productsPanel.setLayout(new GridBagLayout()); // Usando GridBagLayout para alinhamento preciso
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Definindo um pequeno espaço de 5px ao redor dos componentes

        int row = 0;
        for (Produto produto : Estoque.getProdutos().values()) {
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 1.0; // Faz com que o label ocupe o espaço disponível
            String produtoInfo = String.format("%s - R$ %.2f (Estoque: %d)", produto.getNome(), produto.getPreco(), produto.getQuantidade());
            JLabel productLabel = new JLabel(produtoInfo);
            productsPanel.add(productLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = row;
            gbc.weightx = 0; // Não expandir os botões
            JButton addButton = new JButton("Adicionar");
            productsPanel.add(addButton, gbc);

            gbc.gridx = 2;
            gbc.gridy = row;
            JButton removeButton = new JButton("Remover");
            productsPanel.add(removeButton, gbc);

            // Incrementa a linha para o próximo produto
            row++;

            // Adiciona os listeners para os botões
            addButton.addActionListener(e -> {
                int quantidade = 1; // Quantidade padrão ao adicionar um produto
                ordemAtual.addItem(produto, quantidade);
                atualizarAreaPedido();
                TelaGerente telaGerente = TelaGerente.getInstance();
                if (telaGerente != null) {
                    telaGerente.atualizarTabelaEstoque();
                }
            });

            removeButton.addActionListener(e -> {
                int quantidade = 1; // Quantidade padrão ao remover um produto
                ordemAtual.removerItem(produto.getNome(), quantidade);
                atualizarAreaPedido();
                TelaGerente telaGerente = TelaGerente.getInstance();
                if (telaGerente != null) {
                    telaGerente.atualizarTabelaEstoque();
                }
            });
        }

        JScrollPane scrollPane = new JScrollPane(productsPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        totalLabel = new JLabel("Total: R$ 0.00");
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        bottomPanel.add(totalLabel);

        finalizarButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        bottomPanel.add(finalizarButton);

        add(scrollPane, BorderLayout.NORTH);  // Painel com a lista de produtos em cima
        add(new JScrollPane(tabelaPedido), BorderLayout.CENTER);  // Tabela de pedidos ao centro
        add(bottomPanel, BorderLayout.SOUTH);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
        setVisible(true);
    }

    private void atualizarAreaPedido() {
        tabelaModel.setRowCount(0);

        Map<String, Integer> quantidadeMap = new HashMap<>();
        Map<String, Double> valorMap = new HashMap<>();

        for (Produto p : ordemAtual.getItens()) {
            quantidadeMap.put(p.getNome(), quantidadeMap.getOrDefault(p.getNome(), 0) + 1);
            valorMap.put(p.getNome(), valorMap.getOrDefault(p.getNome(), 0.0) + p.getPreco());
        }

        for (String nome : quantidadeMap.keySet()) {
            tabelaModel.addRow(new Object[]{
                    nome, quantidadeMap.get(nome), String.format("R$ %.2f", valorMap.get(nome))
            });
        }

        totalLabel.setText("Total: R$ " + String.format("%.2f", ordemAtual.calTotal()));
    }
}
