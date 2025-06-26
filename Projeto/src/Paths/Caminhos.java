package paths;

//Caminhos para os arquivos de pedidos e inventários do café
public class Caminhos {
    /**
     * Construtor privado para impedir a instanciação desta classe utilitária.
     */
    private Caminhos() {
        // Este construtor está intencionalmente vazio.
    }

    public static final String INVENTARIO_FILE = Caminhos.class.getResource("/Dados/inventario.txt").getPath();
    public static final String PEDIDOS_FILE = Caminhos.class.getResource("/Dados/pedidos.txt").getPath();
}
