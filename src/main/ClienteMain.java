package main;

import telas.Estoque;
import telas.TelaCliente;

import javax.swing.*;

import paths.Caminhos;

import java.io.IOException;

public class ClienteMain{
    private Estoque estoque;

    public ClienteMain() {
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

        abrirTelaCliente();
    }

    private void abrirTelaCliente() {
        SwingUtilities.invokeLater(() -> new TelaCliente(estoque));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClienteMain::new);
    }
}
