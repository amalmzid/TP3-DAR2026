package ServerPackage;

import java.io.*;
import java.net.Socket;

public class ClientProcess3_1 extends Thread{
    private Socket socket;
    private int numero;
    public ClientProcess3_1(Socket socket,int numero){
        this.socket=socket;
        this.numero=numero;
    }
    public void run(){
        try {
            System.out.println("le client numero "+numero+" est connecté");
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true); // true : autoFlush (envoi immédiat à chaque println)
            // c) envoi au client de son numéro d'ordre de connexion
            pw.println("Bienvenue, vous êtes le client n°" + numero);
            String operande1 = br.readLine();
            while (operande1 != null && !operande1.equals("quit")) {
                String operateur = br.readLine();
                String operande2 = br.readLine();
                System.out.println("Opération reçue de client numero "+numero+" : " + operande1 + " " + operateur + " " + operande2);
                String reponse;
                try {
                    reponse = calculer(Double.parseDouble(operande1), operateur,
                            Double.parseDouble(operande2));
                } catch (NumberFormatException e) {
                    reponse = "Erreur : opérande invalide";
                }
                pw.println(reponse);
                System.out.println("Réponse envoyée : " + reponse +"au client numero "+numero);
                operande1 = br.readLine(); // opération suivante ou "quit"
            }
            System.out.println("le client numero "+numero+" est déconnecté");
            socket.close();

        } catch (IOException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }
    }

    // Calcule l'opération et renvoie le résultat sous forme de texte
    static String calculer(double a, String operateur, double b) {
        double r;
        switch (operateur) {
            case "+": r = a + b; break;
            case "-": r = a - b; break;
            case "*": r = a * b; break;
            case "/":
                if (b == 0) {
                    return "Erreur : division par zéro impossible";
                }
                r = a / b;
                break;
            default:
                return "Erreur : opérateur inconnu " + operateur;
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

