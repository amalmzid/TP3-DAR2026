package CommonPackage;

import java.io.Serializable;

// a) Classe sérialisable : ses objets peuvent être envoyés à travers le réseau
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L; // version de la classe (identique côté client et serveur)

    private double operande1;
    private char operateur;
    private double operande2;

    public Operation(double operande1, char operateur, double operande2) {
        this.operande1 = operande1;
        this.operateur = operateur;
        this.operande2 = operande2;
    }

    public double getOperande1() { return operande1; }
    public char getOperateur() { return operateur; }
    public double getOperande2() { return operande2; }
}
