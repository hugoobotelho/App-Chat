/* ***************************************************************
* Autor............: Hugo Botelho Santana
* Matricula........: 202210485
* Inicio...........: 21/11/2024
* Ultima alteracao.: 21/11/2024
* Nome.............: Camada de Transporte/Aplicação - Aplicativo de Instant Messaging
* Funcao...........: Aplicativo de chat para troca de mensagens com o modelo cliente servidor
*************************************************************** */
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Principal extends Application {
    private StackPane root = new StackPane(); // Usando StackPane para facilitar centralização
    private String nomeUsuario; // Nome do usuário conectado
    private String ipServidor;

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(root, 390, 744);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Aplicativo de Instant Messaging");
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        // Mostra a tela inicial ao iniciar o programa
        TelaInicio telaInicio = new TelaInicio(this);
        root.getChildren().setAll(telaInicio.getLayout());

        // Centralizando o layout da TelaInicio
        root.setAlignment(telaInicio.getLayout(), javafx.geometry.Pos.CENTER);
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setIpServidor(String ip) {
        this.ipServidor = ip;
    }

    public String getIpServidor(){
        return ipServidor;
    }

    public StackPane getRoot() {
        return root;
    }

    /*
     * ***************************************************************
     * Metodo: main.
     * Funcao: metodo para iniciar a aplicacao.
     * Parametros: padrao java.
     * Retorno: sem retorno.
     * ***************************************************************
     */
    public static void main(String[] args) {
        launch(args);
    }
}
