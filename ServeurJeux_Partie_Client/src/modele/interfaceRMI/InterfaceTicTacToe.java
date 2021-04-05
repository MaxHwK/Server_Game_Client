package modele.interfaceRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceTicTacToe extends Remote{
    
    public String[][] initialiserCoord(int size) throws Exception;
    // Initialisation de la grille pour pouvoir jouer

    public boolean verifierTour(int x, int y, String[][] coord, String symbole) throws RemoteException;

    public String[][] remplirCase(int x, int y, int joueur, String[][] coord) throws Exception;

    public boolean verifierCase(int x, int y, String[][] coord) throws RemoteException;

    public boolean verifierLignes(int x, String[][] coord, String symbole) throws RemoteException;

    public boolean verifierColones(int y, String[][] coord, String symbole) throws RemoteException;

    public boolean verifierDiagonales(String[][] coord, String symbole) throws RemoteException;

    public boolean verifierDiagonale1(String[][] coord, String symbole) throws RemoteException;

    public boolean verifierDiagonale2(String[][] coord, String symbole) throws RemoteException;

    public boolean grilleRemplie(String[][] coord) throws RemoteException;
    // Permet de remplir la bonne case avec le bon symbole (Rond / Croix) pour le bon joueur
}
