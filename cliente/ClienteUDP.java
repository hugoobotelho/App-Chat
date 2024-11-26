import java.net.*;
import java.util.Scanner;

public class ClienteUDP {
    private final DatagramSocket clienteSocket;
    private final InetAddress enderecoServidor;
    private final int portaServidor;

    public ClienteUDP(String ipServidor, int portaServidor) throws Exception {
        this.clienteSocket = new DatagramSocket(); // Socket para comunicação UDP
        this.enderecoServidor = InetAddress.getByName(ipServidor); // Endereço do servidor
        this.portaServidor = portaServidor;
    }

    /**
     * Envia uma mensagem para o servidor.
     */
    public void enviarMensagem(String mensagem) throws Exception {
        byte[] dadosEnvio = mensagem.getBytes();
        DatagramPacket pacoteEnvio = new DatagramPacket(
                dadosEnvio, 
                dadosEnvio.length, 
                enderecoServidor, 
                portaServidor
        );
        clienteSocket.send(pacoteEnvio);
    }

    /**
     * Aguarda e recebe uma mensagem do servidor.
     */
    public String receberMensagem() throws Exception {
        byte[] dadosRecebidos = new byte[1024];
        DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);
        clienteSocket.receive(pacoteRecebido);
        return new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());
    }

    /**
     * Fecha o socket do cliente.
     */
    public void fechar() {
        clienteSocket.close();
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Configuração: IP e porta do servidor
            String ipServidor = "127.0.0.1"; // Localhost
            int portaServidor = 6789;

            // Criação do cliente UDP
            ClienteUDP cliente = new ClienteUDP(ipServidor, portaServidor);
            System.out.println("Cliente conectado ao servidor em " + ipServidor + ":" + portaServidor);

            // Nome do usuário
            System.out.print("Digite seu nome: ");
            String nomeUsuario = scanner.nextLine();

            // Loop para enviar mensagens
            while (true) {
                System.out.print("Mensagem (grupo|mensagem ou 'sair' para encerrar): ");
                String mensagem = scanner.nextLine();

                if (mensagem.equalsIgnoreCase("sair")) {
                    System.out.println("Encerrando cliente...");
                    break;
                }

                // Formato da mensagem: grupo|usuario|mensagem
                System.out.print("Digite o nome do grupo: ");
                String nomeGrupo = scanner.nextLine();

                String mensagemFormatada = nomeGrupo + "|" + nomeUsuario + "|" + mensagem;

                // Envia mensagem para o servidor
                cliente.enviarMensagem(mensagemFormatada);

                // Aguarda resposta do servidor
                String resposta = cliente.receberMensagem();
                System.out.println("Servidor: " + resposta);
            }

            cliente.fechar();
            scanner.close();
        } catch (Exception e) {
            System.err.println("Erro no cliente UDP: " + e.getMessage());
        }
    }
}
