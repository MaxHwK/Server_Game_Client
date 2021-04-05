package controleur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.interfaceRMI.InterfaceAllumettes;

public class PrincipalControleur implements Initializable {

	@FXML private VBox box_info_allumettes;
	@FXML private VBox box_info_pendu;
	@FXML private VBox box_info_tic_tac_toe;

	@FXML private TextField tf_nomJoueur;

	@FXML private Button btn_info_allumette;
	@FXML private Button btn_jouer_allumettes;

	@FXML private Button btn_info_pendu;
	@FXML private Button btn_jouer_pendu;

	@FXML private Button btn_info_ticTacToe;
	@FXML private Button btn_jouer_ticTacToe;
	
	private String hote;
	private String port;
	
	@FXML
	public void pendu() {	// Lance une partie du Pendu
		Stage nStage = new Stage();
		
		URL fxmlURL=getClass().getResource("../vue/Pendu.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
		Node root = null;
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scene scene = new Scene((AnchorPane) root, 720, 480);
		nStage.setScene(scene);
		nStage.setResizable(false);
		nStage.setTitle("Jeu du pendu");
		nStage.initModality(Modality.APPLICATION_MODAL);
		nStage.getIcons().add(new Image("/vue/icones/penduImg.jpg"));
		
		PenduControleur penduControleur = fxmlLoader.getController();
		penduControleur.setParametres(hote, port);
		penduControleur.initialise();
		
		nStage.show();
	}


	@FXML
	public void info_allumettes() {
		box_info_allumettes.setMinWidth(300);
		box_info_allumettes.setMaxWidth(350);
		box_info_allumettes.setVisible(true);

		box_info_pendu.setMinWidth(0);
		box_info_pendu.setMaxWidth(0);
		box_info_pendu.setVisible(false);

		box_info_tic_tac_toe.setMinWidth(0);
		box_info_tic_tac_toe.setMaxWidth(0);
		box_info_tic_tac_toe.setVisible(false);
	}

	@FXML
	public void info_pendu() {
		box_info_pendu.setMinWidth(300);
		box_info_pendu.setMaxWidth(350);
		box_info_pendu.setVisible(true);

		box_info_allumettes.setMinWidth(0);
		box_info_allumettes.setMaxWidth(0);
		box_info_allumettes.setVisible(false);

		box_info_tic_tac_toe.setMinWidth(0);
		box_info_tic_tac_toe.setMaxWidth(0);
		box_info_tic_tac_toe.setVisible(false);
	}

	@FXML
	public void info_ticTacToe() {
		box_info_tic_tac_toe.setMinWidth(300);
		box_info_tic_tac_toe.setMaxWidth(350);
		box_info_tic_tac_toe.setVisible(true);

		box_info_allumettes.setMinWidth(0);
		box_info_allumettes.setMaxWidth(0);
		box_info_allumettes.setVisible(false);

		box_info_pendu.setMinWidth(0);
		box_info_pendu.setMaxWidth(0);
		box_info_pendu.setVisible(false);
	}

	@FXML
	private void jouer_allumettes() {	// Lance une partie du Jeu des Allumettes
		try {
			InterfaceAllumettes obj = (InterfaceAllumettes) Naming.lookup ("rmi://" + hote + ":" + port + "/Allumettes");
			
			UUID uuid = obj.creerPartie("joueurOn");
			obj.initialise(uuid, "joueurOn", tf_nomJoueur.getText().trim());
			
			Stage nStage = new Stage();
			
			URL fxmlURL=getClass().getResource("../vue/Allumettes.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
			Node root = null;
			try {
				root = fxmlLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Scene scene = new Scene((VBox) root, 600, 400);
			nStage.setScene(scene);
			nStage.setResizable(false);
			nStage.setTitle("Jeu des allumettes");
			nStage.initModality(Modality.APPLICATION_MODAL);
			nStage.getIcons().add(new Image("/vue/icones/allumetteImg.png"));
			
			AllumetteControleur allumetteControleur = fxmlLoader.getController();
			
			allumetteControleur.setInterfaceAllumettes(obj);
			allumetteControleur.initialisation(uuid, 0);
			
			nStage.setOnCloseRequest(e -> {
				allumetteControleur.retour();
			});
			
			nStage.showAndWait();
			
			tf_nomJoueur.clear();
			
			obj.finPartie(uuid);
		
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			e1.printStackTrace();
		}
	}
	
	@FXML
	public void ticTacToe() {	// Lance une Partie de Tic Tac Toe
		Stage nStage = new Stage();
		
		URL fxmlURL=getClass().getResource("../vue/TicTacToe.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
		Node root = null;
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		Scene scene = new Scene((AnchorPane) root, 720, 480);
		nStage.setScene(scene);
		nStage.setResizable(false);
		nStage.setTitle("Jeu du Tic-Tac-Toe");
		nStage.initModality(Modality.APPLICATION_MODAL);
		nStage.getIcons().add(new Image("/vue/icones/morpionImg.jpg"));
		
		TicTacToeControleur ticTacToeControleur = fxmlLoader.getController();
		ticTacToeControleur.setParametres(hote, port);
		ticTacToeControleur.initialise();
		
		nStage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.box_info_allumettes.setVisible(false);
		this.box_info_pendu.setVisible(false);
		this.box_info_tic_tac_toe.setVisible(false);
	}
	
	public void setParameters() {
		this.hote = "127.0.0.1";
		this.port = "8000";
	}
}
