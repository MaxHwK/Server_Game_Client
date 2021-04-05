package client;

import java.net.URL;
import java.rmi.Naming;

import controleur.PrincipalControleur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Client extends Application {

    public void start(Stage primaryStage) {
        try {
            URL fxmlURL=getClass().getResource("../vue/Menu.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            Node root = fxmlLoader.load();
            Scene scene = new Scene((HBox) root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image("/vue/icones/mainImg.png"));
            primaryStage.setTitle("Menu");
            primaryStage.setResizable(false);
            
            primaryStage.show();
            
            PrincipalControleur pControleur = fxmlLoader.getController();
            pControleur.setParameters();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String hote = "127.0.0.1";
            String port = "8000";

            Naming.lookup("rmi://" + hote + ":" + port + "/Pendu");
            Naming.lookup("rmi://" + hote + ":" + port + "/Allumettes");
            Naming.lookup("rmi://" + hote + ":" + port + "/TicTacToe");
            
            launch(args);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
