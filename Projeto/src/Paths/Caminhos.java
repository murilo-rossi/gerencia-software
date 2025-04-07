package Paths;

import java.nio.file.Paths;

//Caminhos para os arquivos de pedidos e inventários do café
public class Caminhos {
    public static final String INVENTARIO_FILE = Caminhos.class.getResource("/Dados/inventario.txt").getPath();
    public static final String PEDIDOS_FILE = Caminhos.class.getResource("/Dados/pedidos.txt").getPath();
}
