package application;

//The java.io package is used to perform input and output (I/O) in Java.
//File object to create directories, and to list down files available in a directory.
import java.io.File;
import javafx.util.Duration;

import javafx.application.Application;

import javafx.collections.FXCollections; //To populate the "Available Tracks" listview
import javafx.collections.ObservableList; //To populate the "Available Tracks" listview
import javafx.concurrent.Task;
import javafx.stage.Stage;
import javafx.scene.Scene;

//Components for layout
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

//Imports for components in this application
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

//imports for javafx media
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

//An image is required to display an icon.
import javafx.scene.image.Image;


public class MPlayr extends Application {
	
	//Visual components, which need class scope.
	
	Label lblAvailableTracks, lblSelectedTracks, lblVolume, lblSongProgress; //Creating Labels
	
	Button btnPlay, btnPause, btnStop, btnAdd, btnRemove, btnRemoveAll; //Creating Buttons
	
	Slider sldrVolume, sldrSongProgress; //Creating sliders
	
	ListView<String> lvAvailableTracks, lvSelectedTracks; //Creating ListView components
	
	Task <Void> task;
	
	MediaPlayer mp1;

	
	//To pass "musicFiles" into
	
	ObservableList<String> trackList = FXCollections.observableArrayList();
	ObservableList<String> selectedTracks = FXCollections.observableArrayList();

	
	public MPlayr() {
		
		//Instantiating the components
		//Instantiating labels
		lblAvailableTracks = new Label("Available Tracks: ");
		lblSelectedTracks = new Label("Selected Tracks: ");
		lblVolume = new Label("Volume");
		lblSongProgress = new Label();  //This will need an if else loop for song status, too.
		
		//Instantiating buttons
		btnPlay = new Button("Play");
		btnPause = new Button("Pause/Unpause");
		btnStop = new Button("Stop");
		btnAdd = new Button("Add");
		btnRemove = new Button("< Remove");
		btnRemoveAll = new Button("< < Remove All");
		
		//Instantiating sliders
		sldrVolume = new Slider();
		sldrVolume.setValue(0.5);
		sldrSongProgress = new Slider(); //This will need an if else loop for song status, too.
		sldrSongProgress.setValue(0.0);
		
		lvAvailableTracks = new ListView<>(trackList);
		lvSelectedTracks = new ListView<>(selectedTracks);
		
		/* The selectedTracks list view should have at least one song in it for the other buttons
		(asides from "Add") to be enabled */

		//Have all of these buttons disabled as default upon startup
			btnPlay.setDisable(true);
			btnPause.setDisable(true);
			btnStop.setDisable(true);
			btnRemove.setDisable(true);
			btnRemoveAll.setDisable(true);
			
		//Listener for the volume slider
		sldrVolume.valueProperty().addListener((obs, oldVal, newVal) -> {
			if(oldVal != newVal) {
				setVolume((double) newVal);
			}
        });

		/*
		mp1.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
	        if (!sldrSongProgress.isValueChanging()) {
	        	sldrSongProgress.setValue(newTime.toSeconds());
	        }
	    });
	    */
		
		
		
		//TODO Figure out button enabling/disabling DONE
		//TODO Add song progress tracker here
		//TODO Add icon
		//TODO mplayer css is not loading correctly, fix. DONE
		//TODO Remove the main class. DONE
		//TODO remove music folder from bin, make path in getMusicFiles() not fixed (relative) DONE
		//TODO Fix imports. Some unncessary
		
	}
	
	
	//Event handling
	public void init() {
		
		//Allocates actions to be carried out when the "Add" button is clicked
		btnAdd.setOnAction(ae -> {
			
				btnPlay.setDisable(false);
				btnPause.setDisable(false);
				btnStop.setDisable(false);
				btnRemove.setDisable(false);
				btnRemoveAll.setDisable(false);
			
				selectMusicFile();
			
			});
		
		
		//Allocates actions to be carried out when the "Remove" button is clicked
		btnRemove.setOnAction(ae -> removeSingleTrack()); //Invokes the removeSingleTrack() method
		
		//Allocates actions to be carried out when the "Remove All" button is clicked
		btnRemoveAll.setOnAction(ae -> {
			
			//When "Remove All" is clicked, the following buttons are disabled.
			btnPlay.setDisable(true);
			btnPause.setDisable(true);
			btnStop.setDisable(true);
			btnRemove.setDisable(true);
			btnRemoveAll.setDisable(true);
			
				//Invokes the removeAllTracks() method
				removeAllTracks();
			});
		
		//Allocates actions to be carried out when the "Play" button is clicked
		btnPlay.setOnAction(ae -> {
			
				//When the track is playing, the Play button is disabled
				btnPlay.setDisable(true);
				
				//Invokes the playTrack() method
				playTrack();
				
			});
		
		//Allocates actions to be carried out when the "Pause" button is clicked
		btnPause.setOnAction(ae -> {
				
				//Invokes the pauseTrack() method
				pauseTrack();
				
			});
		
		//Allocates actions to be carried out when the "Stop" button is clicked
		btnStop.setOnAction(ae -> {
			
				//re-enables the play button when the Stop button is clicked
				btnPlay.setDisable(false);
				
				//Invokes the stopTrack() method
				stopTrack();
				
			});
		
		
		//Adds all tracks in the local music folder to "Available Tracks"
		trackList.addAll(getMusicFiles());
		
		lvAvailableTracks.setItems(trackList);
		
	}//init()
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//Set the title;
		primaryStage.setTitle("MPlayr - for Graham's Bandcamp Library");
		
		//Set an icon
		primaryStage.getIcons().add(new Image("notMusic.jpg"));
		
		//Set the width and height;
		primaryStage.setWidth(700);
		primaryStage.setHeight(600);
		
		//Creating and instantiating the new GridPane container
		GridPane gpMain = new GridPane();
	
		//Adding padding to the GridPane elements
		gpMain.setPadding(new Insets(10));
		gpMain.setHgap(10); //10 pixel gap width between Gridpane components
		gpMain.setVgap(10); //10 pixel gap height between Gridpane components
		
		//Declaring VBoxes (to put one in each row of the GridPane)
		VBox vb1 = new VBox();
		VBox vb2 = new VBox();
		VBox vb3 = new VBox();
		
		vb1.setSpacing(10);
		vb1.setPadding(new Insets(10));
		
		vb2.setSpacing(10);
		vb2.setPadding(new Insets(10));
		
		vb3.setSpacing(10);
		vb3.setPadding(new Insets(10));
		
		//Adding components to the layout.
		//First column of GridPane:
		//Adding "Available Tracks" label
		gpMain.add(vb1, 0, 0); //Adding VBox to hold available tracks 
		
		//Second column of Gridpane:
		gpMain.add(vb2, 1, 0);
		
		//Third column of GridPane:
		gpMain.add(vb3,  2, 0);
	
		//Adding components to the VBoxes
		vb1.getChildren().add(lblAvailableTracks);//Adding "Available Tracks" label to row 0
		vb1.getChildren().add(lvAvailableTracks); //Adding "Available Tracks" ListView starting at row 1
		
		vb2.setAlignment(Pos.CENTER); //Centers vb2's content centrally on the Y-axis
		vb2.getChildren().add(btnAdd);
		vb2.getChildren().add(btnRemove);
		vb2.getChildren().add(btnRemoveAll);
		vb2.getChildren().add(btnPlay);
		vb2.getChildren().add(btnPause);
		vb2.getChildren().add(btnStop);
		vb2.getChildren().add(lblVolume);
		vb2.getChildren().add(sldrVolume);
		
		vb3.getChildren().add(lblSelectedTracks);
		vb3.getChildren().add(lvSelectedTracks);
		vb3.getChildren().add(lblSongProgress);
		vb3.getChildren().add(sldrSongProgress);
		
		//Changing the label colour:
		lblAvailableTracks.setTextFill(Color.web("#04d9ff"));
		lblSelectedTracks.setTextFill(Color.web("#04d9ff"));
		lblVolume.setTextFill(Color.web("#04d9ff"));
		lblSongProgress.setTextFill(Color.web("#04d9ff"));
		
		
		//Create a scene;
		Scene s = new Scene(gpMain);
		
		//Apply a stylesheet.
		s.getStylesheets().add("./Mplayr_style.css");

		//Set the scene;
		primaryStage.setScene(s);
		
		//Show the stage.
		primaryStage.show();

	}//start()
	
	
	//To populate the AvailableTracks listView
	public ObservableList<String> getMusicFiles() {
		
		//ObservableList for the music files.
		ObservableList<String> musicFiles = FXCollections.observableArrayList();
		
		//Creating an Array for the mp3 files.
		String [] fileList;

		// "./" means that the folder is local to the current location
		File f = new File("./music");
		
		//Call list() to get a directory listing into the string array.
		fileList = f.list();
		
		//Add the array of files to the musicFiles ObservableList.
		musicFiles.addAll(fileList);
		
		//Return the ObservableList.
		return musicFiles;		
		
	}//getMusicFiles()
	
	
	//Method to move a selected track from the "Available Tracks" list to the "Selected Tracks" list.
	public void selectMusicFile() {
		
		String objSelected = (String) lvAvailableTracks.getSelectionModel().getSelectedItem();
		
		trackList.remove(objSelected);
		selectedTracks.add(objSelected);
		
	}
	
	//Method to move a single selected track from the "Selected Tracks" list to the "Available Tracks" list.
	public void removeSingleTrack() {
		
		/*
		String objSelected = (String) lvSelectedTracks.getSelectionModel().getSelectedItem();
		
		selectedTracks.remove(objSelected);
		
		trackList.add(objSelected);
		*/
		
		final int songItem = lvSelectedTracks.getSelectionModel().getSelectedIndex();
		if (songItem != -1) {
			String addSongItem = lvSelectedTracks.getSelectionModel().getSelectedItem();
			lvSelectedTracks.getItems().remove(songItem);
			lvAvailableTracks.getItems().add(addSongItem);
		}
	}
	
	//Method to move all tracks from the "Selected Tracks" list to the "Available Tracks" list.
	public void removeAllTracks() {
		
		// When "Remove All" is clicked, the following buttons are disabled.
		btnPlay.setDisable(true);
		btnPause.setDisable(true);
		btnStop.setDisable(true);
		btnRemove.setDisable(true);
		btnRemoveAll.setDisable(true);
		
		lvSelectedTracks.getItems().clear(); //Allows the clearing of lvSelectedTracks
		trackList.clear();
		trackList.addAll(getMusicFiles());
	}
	
	//Method to play the song selected by the user when the sing is in the "Selected Tracks" list.
	public void playTrack() {
		
		//When the track is playing, the Play button is disabled
		btnPlay.setDisable(true);
		
		String songName =  "music" + File.separator + lvSelectedTracks.getSelectionModel().getSelectedItem().toString();
		System.out.println(songName);
		Media chooseAndPlay = new Media(new File(songName).toURI().toString());//Media instance
		mp1 = new MediaPlayer(chooseAndPlay); //Instantiating media player
		mp1.setVolume(0.7);
		mp1.play();
		
		System.out.println("DUR: " + mp1.getTotalDuration());
		System.out.println("CUR: " + mp1.getCurrentTime());
		
	}//playTrack()

	/* Method to pause the track. 
	Pressing "Play" after this will start the track from where it left off before "Pause" was pressed. */
	public void pauseTrack() {
		if(mp1.getStatus() == Status.PLAYING) {
			mp1.pause();
			btnPlay.setDisable(false);
		} else if(mp1.getStatus() == Status.PAUSED) {
			mp1.play();
			btnPlay.setDisable(true);
		}
	}//pauseTrack()
	
	/* Method to stop the track. 
	Unlike pause, pressing "Play" after this will start the track from the beginning. */
	public void stopTrack() {
		if(mp1.getStatus() == Status.PLAYING || mp1.getStatus() == Status.PAUSED) {
			mp1.stop();
		}
	}//stopTrack()
	
	public void setVolume(double newVal) {
		mp1.setVolume(newVal/100);
	}//setVolume()
	
	
	public void setCurrentTime() {
		
		mp1.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
			
			if (!sldrSongProgress.isValueChanging()) {
				sldrSongProgress.setValue(newTime.toSeconds());
			}
			else {
				Duration seekTo = Duration.seconds(sldrSongProgress.getValue());
				mp1.seek(seekTo);
			}
			
			double min = oldTime.toMinutes();
			double sec = oldTime.toSeconds();
			String currTimer = String.format("%02.0f:%02.0f", Math.floor(min), Math.floor(sec) % 60);
			lblSongProgress.setText(currTimer);
			
		});
	} 
	
	public static void main(String[] args) {
		launch();
	}//main()

}

