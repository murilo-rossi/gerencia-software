package Main;

import Telas.Estoque;
import Paths.Caminhos;
import Telas.TelaCliente;

import javax.swing.*;
import java.io.IOException;

public class ClienteMain{
    private Estoque Estoque;

    public ClienteMain() {
        // Carrega o inventário
        try {
            Estoque = Estoque.carregarInventario(Caminhos.INVENTARIO_FILE);
        } catch (IOException e) {
            Estoque = new Estoque();
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
        SwingUtilities.invokeLater(() -> new TelaCliente(Estoque));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClienteMain::new);
    }
}
