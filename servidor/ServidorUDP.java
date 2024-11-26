package servidor;

import java.net.*;

public class ServidorUDP {
    private final GrupoManager grupoManager;

    public ServidorUDP() {
        this.grupoManager = new GrupoManager();
    }

    public void iniciar() {
        try {
            DatagramSocket servidorSocket = new DatagramSocket(6789); // Porta do servidor
            System.out.println("Servidor UDP iniciado na porta 6789...");

            while (true) {
                // Buffer para receber dados
                byte[] dadosRecebidos = new byte[1024];
                DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);

                // Aguarda uma mensagem de um cliente
                servidorSocket.receive(pacoteRecebido);

                // Inicia uma nova thread para processar a mensagem recebida
                new Thread(new ProcessaMensagem(pacoteRecebido, servidorSocket, grupoManager)).start();
            }
        } catch (Exception e) {
            System.err.println("Erro no servidor UDP: " + e.getMessage());
        }
    }

    private static class ProcessaMensagem implements Runnable {
        private final DatagramPacket pacoteRecebido;
        private final DatagramSocket servidorSocket;
        private final GrupoManager grupoManager;

        public ProcessaMensagem(DatagramPacket pacoteRecebido, DatagramSocket servidorSocket, GrupoManager grupoManager) {
            this.pacoteRecebido = pacoteRecebido;
            this.servidorSocket = servidorSocket;
            this.grupoManager = grupoManager;
        }

        @Override
        public void run() {
            try {
                // Converte os dados recebidos em String
                String mensagemRecebida = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());
                System.out.println("Mensagem recebida de " + pacoteRecebido.getAddress() + ":" + pacoteRecebido.getPort());
                System.out.println("Conteúdo: " + mensagemRecebida);

                // Formato esperado da mensagem: "grupo|usuario|mensagem"
                String[] partes = mensagemRecebida.split("\\|", 3);
                if (partes.length != 3) {
                    System.err.println("Formato inválido de mensagem");
                    return;
                }

                String nomeGrupo = partes[0];
                String nomeUsuario = partes[1];
                String mensagem = partes[2];

                // Cria o objeto do usuário remetente
                Usuario remetente = new Usuario(nomeUsuario, pacoteRecebido.getAddress(), pacoteRecebido.getPort());

                // Envia a mensagem para todos os membros do grupo, exceto o remetente
                for (Usuario usuario : grupoManager.obterMembros(nomeGrupo)) {
                    if (!usuario.equals(remetente)) {
                        byte[] dadosSaida = (nomeUsuario + ": " + mensagem).getBytes();
                        DatagramPacket pacoteResposta = new DatagramPacket(
                                dadosSaida,
                                dadosSaida.length,
                                usuario.getEndereco(),
                                usuario.getPorta()
                        );
                        servidorSocket.send(pacoteResposta);
                    }
                }

                // Adiciona o remetente ao grupo (se necessário)
                grupoManager.adicionarUsuario(nomeGrupo, remetente);

            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem: " + e.getMessage());
            }
        }
    }
}
