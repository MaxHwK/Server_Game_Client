package modele.interfaceRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

public interface InterfaceAllumettes extends Remote {
	
	public UUID creerPartie(String mode) throws RemoteException;
	// Cree une nouvelle partie si c'est en solo sinon rejoint une partie si c'est en duo
	// Si il n'en trouve pas, il en cree une
	
	public void initialise(UUID uuid, String mode, String nomJoueur) throws RemoteException;
	// Initialisation des donnees pour debuter une partie
	
	public String getMode(UUID uuid) throws RemoteException;
	// Recupere le mode de la Partie
	
	public void rejoindrePartie(UUID uuid, String nomJoueur) throws RemoteException;
	// Ajout du Joueur dans la Partie
	
	public int getNbJoueurs(UUID uuid) throws RemoteException;
	// Recupere le Nombre de Joueurs
	
	public String getNJ1(UUID uuid) throws RemoteException;
	// Recupere l'id du Joueur 1
	
	public String getNJ2(UUID uuid) throws RemoteException;
	// Recupere l'id du Joueur 2

	public Hashtable<UUID, String> getJoueursParties() throws RemoteException;
	// Affichage de la liste des parties n'ayant qu'un joueur

	public void finPartie(UUID uuid) throws RemoteException;
	// Si il n'y a plus de Joueurs dans la Partie alors on la retire

	public String nomJoueurTour(UUID uuid) throws RemoteException;
	// Affichage du Joueur devant jouer
	
	public int getTour(UUID uuid) throws RemoteException;
	// Recupere le numero du tour

	public int maxAllumettes(UUID uuid) throws RemoteException;
	// Maximum d'allumettes pouvant etre enlevees

	public int coupIA(UUID uuid) throws RemoteException;
	// Nombre d'allumettes selectionnees par le Serveur

	public ArrayList<String> getAllRetiree(UUID uuid) throws RemoteException;
	// Recupere quelles allumettes ont ete retire

	public int getNbAllumettes(UUID uuid) throws RemoteException;
	// Recupere le nombre d'allumettes restantes

	public void action(UUID uuid, int nbAllChoisies) throws RemoteException;
	// Actualise le nombre d'allumettes restantes
	
	public void setAllRetiree(UUID uuid, ArrayList<String> tab) throws RemoteException;
	// Affichage d'une liste des allumettes retirees
	
	public int[] getTabScore(UUID uuid) throws RemoteException;
	// Recupere le Score
	
	public String nomGagnant(UUID uuid) throws RemoteException;
	// Affichage du Gagnant

	public int scoreGagnant(UUID uuid) throws RemoteException;
	// Affichage du Score du Gagnant
}
