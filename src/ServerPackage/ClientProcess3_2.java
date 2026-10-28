package ServerPackage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import CommonPackage.Operation;

public class ClientProcess3_2 extends Thread{
    private Socket socket;
    private int numero;
    public ClientProcess3_2(Socket socket,int numero){
        this.socket=socket;
        this.numero=numero;
    }
    public void run(){
        try {
            System.out.println("le client numero " + numero + " est connecté");
            // Flux objets : on crée l'ObjectOutputStream (et on le flush) AVANT l'ObjectInputStream
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // c) envoi au client de son numéro d'ordre de connexion
            oos.writeObject("Bienvenue, vous êtes le client n°" + numero);
            oos.flush();
            while (true) {
                // c) Le serveur reçoit l'objet
                Object recu = ois.readObject();
                if (!(recu instanceof Operation)) {
                    continue;
                }
                Operation op = (Operation) recu;
                // d) Le serveur récupère les données de l'objet
                double a = op.getOperande1();
                char operateur = op.getOperateur();
                double b = op.getOperande2();
                System.out.println("Opération reçue de client numero " + numero + " : " + a + " " + operateur + " " + b);

                // e) Le serveur effectue le calcul
                String reponse = calculer(a, operateur, b);

                // Étape 3 : incrémentation synchronisée du compteur global
                int total = ServerMT3_2.incrementerCompteur();
                System.out.println("Opérations traitées au total : " + total);

                // f) Le serveur renvoie le résultat au client
                oos.writeObject(reponse);
                oos.flush();
                System.out.println("Réponse envoyée : " + reponse + " au client numero " + numero);
            }
        } catch (EOFException e) {
            // le client a fermé la connexion
            System.out.println("le client numero " + numero + " est déconnecté");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    static String calculer(double a, char operateur, double b) {
        double r;
        switch (operateur) {
            case '+': r = a + b; break;
            case '-': r = a - b; break;
            case '*': r = a * b; break;
            case '/':
                if (b == 0) {
                    return "Erreur : division par zéro impossible";
                }
                r = a / b;
                break;
            default:
                return "Erreur : opérateur inconnu";
        }
        return formater(a) + " " + operateur + " " + formater(b) + " = " + formater(r);
    }

    // Affiche 55 au lieu de 55.0 quand le nombre est entier
    static String formater(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d)) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }
}


