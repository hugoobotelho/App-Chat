/* ***************************************************************
* Autor............: Hugo Botelho Santana
* Matricula........: 202210485
* Inicio...........: 21/11/2024
* Ultima alteracao.: 21/11/2024
* Nome.............: Camada de Transporte/Aplicação - Aplicativo de Instant Messaging
* Funcao...........: Aplicativo de chat para troca de mensagens com o modelo cliente servidor
*************************************************************** */
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Principal extends Application {
    private StackPane root = new StackPane(); // Usando StackPane para facilitar centralização
    private String nomeUsuario; // Nome do usuário conectado
    private String ipServidor;
    private ClienteTCP clienteTCP; // Instância do cliente TCP
    private ClienteUDP clienteUDP; // Instância do cliente UDP

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(root, 390, 744);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Aplicativo de Instant Messaging");
        primaryStage.setResizable(false);
        primaryStage.show();

        // Configura o evento de encerramento do aplicativo
        primaryStage.setOnCloseRequest(t -> {
            if (clienteUDP != null) {
                clienteUDP.fechar(); // Fecha o cliente UDP
            }
            Platform.exit();
            System.exit(0);
        });

        // Mostra a tela inicial ao iniciar o programa
        TelaInicio telaInicio = new TelaInicio(this);
        root.getChildren().setAll(telaInicio.getLayout());

        // Centralizando o layout da TelaInicio
        root.setAlignment(telaInicio.getLayout(), javafx.geometry.Pos.CENTER);
    }

    /**
     * Método chamado quando o usuário clica em "Entrar" na tela de início.
     * Inicializa o cliente TCP e UDP.
     */
    public void criarClientes(String ipServidor, String nomeUsuario) {
        this.ipServidor = ipServidor;
        this.nomeUsuario = nomeUsuario;

        // Criando e conectando o cliente TCP
        clienteTCP = new ClienteTCP(ipServidor, 6789);

        // Criando e conectando o cliente UDP
        criarClienteUDP(ipServidor, 6789);
    }

    /**
     * Método para criar o cliente UDP e iniciar a escuta de mensagens.
     */
    public void criarClienteUDP(String ipServidor, int porta) {
        try {
            clienteUDP = new ClienteUDP(ipServidor, porta); // Inicializa o cliente UDP
            System.out.println("Cliente UDP criado e conectado ao servidor " + ipServidor + ":" + porta);
            iniciarThreadRecebimentoUDP(); // Inicia a thread para receber mensagens via UDP
        } catch (Exception e) {
            System.err.println("Erro ao criar ClienteUDP: " + e.getMessage());
        }
    }

    /**
     * Método que inicia uma thread para escutar mensagens recebidas via UDP.
     */
    private void iniciarThreadRecebimentoUDP() {
        new Thread(() -> {
            try {
                while (true) {
                    String mensagemRecebida = clienteUDP.receberMensagem(); // Aguarda mensagens do servidor
                    System.out.println("Mensagem recebida via UDP: " + mensagemRecebida);

                    // Processar a mensagem recebida (atualizar interface gráfica, por exemplo)
                    Platform.runLater(() -> {
                        // Aqui você pode delegar a mensagem recebida para a tela de chat
                        // Exemplo: TelaChat.adicionarMensagem(mensagemRecebida);
                    });
                }
            } catch (Exception e) {
                System.err.println("Erro ao receber mensagem UDP: " + e.getMessage());
            }
        }).start();
    }

    // Métodos getter e setter
    public ClienteTCP getClienteTCP() {
        return clienteTCP;
    }

    public ClienteUDP getClienteUDP() {
        return clienteUDP;
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

    public String getIpServidor() {
        return ipServidor;
    }

    public StackPane getRoot() {
        return root;
    }

    /**
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
