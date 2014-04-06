/**
 * 
 */
package de.fsch.ibot.ui.renderers.javafx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * @author fsch
 *
 */
public class FXApplication extends Application
{

	/**
	 * 
	 */
	public FXApplication()
	{
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
        primaryStage.setTitle("example Gui");
        
        //The BorderPane has the same areas laid out as the
        //BorderLayout layout manager
        BorderPane componentLayout = new BorderPane();
        componentLayout.setPadding(new Insets(20,0,20,20));
        
        //The FlowPane is a conatiner that uses a flow layout
        final FlowPane choicePane = new FlowPane();
        choicePane.setHgap(100);
        Label choiceLbl = new Label("Fruits");
        
        //The choicebox is populated from an observableArrayList
        ChoiceBox fruits = new ChoiceBox(FXCollections.observableArrayList("Asparagus", "Beans", "Broccoli", "Cabbage"
         , "Carrot", "Celery", "Cucumber", "Leek", "Mushroom"
         , "Pepper", "Radish", "Shallot", "Spinach", "Swede"
         , "Turnip"));
        
        //Add the label and choicebox to the flowpane
        choicePane.getChildren().add(choiceLbl);
        choicePane.getChildren().add(fruits);
        
        //put the flowpane in the top area of the BorderPane
        componentLayout.setTop(choicePane);
        
        final FlowPane listPane = new FlowPane();
        listPane.setHgap(100);
        Label listLbl = new Label("Vegetables");
        
        ListView vegetables = new ListView(FXCollections.observableArrayList("Apple", "Apricot", "Banana"
         ,"Cherry", "Date", "Kiwi", "Orange", "Pear", "Strawberry"));
        listPane.getChildren().add(listLbl);
        listPane.getChildren().add(vegetables);
        listPane.setVisible(false);
        
        componentLayout.setCenter(listPane);
        
        //The button uses an inner class to handle the button click event
        Button vegFruitBut = new Button("Fruit or Veg");
        vegFruitBut.setOnAction(new EventHandler() {

            public void handle(ActionEvent event) {
                //switch the visibility for each FlowPane
                choicePane.setVisible(!choicePane.isVisible());
                listPane.setVisible(!listPane.isVisible());
            }

			@Override
			public void handle(Event event)
			{
				// TODO Auto-generated method stub
				
			}
        });
        
        componentLayout.setBottom(vegFruitBut);
        
        //Add the BorderPane to the Scene
        Scene appScene = new Scene(componentLayout,500,500);
        
        //Add the Scene to the Stage
        primaryStage.setScene(appScene);
        primaryStage.show();

	}

}
