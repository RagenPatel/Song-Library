
package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SongLibController {
	// calls/initializes listview from FXML
	@FXML
	ListView<String> listView;

	// calls/initializes FXML text fields: textfield1 = name, textfield2 =
	// Artist, textfield3 = Album, textField4 = Year
	@FXML
	private TextField textField1, textField2, textField3, textField4;

	// calls/initializes FXML button fields
	@FXML
	private Button addButton, editButton, doneButton, deleteButton;

	@FXML
	private Label detailsLabel;

	// Node class to hold all values of one song
	public class Node {
		String name, artist, album, year;
		Node next;

		public Node() {
			this.next = null;
		}

		public Node(String name, String artist, String album, String year) {
			this.name = name;
			this.artist = artist;
			this.album = album;
			this.year = year;
			this.next = next;
		}
	}

	public Node head;

	private ObservableList<String> obsList;

	// Set initial conditions
	// Load in values from a file if the program already had values from
	// previous sessions
	public void start(Stage mainStage) {
		// initialize list that will hold all the song names
		obsList = FXCollections.observableArrayList();

		// initialize node for holding values of songs
		Node list = new Node();
		Node ptr = list;

		// Load the song list from file songlist.txt
		File file = new File("songlist.txt");
		try {
			Scanner sc = new Scanner(file);
			int i = 0;
			while (sc.hasNextLine()) {
				// Necessary values for this loop
				String name = "", artist = "", album = "", year = "";

				for (int j = 0; j < 4; j++) {
					if (sc.hasNext()) {
						if (j == 0) {
							name = sc.useDelimiter("`").next();
							list.name = name;
						} else if (j == 1) {
							artist = sc.useDelimiter("`").next();
							list.artist = artist;
						} else if (j == 2) {
							album = sc.useDelimiter("`").next();
							list.album = album;
						} else if (j == 3) {
							year = sc.useDelimiter("`").next();
							list.year = year;
						}
					}
				}
				// if(sc.equals("\n`")){
				list.next = new Node();
				list = list.next;
				// }
				i++;
				System.out.println("line # " + i);
			}

			head = ptr;

			while (ptr.next != null) {
				obsList.add(ptr.name);
				ptr = ptr.next;
			}

			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		file.delete();
		createFile();

		/*
		 * System.out.println("while loop"); // Add stuff to list with first one
		 * selected while (ptr.next != null) { if (ptr.next.next.album != null)
		 * { if (ptr.next.next.next.year != null) { obsList.add(ptr.name + ", "
		 * + ptr.next.artist + ", " + ptr.next.next.album + ", " +
		 * ptr.next.next.next.year); } else { obsList.add(ptr.name + ", " +
		 * ptr.next.artist + ", " + ptr.next.next.album); } } else if
		 * (ptr.next.next.next.year != null) { obsList.add(ptr.name + ", " +
		 * ptr.next.artist + ", " + ptr.next.next.next.year); } else {
		 * obsList.add(ptr.name + ", " + ptr.next.artist); } ptr =
		 * ptr.next.next.next.next; }
		 * 
		 * System.out.println("End of while loop");
		 */

		// set the list to show the items in the obsList
		listView.setItems(obsList);

		// initialize values of the buttons so only the required buttons are
		// enabled
		if (obsList.isEmpty()) {
			addButton.setDisable(false);
			editButton.setDisable(true);
			doneButton.setDisable(true);
			deleteButton.setDisable(true);
		} else {
			listView.getSelectionModel().select(0);
			handleMouseClickEvent(0);
			addButton.setDefaultButton(false);
			editButton.setDisable(false);
			deleteButton.setDisable(false);
			doneButton.setDisable(true);
		}

		// Testing out how to add items to list
		/*
		 * obsList.add("Hello"); listView.setItems(obsList);
		 */

		// Listeners for add button. TextField1 = name, TextField2 = Artist
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Are you sure?");
				alert.setContentText("Are you sure you want to add this song to the list?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
				    // ... user chose YES
					handleTextFieldEvent();
					
				} else {
				    // ... user chose CANCEL or closed the dialog
					System.out.print("Canceled add changes.");
				}
				
				
				textField1.replaceText(0, textField1.getText().length(), "");
				textField2.replaceText(0, textField2.getText().length(), "");
				textField3.replaceText(0, textField3.getText().length(), "");
				textField4.replaceText(0, textField4.getText().length(), "");
			}
		});

		editButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				addButton.setDisable(true);
				deleteButton.setDisable(true);
				doneButton.setDisable(false);
				editButton.setDisable(true);

				handleEditEvent(listView.getSelectionModel().getSelectedIndex());

			}
		});

		doneButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Are you sure?");
				alert.setContentText("Are you sure you want to make the changes?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
				    // ... user chose YES
					handleDoneEvent(listView.getSelectionModel().getSelectedIndex());
					
				} else {
				    // ... user chose CANCEL or closed the dialog
					System.out.print("Canceled changes.");
				}
				
				addButton.setDisable(false);
				deleteButton.setDisable(false);
				doneButton.setDisable(true);
				editButton.setDisable(false);
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Are you sure?");
				alert.setContentText("Are you sure you want to delete this item?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
				    // ... user chose YES
					handleDeleteEvent(listView.getSelectionModel().getSelectedIndex());
					
				} else {
				    // ... user chose CANCEL or closed the dialog
					System.out.print("Canceled changes.");
				}
				
			}

		});

		// ListView onClick action
		listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				handleMouseClickEvent(listView.getSelectionModel().getSelectedIndex());
			}
		});

	}

	/*
	 * When mouse click occurs, it makes that list value display the appropriate
	 * values from the file and removes previous one Initially,
	 * previousMouseClick = -100 so you dont have to update random obs lists.
	 * 
	 * This should take listSelect value. Find the line in the file and update
	 * the selected list to show appropriate details.
	 * 
	 * AND it should replace the previous list value to only show the name.
	 */
	private void handleMouseClickEvent(int listSelect) {

		if (obsList.size() > 0) {
			try {
				File file = new File("songlist.txt");
				Scanner sc = new Scanner(file);

				while (sc.hasNext()) {
					String test = sc.next();
					System.out.println("test: " + test);
					test = test.substring(1, test.length());
					String name = test.substring(0, test.indexOf('`'));

					if (obsList.get(listSelect).equals(name)) {
						String testTemp = test.toString();
						String temp = test;
						int artist;
						temp = temp.substring(temp.indexOf('`') + 1); // `Name`Artist`Album`Year
																		// This
																		// line
																		// gets
																		// rid
																		// of
																		// name
						artist = temp.indexOf('`');
						String artistName = temp.substring(0, temp.indexOf('`'));

						temp = temp.substring(temp.indexOf('`') + 1); // Artist`Album`Year
																		// This
																		// line
																		// gets
																		// rid
																		// of
																		// artist
																		// temp
																		// =
																		// Album`Year

						System.out.println("Start index: " + (testTemp.indexOf('`') + 1) + " end: "
								+ (artist + name.length() + 1));
						System.out.println("Lengtho of testTemp: " + testTemp.length());
						String answer = "Name: "+ name + ", Artist: "
								+ testTemp.substring(testTemp.indexOf('`') + 1, artist + name.length() + 1); // name
																												// and
																												// artist
																												// set

						// Check if the Album part is empty or not
						if (!temp.substring(0, temp.indexOf('`')).trim().isEmpty()) {
							answer += ", Album: " + temp.substring(0, temp.indexOf('`'));
						}

						// set temp to the remaining string (in this case, just
						// Year)
						temp = temp.substring(temp.indexOf('`') + 1);

						// Check if there is data for the Year portion
						if (!temp.trim().isEmpty()) {
							answer += ", Year: " + temp;
						}

						// Checked for all the required data. Now, add it to
						// list
						System.out.println("answer: " + answer);
						detailsLabel.setText(answer);

						return;
						/*
						 * obsList.get(listSelect).replace(name, answer);
						 * System.out.println("Answer: "+ answer);
						 * listView.setItems(obsList);
						 */
					}

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Nothing in the list to click");
			detailsLabel.setText("");
		}

	}

	// Handles the event for adding stuff into the list
	private void handleTextFieldEvent() {
		if (textField1.getText().trim().isEmpty() || textField2.getText().trim().isEmpty()) {
			System.out.println("Error. Required to enter at least Name of song and Artist");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Required Fields Missing");
			alert.setHeaderText(null);
			alert.setContentText("You must at least have the name of the song and the Artist name in the text fields.");
			alert.showAndWait();
		} else {

			// creates Node for song information

			Node tempSong = new Node(textField1.getText(), textField2.getText(), textField3.getText(),
					textField4.getText());

			// checks if a list exists yet
			if (head == null) {

				head = tempSong;
				obsList.add(tempSong.name);
				listView.setItems(obsList);
				addButton.setDefaultButton(false);
				editButton.setDisable(false);
				deleteButton.setDisable(false);
				doneButton.setDisable(true);
			} else {

				Node tmp = head;
				int i = 0;
				int size = obsList.size();

				System.out.println("Obs list 1: " + obsList.get(0));

				obsList.clear();

				// checks for list with only one song in it
				if (size == 1) {
					if ((((tempSong.name).toLowerCase()).equals((tmp.name).toLowerCase()))
							&& (((tempSong.artist.toLowerCase())).equals((tmp.artist).toLowerCase()))) {

						obsList.add(tmp.name);

						Alert alert = new Alert(AlertType.ERROR); // checks if
																	// song is a
																	// duplicate
						alert.setTitle("Duplicate Song");
						alert.setHeaderText(null);
						alert.setContentText("Duplicate name of the song.");
						alert.showAndWait();
					} else {
						if (((tempSong.name).toLowerCase()).compareTo((tmp.name).toLowerCase()) < 0) { // if
																										// insert
																										// comes
																										// before
																										// the
																										// song
																										// alphabetically
							obsList.add(tempSong.name);
							tempSong.next = head;
							head = tempSong;
							obsList.add(tmp.name);
						} else { // if insert comes after the song
							obsList.add(tmp.name);
							obsList.add(tempSong.name);
							tmp.next = tempSong;
						}
					}
				} else {

					Node prev = head;
					// checks if song already exists in the list
					while (i < size) {
						if ((((tempSong.name).toLowerCase()).equals((tmp.name).toLowerCase()))
								&& (((tempSong.artist.toLowerCase())).equals((tmp.artist).toLowerCase()))) {

							while (i < size) {

								obsList.add(tmp.name); // recreates original
														// list

								i++;
								if (i < size) {
									tmp = tmp.next;
								}

							}

							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Duplicate Song");
							alert.setHeaderText(null);
							alert.setContentText("Duplicate name of the song.");
							alert.showAndWait();

							break;
						} else {
							if (((tempSong.name).toLowerCase()).compareTo((tmp.name).toLowerCase()) < 0) { // compares
																											// inserted
																											// song
																											// if
																											// it
																											// comes
																											// before
																											// song
																											// on
																											// the
																											// list
								if (tmp == head) {
									obsList.add(tempSong.name);
									tempSong.next = head;
									head = tempSong;
								} else {

									obsList.add(tempSong.name);
									tempSong.next = tmp;
									prev.next = tempSong;
								}
								tmp = tempSong.next; // recreating rest of the
														// list that was already
														// sorted
								while (i < size) {
									obsList.add(tmp.name);

									i++;
									if (i < size) {
										tmp = tmp.next;
									}
								}
								break;

							}
						}

						// inserting a song that is currently lower
						// alphabetically in the list compared to tmp.name

						obsList.add(tmp.name); // recreating sorted list from
												// stored song nodes
						i++;
						if (i < size) { // avoids null pointer
							prev = tmp;
							tmp = tmp.next;
						} else {
							obsList.add(tempSong.name);
							tmp.next = tempSong;
						}
					}
				}

				listView.setItems(obsList);
				addButton.setDefaultButton(false);
				editButton.setDisable(false);
				deleteButton.setDisable(false);
				doneButton.setDisable(true);
				
				textField1.replaceText(0, textField1.getText().length(), "");
				textField2.replaceText(0, textField2.getText().length(), "");
				textField3.replaceText(0, textField3.getText().length(), "");
				textField4.replaceText(0, textField4.getText().length(), "");

			}

		}

		createFile(); // recreates songlist.txt

		listView.getSelectionModel().select(obsList.size() - 1);
		handleMouseClickEvent(obsList.size() - 1);
	}

	// Handles event when Done is pressed
	private void handleEditEvent(int listSelect) {

		//Disable clicking on other items in list while editing
		listView.setMouseTransparent(true);
		
		
		Node temp = head;
		// finds where the edit is taking place on the list

		for (int i = 0; i < listSelect; i++) {
			temp = temp.next;
		}

		
		textField1.replaceText(0, textField1.getText().length(), temp.name);
		textField2.replaceText(0, textField2.getText().length(), temp.artist);
		textField3.replaceText(0, textField3.getText().length(), temp.album);
		textField4.replaceText(0, textField4.getText().length(), temp.year);

		// SET other text fields to their appropriate
		// values************************************************************

	}

	// handles Delete event
	private void handleDeleteEvent(int listSelect) {
		int selection = listSelect;

		if (obsList.size() == 1) { // if only one song in list, sets head to
									// null
			head = null;
		} else {
			if (selection == 0) { // if deleting the head, makes head = next
									// song in list
				head = head.next;
			} else { // if deleting other song from the list
				Node temp = head;
				for (int i = 0; i < selection - 1; i++) {
					temp = temp.next; // removes listSelect Node from the list
				}
				if (temp.next.next != null) {
					temp.next = temp.next.next;
				} else {
					temp.next = null;
				}
			}
		}

		obsList.remove(selection);

		listView.getSelectionModel().select(obsList.size() - 1);
		handleMouseClickEvent(obsList.size() - 1);

		createFile(); // recreates songlist.txt

		if (obsList.size() == 0) {

			addButton.setDisable(false);
			editButton.setDisable(true);
			doneButton.setDisable(true);
			deleteButton.setDisable(true);
		}

	}

	// handles event when Done is pressed
	private void handleDoneEvent(int listSelect) {

		
		if (textField1.getText().trim().isEmpty() || textField2.getText().trim().isEmpty()) { // check
																								// if
																								// empty
																								// textfields
			System.out.println("Error. Required to enter at least Name of song and Artist");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Required Fields Missing");
			alert.setHeaderText(null);
			alert.setContentText("You must at least have the name of the song and the Artist name in the text fields.");
			alert.showAndWait();
		}

		
		//Enable list view click
		listView.setMouseTransparent(false);
		
		
		Node temp = head;

		if (obsList.size() - 1 == 0) { // only song in list, nothing else to
										// sort
			temp.name = textField1.getText();
			temp.artist = textField2.getText();
			temp.album = textField3.getText();
			temp.year = textField4.getText();
			obsList.clear();
			obsList.add(temp.name);

			listView.getSelectionModel().select(obsList.size() - 1);

		}

		for (int i = 0; i < listSelect; i++) {
			temp = temp.next;

		}

		if ((textField1.getText()).equals(temp.name)) { // user isn't changing
														// the name of the song
														// (no need to sort)
			temp.artist = textField2.getText();
			temp.album = textField3.getText();
			temp.year = textField4.getText();
		} else { // user changed the name of song (sorting required)
			handleDeleteEvent(listSelect);
			handleTextFieldEvent(); // deletes then re-adds song to the list
		}

		createFile(); // recreates songlist.txt
		
		listView.getSelectionModel().select(listSelect);
		
		//update label with new info
		handleMouseClickEvent(listSelect);
		
		textField1.replaceText(0, textField1.getText().length(), "");
		textField2.replaceText(0, textField2.getText().length(), "");
		textField3.replaceText(0, textField3.getText().length(), "");
		textField4.replaceText(0, textField4.getText().length(), "");

	}

	private void createFile() { // will store an updating song list in
								// songlist.txt file
		File old = new File("songlist.txt");
		old.delete();
		File sList = new File("songlist.txt");

		Node temp = head;
		if (head == null) {
			sList.delete();
			return;
		}
		try {
			FileWriter sW = new FileWriter(sList, false);

			while (temp.next != null) {
				sW.write("`" + temp.name + "`" + temp.artist + "`" + temp.album + "`" + temp.year + "\n"); // writes
																											// the
																											// song
																											// node
																											// to
																											// the
																											// file

				temp = temp.next;
			}
			if(temp.name != null){
				sW.write("`" + temp.name + "`" + temp.artist + "`" + temp.album + "`" + temp.year + "\n");
			}
			sW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

}