import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javax.swing.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("static-access")
public class GUI extends Application {
	
	public static void main(String [] args)
	{
		launch(args);
	}
	Label rulesLabel;
	Label passwordLabel;
	Label retypeLabel;
	Label retypePasswordLabel;
	
	TextField passwordTextField;
	TextField retypeTextField;
	
	Button checkPasswordButton;
	Button checkPasswordInFileButton;
	Button exitButton;
	
	PasswordCheckerUtility check = new PasswordCheckerUtility();

	@Override
	public void start(Stage stage) throws Exception {
		
		rulesLabel = new Label ("Use the following rules when creating your passwords:\n"
								+ "\t1. Length must be greater than 6; a strong password will contain at least 10 characters\n"
								+ "\t2. Must contain at least one upper case alpha character\n"
								+ "\t3. Must contain at least one lower case alpha character\n"
								+ "\t4. Must contain at least one numeric character\n"
								+ "\t5. May not have more than 2 of the same character in sequence\n");
		passwordLabel = new Label("Password");
		retypeLabel = new Label ("Re-type");
		retypePasswordLabel = new Label("Password");
		
		passwordTextField = new TextField();
		retypeTextField = new TextField();
		
		HBox passwordInput = new HBox(10);
		passwordInput.setAlignment(Pos.CENTER);
		passwordInput.getChildren().addAll(passwordLabel,passwordTextField);
		
		VBox ReTypePasswordInput = new VBox(5);
		ReTypePasswordInput.setPadding(new Insets(5,0,10,0));
		ReTypePasswordInput.getChildren().addAll(retypeLabel,retypePasswordLabel);
		
		HBox retypePasswordInput = new HBox(10);
		retypePasswordInput.setAlignment(Pos.CENTER);
		retypePasswordInput.getChildren().addAll(ReTypePasswordInput,retypeTextField);
		
		VBox passwordPane = new VBox(10);
		passwordPane.setPadding(new Insets(20,0,40,0));
		passwordPane.setAlignment(Pos.CENTER);
		passwordPane.getChildren().addAll(passwordInput,retypePasswordInput);
		
		
		checkPasswordButton = new Button ("Check Password");
		checkPasswordInFileButton = new Button ("Check Passwords in File");
		exitButton = new Button ("Exit");
		
		checkPasswordButton.setPadding(new Insets(5,20,5,20));
		checkPasswordInFileButton.setPadding(new Insets(5,20,5,20));
		exitButton.setPadding(new Insets(5,20,5,20));
	
		//Set Mnemonic 
		checkPasswordButton = new Button("_Check Password");
		checkPasswordButton.setMnemonicParsing(true); 
		checkPasswordInFileButton = new Button("Check Password in _File");
		checkPasswordInFileButton.setMnemonicParsing(true);
		exitButton = new Button("_Exit");
		exitButton.setMnemonicParsing(true);
		
		//Add Tooltips
		checkPasswordButton.setTooltip(new Tooltip("Click here to check your password"));
		checkPasswordInFileButton.setTooltip(new Tooltip("Click here to check the passwords in File"));
		exitButton.setTooltip(new Tooltip("Click here to exit"));
		
		HBox buttonPane = new HBox(20);
		buttonPane.setAlignment(Pos.BOTTOM_CENTER);
		buttonPane.getChildren().addAll(checkPasswordButton,checkPasswordInFileButton,exitButton);
		
		VBox mainPane = new VBox(30);
		mainPane.setPadding(new Insets(10,50,10,50));
		mainPane.setAlignment(Pos.CENTER);
		mainPane.getChildren().addAll(rulesLabel,passwordPane,buttonPane);
		
		// Sets event handlers 
	    checkPasswordButton.setOnAction(new CheckPasswordButtonEventHandler());
	    checkPasswordInFileButton.setOnAction(new CheckFilePasswordsButtonEventHandler());
	    exitButton.setOnAction(new ExitButtonEventHandler());
	      
		
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(mainPane);
		
		Scene scene = new Scene(borderPane);
		stage.setTitle("Password Checker");
		stage.setScene(scene);
		stage.show();
		
	}
	
	  class CheckPasswordButtonEventHandler implements EventHandler<ActionEvent>
	  {
	       @Override
	       public void handle(ActionEvent event)
	       {
	           String password = null;
	           String retypedPassword = null;
	          
	           password = passwordTextField.getText().trim();
	           retypedPassword = retypeTextField.getText().trim();
	          
	           try
	           {
	               // If the password and the re-typed password are not the same, the UnmatchedException get thrown.
	               if(!password.equals(retypedPassword))
	               {                  
	                   UnmatchedException exception = new UnmatchedException ("The passwords do not match");
	                   throw exception;
	               }
	    
	               check.isValidPassword(password);
	              
	               // If no exception is thrown,so display to user that password is valid.
	               JOptionPane.showMessageDialog(null, "Password is valid", "Password Status", JOptionPane.INFORMATION_MESSAGE);      
	           }
	           catch (UnmatchedException exception)
	           {
	               JOptionPane.showMessageDialog(null, "The passwords do not match.", "Password Status", JOptionPane.INFORMATION_MESSAGE);
	               exception.printStackTrace();
	           }
	           catch (LengthException e) {
	               JOptionPane.showMessageDialog(null, "The password must be at least 6 characters long.", "Password Error", JOptionPane.INFORMATION_MESSAGE);
	               e.printStackTrace();
	           }
	           catch (NoDigitException e) {
	               JOptionPane.showMessageDialog(null, "The password must contain at least one digit.","Password Error", JOptionPane.INFORMATION_MESSAGE);
	               e.printStackTrace();
	           }
	           catch (NoUpperAlphaException e) {
	               JOptionPane.showMessageDialog(null, "The password must contain at least one uppercase alphabetic character.", "Password Error", JOptionPane.INFORMATION_MESSAGE);
	               e.printStackTrace();
	           }
	           catch (NoLowerAlphaException e) {
	               JOptionPane.showMessageDialog(null, "The password must contain at least one lowercase alphabetic character.","Password Error", JOptionPane.INFORMATION_MESSAGE);
	               e.printStackTrace();
	           }
	           catch (InvalidSequenceException e) {
	               JOptionPane.showMessageDialog(null, "The password cannot contain more than two of the same character in sequence.","Password Error", JOptionPane.INFORMATION_MESSAGE);
	               e.printStackTrace();
	           }
	       }
	   }

	   class CheckFilePasswordsButtonEventHandler implements EventHandler<ActionEvent>
	   {
	       @Override
	       public void handle(ActionEvent event)
	       {
	          
	           File selectedFile = null;
	           Scanner inputFile;
	           ArrayList<String> passwordList = new ArrayList<>();   
	           ArrayList<String> illegalPasswordList = new ArrayList<>();   
	          
	           // to select a file from their computer to open for reading 
	           JFileChooser fileChooser = new JFileChooser();
	           int status = fileChooser.showOpenDialog(null);
	          
	           if (status == JFileChooser.APPROVE_OPTION)
	           {
	               selectedFile = fileChooser.getSelectedFile();
	           }
	           try
	           {
	               inputFile = new Scanner(selectedFile);
	              
	               // Read each password, line by line from the .txt file into a String ArrayList
	               while (inputFile.hasNext())
	               {
	                   passwordList.add(inputFile.nextLine());
	               }
	               illegalPasswordList = check.validPasswords(passwordList);
	  
	               String illegal = "";
	              
	               // Loop through the illegalPasswordList ArrayList 
	               for(int i =0; i < illegalPasswordList.size(); i++)
	               {
	                   illegal += illegalPasswordList.get(i) + "\n";
	               }
	               JOptionPane.showMessageDialog(null, illegal, "Illegal passwords", JOptionPane.INFORMATION_MESSAGE);
	              
	           }
	           catch (FileNotFoundException e)
	           {
	               e.printStackTrace();  
	           }
	       }
	   }
	   class ExitButtonEventHandler implements EventHandler<ActionEvent>
	   {
	       @Override
	       public void handle(ActionEvent event)
	       {
	           System.exit(0);      
	       }
	   }
}
