package ClientPackage;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client3_1 {
    static final int PORT = 1234;
    // Mettre ici l'adresse IP du serveur ("localhost" en local, ex: "192.168.1.10" en réseau local)
    static final String IP_SERVEUR = "192.168.0.122";
    // nombre ESPACE opérateur(+ - * /) ESPACE nombre   ex : 55 * 25
    // \\s+ oblige au moins un espace autour de l'opérateur (55*25 est refusé)
    static final Pattern PATTERN = Pattern.compile(
            "^\\s*(-?\\d+(?:\\.\\d+)?)\\s+([+\\-*/])\\s+(-?\\d+(?:\\.\\d+)?)\\s*$");

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);

            // 3) Connexion au serveur via InetAddress + InetSocketAddress
            InetAddress adresseServeur = InetAddress.getByName(IP_SERVEUR);
            InetSocketAddress adresseSocket = new InetSocketAddress(adresseServeur, PORT);

            Socket socket = new Socket();
            socket.connect(adresseSocket);
            System.out.println("Je suis un client connecté au serveur " + socket.getRemoteSocketAddress());

            // Flux de communication puis flux de traitement (texte)
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            // c) Lecture du numéro d'ordre envoyé par le serveur
            System.out.println(br.readLine());

            while (true) {
                System.out.print("Entrez une opération (ex: 55 * 25) ou 'quit' : ");
                String saisie = sc.nextLine().trim();
                if (saisie.equalsIgnoreCase("quit")) {
                    break;
                }

                // Validation de la syntaxe avant l'envoi
                Matcher m = PATTERN.matcher(saisie);
                if (!m.matches()) {
                    System.out.println("Syntaxe invalide , rien n'est envoyé.");
                    continue;
                }

                // a) Envoi des trois éléments, un par ligne
                pw.println(m.group(1)); // premier opérande
                pw.println(m.group(2)); // opérateur
                pw.println(m.group(3)); // deuxième opérande

                // d) Lecture et affichage du résultat
                String reponse = br.readLine();
                System.out.println("Résultat : " + reponse);
            }

            pw.println("quit"); // prévenir le serveur
            System.out.println("Fin de la communication");
            sc.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Erreur côté client : " + e.getMessage());
        }
    }
}
