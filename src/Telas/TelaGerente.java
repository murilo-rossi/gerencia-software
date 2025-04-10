package Telas;

import Paths.Caminhos;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** Classe que representa a interface do gerente */
public class TelaGerente extends JFrame {
    private static TelaGerente instance;
    private Estoque Estoque;
    private DefaultTableModel tabelaModel;
    private JTable tabelaEstoque;

    /**
     * Construtor da classe TelaGerente
     * @param Estoque O inventário da cafeteria
     */
    public TelaGerente(Estoque Estoque) {
        this.Estoque = Estoque;
        instance = this;

        // Configuração da tela inicial
        setTitle("Administrador");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Configuração da tabela de estoque
        tabelaModel = new DefaultTableModel(new Object[]{"Produto", "Preço", "Quantidade", ""}, 0);
        tabelaEstoque = new JTable(tabelaModel);
        atualizarTabelaEstoque();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabelaEstoque.getColumnCount(); i++) {
            tabelaEstoque.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Configuração do evento de clique na tabela de estoque
        tabelaEstoque.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tabelaEstoque.rowAtPoint(e.getPoint());
                int column = tabelaEstoque.columnAtPoint(e.getPoint());
                if (column == 3) { // Coluna "Atualizar"
                    String nomeProduto = (String) tabelaModel.getValueAt(row, 0);
                    Produto produto = Estoque.pesquisarProduto(nomeProduto);
                    if (produto != null) {
                        abrirDialogoAtualizacao(produto);
                    }
                }
            }
        });

        // Configuração dos paineis
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(tabelaEstoque), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        // Configuração dos botões no painel
        JButton cadastrarButton = new JButton("Cadastrar Produto");
        cadastrarButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        cadastrarButton.addActionListener(e -> abrirDialogoCadastro());
        bottomPanel.add(cadastrarButton);

        JButton excluirButton = new JButton("Excluir Produto");
        excluirButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        excluirButton.addActionListener(e -> abrirDialogoExclusao());
        bottomPanel.add(excluirButton);

        JButton relatorioButton = new JButton("Relatório de Vendas");
        relatorioButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        relatorioButton.addActionListener(e -> exibirRelatorioVendas());
        bottomPanel.add(relatorioButton);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
        setVisible(true);
    }

    /**
     * Abre o diálogo de cadastro de produtos.
     */
    private void abrirDialogoCadastro() {
        JDialog dialogoCadastro = new JDialog(this, "Cadastrar Produto", true);
        dialogoCadastro.setSize(300, 300);
        dialogoCadastro.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Configuração dos elementos no painel
        JLabel nomeLabel = new JLabel("Nome do Produto:");
        nomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        JTextField nomeField = new JTextField();

        JLabel precoLabel = new JLabel("Preço do Produto:");
        precoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        JTextField precoField = new JTextField();

        JLabel quantidadeLabel = new JLabel("Quantidade:");
        quantidadeLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        JTextField quantidadeField = new JTextField();

        JButton cadastrarButton = new JButton("Cadastrar Produto");
        cadastrarButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente

        // Escuta de ações no botão de cadastrar
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                double preco;
                int quantidade;

                // Tentativa de converter texto para números
                try {
                    preco = Double.parseDouble(precoField.getText());
                    quantidade = Integer.parseInt(quantidadeField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialogoCadastro, "Valor inválido. use um ponto (.) para números decimais.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Cria um novo objeto Produto com os dados fornecidos
                Produto produto = new Produto(nome, preco, quantidade);
                Estoque.addProduto(produto); // Adiciona o produto à Estoque
                JOptionPane.showMessageDialog(dialogoCadastro, "Produto cadastrado com sucesso!");

                try {
                    Estoque.salvarInv(Caminhos.INVENTARIO_FILE); // Salva o inventário atualizado
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                atualizarTabelaEstoque(); // Atualiza a tabela de estoque na tela de gerente
                dialogoCadastro.dispose(); // Fecha o diálogo de cadastro
            }
        });

        // Adiciona os elementos no painel com espaçamento
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento superior
        panel.add(nomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5))); // Espaçamento entre label e field
        panel.add(nomeField);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento entre os conjuntos de componentes
        panel.add(precoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(precoField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(quantidadeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(quantidadeField);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaçamento antes do botão
        panel.add(cadastrarButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento inferior

        dialogoCadastro.add(panel);

        // Centraliza o diálogo na tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialogoCadastro.setLocation((screenSize.width - dialogoCadastro.getWidth()) / 2, (screenSize.height - dialogoCadastro.getHeight()) / 2);
        dialogoCadastro.setVisible(true);
    }

    /**
     * Abre o diálogo de exclusão de produtos.
     */
    private void abrirDialogoExclusao() {
        JDialog dialogoExclusao = new JDialog(this, "Excluir Produto", true);
        dialogoExclusao.setSize(300, 150);
        dialogoExclusao.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel selectLabel = new JLabel("Selecione o Produto:");
        selectLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        JComboBox<String> produtoComboBox = new JComboBox<>();
        for (Produto produto : Estoque.getProdutos().values()) {
            produtoComboBox.addItem(produto.getNome());
        }

        JButton excluirButton = new JButton("Excluir Produto");
        excluirButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeProduto = (String) produtoComboBox.getSelectedItem();
                int confirm = JOptionPane.showConfirmDialog(dialogoExclusao, "Tem certeza que deseja excluir o produto " + nomeProduto + "?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Estoque.deletarProduto(nomeProduto);
                    JOptionPane.showMessageDialog(dialogoExclusao, "Produto excluído com sucesso!");

                    try {
                        Estoque.salvarInv(Caminhos.INVENTARIO_FILE); // Salva o inventário atualizado
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    atualizarTabelaEstoque(); // Atualiza a tabela de estoque na tela de gerente
                    dialogoExclusao.dispose(); // Fecha o diálogo de exclusão
                }
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento superior
        panel.add(selectLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(produtoComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaçamento antes do botão
        panel.add(excluirButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento inferior

        dialogoExclusao.add(panel);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialogoExclusao.setLocation((screenSize.width - dialogoExclusao.getWidth()) / 2, (screenSize.height - dialogoExclusao.getHeight()) / 2);
        dialogoExclusao.setVisible(true);
    }

    /**
     * Abre o diálogo de atualização de produtos.
     * @param produto O produto que será atualizado.
     */
    private void abrirDialogoAtualizacao(Produto produto) {
        JDialog dialogoAtualizacao = new JDialog(this, "Atualizar Produto", true);
        dialogoAtualizacao.setSize(300, 300);
        dialogoAtualizacao.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Configuração dos elementos no painel
        JLabel nomeLabel = new JLabel("Nome do Produto:");
        nomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        JTextField nomeField = new JTextField(produto.getNome());
        nomeField.setEnabled(false); // Nome não pode ser alterado

        JLabel precoLabel = new JLabel("Preço do Produto:");
        precoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        JTextField precoField = new JTextField(String.valueOf(produto.getPreco()));


        JLabel quantidadeLabel = new JLabel("Quantidade:");
        quantidadeLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente
        JTextField quantidadeField = new JTextField(String.valueOf(produto.getQuantidade()));

        JButton atualizarButton = new JButton("Atualizar Produto");
        atualizarButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente

        // Escuta de ações no botão de atualizar
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double preco;
                int quantidade;

                // Tentativa de converter texto para números
                try {
                    preco = Double.parseDouble(precoField.getText());
                    quantidade = Integer.parseInt(quantidadeField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialogoAtualizacao, "Valor inválido. use um ponto (.) para números decimais.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Atualiza o objeto Produto com os novos dados
                Produto produtoAtualizado = new Produto(produto.getNome(), preco, quantidade);
                Estoque.addProduto(produtoAtualizado); // Atualiza o produto na Estoque
                JOptionPane.showMessageDialog(dialogoAtualizacao, "Produto atualizado com sucesso!");

                try {
                    Estoque.salvarInv(Caminhos.INVENTARIO_FILE); // Salva o inventário atualizado
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                atualizarTabelaEstoque(); // Atualiza a tabela de estoque na tela de gerente
                dialogoAtualizacao.dispose(); // Fecha o diálogo de atualização
            }
        });

        // Adiciona os elementos no painel com espaçamento
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento superior
        panel.add(nomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5))); // Espaçamento entre label e field
        panel.add(nomeField);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento entre os conjuntos de componentes
        panel.add(precoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(precoField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(quantidadeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(quantidadeField);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaçamento antes do botão
        panel.add(atualizarButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento inferior

        dialogoAtualizacao.add(panel);

        // Centraliza o diálogo na tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialogoAtualizacao.setLocation((screenSize.width - dialogoAtualizacao.getWidth()) / 2, (screenSize.height - dialogoAtualizacao.getHeight()) / 2);
        dialogoAtualizacao.setVisible(true);
    }

    /**
     * Atualiza a tabela de estoque com os produtos atuais.
     */
    public void atualizarTabelaEstoque() {
        tabelaModel.setRowCount(0); // Limpa a tabela
        for (Produto produto : Estoque.getProdutos().values()) {
            tabelaModel.addRow(new Object[]{produto.getNome(), String.format("%.2f", produto.getPreco()), produto.getQuantidade(), "Atualizar"});
        }
    }

    /**
     * Exibe o relatório de vendas.
     */
    private void exibirRelatorioVendas() {
        // Carrega os dados do arquivo de relatório de vendas
        String filePath = Caminhos.PEDIDOS_FILE;
        StringBuilder relatorioTexto = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                relatorioTexto.append(linha).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar o relatório de vendas.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cria uma nova janela para exibir o relatório
        JDialog dialogRelatorio = new JDialog(this, "Relatório de Vendas", true);
        dialogRelatorio.setSize(500, 400);
        dialogRelatorio.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea(relatorioTexto.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        dialogRelatorio.add(scrollPane, BorderLayout.CENTER);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialogRelatorio.setLocation((screenSize.width - dialogRelatorio.getWidth()) / 2, (screenSize.height - dialogRelatorio.getHeight()) / 2);
        dialogRelatorio.setVisible(true);
    }
    /**
     * Retorna a instância atual da tela do gerente.
     * @return A instância da tela do gerente.
     */
    public static TelaGerente getInstance() {
        return instance;
    }
}
