package main;

import telas.Estoque;
import telas.TelaGerente;

import javax.swing.*;

import paths.Caminhos;

import java.awt.*;
import java.io.IOException;

public class GerenteMain extends Component {
    private Estoque estoque;

    // Armazena a senha do gerente (em um sistema real, use armazenamento seguro)
    private String senhaGerente = "admin";

    public GerenteMain() {
        // Carrega o inventário
        try {
            estoque = Estoque.carregarInventario(Caminhos.INVENTARIO_FILE);
        } catch (IOException e) {
            estoque = new Estoque();
        }

        // Define o look and feel Nimbus
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        autenticarDono();
    }

    private void autenticarDono() {
        JPasswordField passwordField = new JPasswordField(10);
        int action = JOptionPane.showConfirmDialog(this, passwordField, "Digite a senha:", JOptionPane.OK_CANCEL_OPTION);
        if (action == JOptionPane.OK_OPTION) {
            String senha = new String(passwordField.getPassword());
            if (senha.equals(senhaGerente)) {
                abrirTelaGerente();
                // Após autenticação, oferece opção de trocar senha
                int trocar = JOptionPane.showConfirmDialog(this, "Deseja trocar a senha?", "Trocar Senha", JOptionPane.YES_NO_OPTION);
                if (trocar == JOptionPane.YES_OPTION) {
                    trocarSenha();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Senha errada", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Novo método para troca de senha
    private void trocarSenha() {
        JPasswordField novaSenhaField = new JPasswordField(10);
        JPasswordField confirmarSenhaField = new JPasswordField(10);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Nova senha:"));
        panel.add(novaSenhaField);
        panel.add(new JLabel("Confirmar senha:"));
        panel.add(confirmarSenhaField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Trocar Senha", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String novaSenha = new String(novaSenhaField.getPassword());
            String confirmarSenha = new String(confirmarSenhaField.getPassword());
            if (novaSenha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "A nova senha não pode ser vazia.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else if (!novaSenha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                senhaGerente = novaSenha;
                JOptionPane.showMessageDialog(this, "Senha alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void abrirTelaGerente() {
        SwingUtilities.invokeLater(() -> TelaGerente.getInstance(estoque));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GerenteMain::new);
    }
}
