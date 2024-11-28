import java.net.*;
import java.util.Scanner;

public class ClienteUDP {
    private final DatagramSocket clienteSocket;
    private final DatagramSocket clienteSocket1;
    private final InetAddress enderecoServidor;
    private final int portaServidor;


    public ClienteUDP(String ipServidor, int portaServidor) throws Exception {
        this.clienteSocket = new DatagramSocket(); // Socket para comunicação UDP
        this.enderecoServidor = InetAddress.getByName(ipServidor); // Endereço do servidor
        this.portaServidor = portaServidor;
        this.clienteSocket1 = new DatagramSocket(9876);  // Escolha uma porta fixada
    }

    /**
     * Envia uma mensagem para o servidor.
     */
    public void enviarMensagem(String mensagem) {
        try {
            byte[] dadosEnvio = mensagem.getBytes();
            DatagramPacket pacoteEnvio = new DatagramPacket(
                    dadosEnvio,
                    dadosEnvio.length,
                    enderecoServidor,
                    portaServidor);
            clienteSocket.send(pacoteEnvio);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Aguarda e recebe uma mensagem do servidor.
     */
    public String receberMensagem() {
        try {

            byte[] dadosRecebidos = new byte[1024];
            DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);
            clienteSocket1.receive(pacoteRecebido); // Bloqueia até receber um pacote
            return new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro";
        }

    }

    /**
     * Fecha o socket do cliente e para a escuta.
     */
    public void fechar() {
        // escutando = false; // Para a thread de escuta
        clienteSocket.close();
    }
}
