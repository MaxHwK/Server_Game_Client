package modele.interfaceRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfacePendu extends Remote {

    public int randInt(int max) throws RemoteException;
    // Permet de generer un nombre aleatoire

    public String motAleatoire() throws RemoteException;
    // Permet de generer un mot aleatoire de la classe Dictionnaire

    public String changeMot(String mot, char[] lettres) throws RemoteException;
    // Permet de voir quelles lettres ont ete trouve ou non
    // Les lettres n'ayant pas encore ete trouve seront representees avec un _

    public boolean contientChar(char[] tab, char c) throws RemoteException;
    // Permet de savoir si un caractere est dans une table

    public char[] ajouterChar(char[] tab, char c) throws RemoteException;
    // Ajout d'un caractere a la fin d'une table
}
