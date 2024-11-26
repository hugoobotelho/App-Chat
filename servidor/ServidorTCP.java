package servidor;

import java.net.*;
import java.io.*;

public class ServidorTCP {

    public void iniciar() {
        try {
            int portaLocal = 6789;
            ServerSocket servidorSocket = new ServerSocket(portaLocal); // Socket do servidor
            System.out.println("Servidor TCP iniciado na porta 6789...");

            while (true) {
                Socket conexao = servidorSocket.accept(); // Aceita conexões de clientes

                // Inicia uma thread para lidar com o cliente
                new Thread(new ProcessaCliente(conexao)).start();
            }
        } catch (Exception e) {
            System.err.println("Erro no servidor TCP: " + e.getMessage());
        }
    }

    /**
     * Classe interna para processar conexões de clientes em threads separadas.
     */
    private static class ProcessaCliente implements Runnable {
        private Socket conexao;

        public ProcessaCliente(Socket conexao) {
            this.conexao = conexao;
        }

        @Override
        public void run() {
            try {
                // Configura o fluxo de entrada
                ObjectInputStream entrada = new ObjectInputStream(conexao.getInputStream());
                String mensagemRecebida = (String) entrada.readObject();
                System.out.println("Mensagem recebida via TCP: " + mensagemRecebida);

                // Envia uma resposta ao cliente (opcional)
                ObjectOutputStream saida = new ObjectOutputStream(conexao.getOutputStream());
                saida.writeObject("Mensagem recebida com sucesso: " + mensagemRecebida);
                saida.flush();

                // Fecha a conexão
                conexao.close();
            } catch (Exception e) {
                System.err.println("Erro ao processar cliente TCP: " + e.getMessage());
            }
        }
    }
}
