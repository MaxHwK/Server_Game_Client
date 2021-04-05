package controleur;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import modele.interfaceRMI.InterfaceAllumettes;

public class AllumetteControleur {
	
	@FXML private Button btn_valider;
	@FXML private Button btn_retour;
	
	@FXML private HBox boxAllumettes;
	
	@FXML private Label lbl_scoreJ1;
	@FXML private Label lbl_j1;
	@FXML private Label lbl_scoreJ2;
	@FXML private Label lbl_j2;
	@FXML private Label lbl_tour;
	
	
	private InterfaceAllumettes iAllumettes;
	
	private UUID idPartie;
	
	private int nbAllChoisies = 0;
	private int numJoueur;
	
	private ArrayList<String> tabAllRetirerStr = new ArrayList<String>();
	private ArrayList<Node> tabAllRetirer = new ArrayList<Node>();
	
	private Thread tVictoire;
	
	private boolean finTour = false;

	public void initialisation(UUID uuid, int joueur) {		// Permet d'enregistrer l'id de la partie
		try {
			this.idPartie = uuid;
			this.numJoueur = joueur;

			lbl_j1.setText(iAllumettes.getNJ1(uuid) + " ");
			afficheAllumettes(iAllumettes.getNbAllumettes(uuid));
			affPremierJoueur(iAllumettes.nomJoueurTour(uuid));
				
			tVictoire = threadVictoire();
			tVictoire.start();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private Thread threadVictoire() {	// Tant que le jeu n'est pas fini, relance un tour
		return new Thread(() -> {
			try {
				while (iAllumettes.getNbAllumettes(idPartie) != 0) {
					finTour = false;
					tour();
					
					while (!finTour) {
						if (iAllumettes.getNbAllumettes(idPartie) == 0) {
							finTour = true;
							break;
						}
						Thread.sleep(500);
					}
				}
				
				Platform.runLater(() -> {
					try {
						finPartie(iAllumettes.nomGagnant(idPartie), iAllumettes.scoreGagnant(idPartie));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				});
				
			} catch (RemoteException | InterruptedException e) {
				System.out.println("Sleep interrupted, Fin de partie");
			}
		});
	}
	
	private void tour() {		// Execution d'un tour
		new Thread(() -> {
			try {
				Platform.runLater(() -> {
					try {
						affTourJoueur(iAllumettes.nomJoueurTour(this.idPartie));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				});

				if ( iAllumettes.getTour(idPartie)%2 == 1 && iAllumettes.getMode(idPartie).equals("JoueurOn")) {
			
					affichageAttente(true);
					Thread.sleep(800);
					this.nbAllChoisies = iAllumettes.coupIA(this.idPartie);
					tourIA(this.nbAllChoisies);		// Selection des allumettes disponibles par le serveur		
					affichageAttente(false);
			
					Platform.runLater(() -> {	// Permet d'exceuter une fonction dans un autre thread
							valider();
					});
				}
			} catch (RemoteException | InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	private void affichageAttente(boolean bool) {
		this.btn_valider.setDisable(bool);
		this.boxAllumettes.setDisable(bool);
	}
	
	public void setInterfaceAllumettes(InterfaceAllumettes interfaceAllumettes) {
		this.iAllumettes = interfaceAllumettes;
	}

	private void afficheAllumettes(int nbAllumette) {	// Creation d'un nombre d'allumettes
		
		for (int i=1; i<=nbAllumette; i++) {
			String id = "baton".concat(String.valueOf(i));
			
			Button button = new Button();
			button.setId(id);
			button.setOnAction(a -> {
				choixAllumette(a);
			});
			button.setMinWidth(10);
			button.setMaxWidth(10);
			button.setMinHeight(180);
			button.setMaxHeight(180);
			boxAllumettes.getChildren().add(button);
		}
		btn_valider.setDisable(true);
	}
	
	private void affPremierJoueur(String nomPJoueur) {		// Informe le premier joueur à jouer
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Premier joueur");
		alert.setHeaderText(null);
		alert.setContentText(nomPJoueur + " est le premier joueur a jouer");
		alert.showAndWait();
	}
	
	private void tourIA(int nbAlluChoisies) {	// Liste des allumettes non cachees
		
		ObservableList<Node> alluVisibles = boxAllumettes.getChildren().filtered(t->t.isVisible());
		
		for (int i=0; i<nbAlluChoisies; i++)
			tabAllRetirer.add(alluVisibles.get(i));
	}
	
	private void affTabScore(int[] tabScore) {		// Affichage des Scores
		lbl_scoreJ1.setText(String.valueOf(tabScore[0]));
		lbl_scoreJ2.setText(String.valueOf(tabScore[1]));
	}

	private void affTourJoueur(String nomJoueur) {	// Affichage du Joueur devant jouer
		lbl_tour.setText(nomJoueur);
	}
	
	@FXML
	private void choixAllumette(ActionEvent event) {	// Recupere le choix
		
		final Node source = (Node) event.getSource();
	
		if (source.getStyle().equals("") && nbAllChoisies != 2) {
			tabAllRetirer.add(source);
			nbAllChoisies += 1;
			source.setStyle("-fx-background-color: #3d6ca4;");
		}
		else if (!source.getStyle().equals("")) {
			tabAllRetirer.remove(source);
			nbAllChoisies -= 1;
			source.setStyle("");
		}
		
		if ( nbAllChoisies == 0 )
			btn_valider.setDisable(true);
		else
			btn_valider.setDisable(false);
	}
	
	@FXML
	private void valider() {	// Actualisation du score et retire les allumettes aillant ete prises
		try {
			iAllumettes.action(this.idPartie, this.nbAllChoisies);	// Enregistrement sur le Serveur

			affTabScore(iAllumettes.getTabScore(idPartie));
			
			for (Node node : tabAllRetirer) {
				tabAllRetirerStr.add(node.getId());
				node.setVisible(false);
			}
			tabAllRetirer.clear();
			
			iAllumettes.setAllRetiree(idPartie, tabAllRetirerStr);
			
			nbAllChoisies = 0;
			
			finTour = true;
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void retour() {		// Ferme le Jeu
		if (tVictoire != null)
			tVictoire.interrupt();
		
	    Stage stage = (Stage) btn_retour.getScene().getWindow();
	    stage.close();
	}
	
	private void finPartie(String nomGagnant, int scoreGagnant) {	// Affichage du Message de fin de Partie
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Fin de partie");
		alert.setHeaderText(null);
		alert.setContentText("Fin de la partie ! Le gagnant est " + nomGagnant + " avec un score de " + scoreGagnant );
		
		alert.showAndWait();

		Stage stage = (Stage) btn_retour.getScene().getWindow();
	    stage.close();
	}

}
