package ServerPackage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT3_2 {
    static final int PORT = 1234;

    // compteur global partagé : nombre total d'opérations traitées par le serveur
    private static int compteurOperations = 0;

    // synchronized : un seul thread ClientProcess à la fois peut incrémenter
    public static synchronized int incrementerCompteur() {
        return ++compteurOperations;
    }

    public static void main(String[] args) {

        int numero = 0; // numéro d'ordre de connexion
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            // adresse IP de la machine serveur, à donner aux clients du réseau local
            System.out.println("Serveur démarré sur " + InetAddress.getLocalHost().getHostAddress() + ":" + PORT);
            System.out.println("Le serveur attend la connexion d'un client…");
            while (true) {
                Socket socket = serverSocket.accept(); // bloque jusqu'au prochain client
                numero++;
                // b) IP + numéro d'ordre du client
                System.out.println("Client n°" + numero + " connecté, adresse : "  + socket.getRemoteSocketAddress());
                // un thread dédié par client, qui reçoit sa socket et son numéro
                new ClientProcess3_2(socket, numero).start();
            }
        } catch (IOException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }

    }
}