
package application;
	
import Controller.SongLibController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controller/SongLibrary.fxml"));
		
		AnchorPane root = (AnchorPane)loader.load();
	
        
		SongLibController SongLibController = loader.getController();
		SongLibController.start(primaryStage);
	
		Scene scene = new Scene(root, 850, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
       	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
