/* ***************************************************************
* Autor............: Hugo Botelho Santana
* Matricula........: 202210485
* Inicio...........: 21/11/2024
* Ultima alteracao.: 28/11/2024
* Nome.............: Camada de Transporte/Aplicação - Aplicativo de Instant Messaging
* Funcao...........: Aplicativo de chat para troca de mensagens com o modelo cliente servidor
*************************************************************** */
import java.util.HashMap;
import java.util.Map;

public class Principal {

    private static final GrupoManager grupoManager = new GrupoManager(); // Gerencia grupos
    private static final Map<String, Usuario> usuarios = new HashMap<>(); // Gerencia usuários

    public static void main(String[] args) {
        // Inicia o servidor UDP em uma thread separada
        Thread servidorUDPThread = new Thread(() -> {
            ServidorUDP servidorUDP = new ServidorUDP(grupoManager, usuarios);
            servidorUDP.iniciar();
        });

        // Inicia o servidor TCP em uma thread separada
        Thread servidorTCPThread = new Thread(() -> {
            ServidorTCP servidorTCP = new ServidorTCP(grupoManager, usuarios);
            servidorTCP.iniciar();
        });

        // Inicia as threads
        servidorUDPThread.start();
        servidorTCPThread.start();

        System.out.println("Servidores UDP e TCP iniciados...");
    }

    // Getter para o GrupoManager
    public static GrupoManager getGrupoManager() {
        return grupoManager;
    }

    // Getter para o mapa de usuários
    public static Map<String, Usuario> getUsuarios() {
        return usuarios;
    }
}
