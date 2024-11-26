package servidor;

public class Principal {

    public static void main(String[] args) {
        // Inicia o servidor UDP em uma thread separada
        Thread servidorUDPThread = new Thread(() -> {
            ServidorUDP servidorUDP = new ServidorUDP();
            servidorUDP.iniciar();
        });

        // Inicia o servidor TCP em uma thread separada
        Thread servidorTCPThread = new Thread(() -> {
            ServidorTCP servidorTCP = new ServidorTCP();
            servidorTCP.iniciar();
        });

        // Inicia as threads
        servidorUDPThread.start();
        servidorTCPThread.start();

        System.out.println("Servidores UDP e TCP iniciados...");
    }
}
