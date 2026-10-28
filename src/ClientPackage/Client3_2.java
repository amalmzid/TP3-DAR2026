package ClientPackage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import CommonPackage.Operation;

public class Client3_2 {
    static final int PORT = 1234;
    // Mettre ici l'adresse IP du serveur ("localhost" en local, ex : "192.168.1.10" en réseau local)
    static final String IP_SERVEUR = "192.168.0.122";

    public static void main(String[] args) {
        try {
            Scanner clavier = new Scanner(System.in);

            // Connexion au serveur via InetAddress + InetSocketAddress
            InetAddress adresseServeur = InetAddress.getByName(IP_SERVEUR);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(adresseServeur, PORT));
            System.out.println("Je suis un client connecté au serveur " + socket.getRemoteSocketAddress());

            // Flux objets : ObjectOutputStream (avec flush) AVANT ObjectInputStream,
            // sinon les deux côtés se bloquent en attendant l'en-tête de l'autre
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // c) Lecture du numéro d'ordre envoyé par le serveur
            System.out.println(ois.readObject());

            while (true) {
                System.out.print("Premier nombre (ou 'quit') : ");
                String saisieA = clavier.next();
                if (saisieA.equalsIgnoreCase("quit")) {
                    break;
                }
                double a = Double.parseDouble(saisieA);

                System.out.print("Opérateur (+, -, *, /) : ");
                String operateur = clavier.next();
                if (!operateur.equals("+") && !operateur.equals("-")
                        && !operateur.equals("*") && !operateur.equals("/")) {
                    System.out.println("Opérateur invalide, rien n'est envoyé.");
                    continue;
                }

                System.out.print("Deuxième nombre : ");
                double b = clavier.nextDouble();

                // a) Envoi de l'objet Operation au serveur
                Operation op = new Operation(a, operateur.charAt(0), b);
                oos.writeObject(op);
                oos.flush();

                // b) Réception du résultat (objet String) envoyé par le serveur
                System.out.println("Résultat : " + ois.readObject());
            }

            System.out.println("Fin de la communication");
            clavier.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Erreur côté client : " + e.getMessage());
        }
    }
}
