/*
Project: ICS3U
Package: memory
Class: MemoryCardGame
Programmer: Shaan Banday.
Teacher: Mr. Elliott.
Date Created: 20th October, 2020.
Program Description: A game to test ones memory by having a set of cards with a bunch of images (Canadian prime ministers). The user can click a card to
flip it over, and then click another card to see if it matches. The game is commenced when the program is ran, and it is finished either when all the
cards have been flipped over, or when the user exceeds 23 moves. If the user exceeds 23 moves, they lose the game, which engenders a score of 0,
otherwise, they garner a score between 20, and 65. The highest possible score is 65, since the lowest amount of moves is 8.  
*/
package memory; //Name of package

//Import all these methods for graphics
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.Color;

public class MemoryCardGame extends JFrame implements ActionListener //start of class. It implements an action listener since the buttons must 
	{ 
		/**
	 	* 
	 	*/
		private static final long serialVersionUID = 5333711394323858324L;
		//Declare all graphical objects
		JPanel memoryPanel;	//Create a new panel called memoryPanel to hold everything			
		LayoutManager memoryLayout; //Create a new layout called memoryLayout to display everything a certain way on the screen
		ImageIcon backOfCard = new ImageIcon("pictures/card.jpg"); //Create an image for the back of the card and set it to an image in the pictures folder
		Image cursorImage = Toolkit.getDefaultToolkit().getImage("pictures/canadaCursor.jpg"); //Create an image of the cursor from the aforementioned pictures folder
	    Cursor canadaCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point (0,0), "canadaCursor"); //Set the image of the cursor to canadaCursor, and name the cursor appropriately	
		
		//Declare all arrays
		JButton[] cards;  //An array of all the cards, stored as buttons since they will be pressed. This array will be randomised by a separate method later
		String[] pictureNames = {"pictures/jeanChretien.jpg", "pictures/johnMacdonald.jpg", "pictures/kimCampbell.jpg", "pictures/lesterPearson.jpg", 
				"pictures/paulMartin.jpg", "pictures/pierreTrudeau.jpg", "pictures/wilfridLaurier.jpg", "pictures/wlmKing.jpg", "pictures/jeanChretien.jpg",
				"pictures/johnMacdonald.jpg", "pictures/kimCampbell.jpg", "pictures/lesterPearson.jpg", "pictures/paulMartin.jpg", "pictures/pierreTrudeau.jpg", 
				"pictures/wilfridLaurier.jpg", "pictures/wlmKing.jpg"}; //A string array of size: 16, with all the locations of each picture. Note: each picture is in the array twice.  
		
		//Declare all variables
		int numCardsFlipped;  //Integer to store the number of cards currently flipped. (this can only be 0, 1, or 2)
		int counter; //Integer to store the number of cards that have been turned and matched
		int numOfMoves; //Integer to store the total number of moves
		int lastIndex1, lastIndex2; //Integer to keep track of the two indices of cards being turned over to check if they are a match
		String lastElement1, lastElement2; //String to keep track of the last two picture names (in the pictureNames[] array) to compare them using a separate method
		int score; //Integer to display the final score for the user. Note: the number of moves does not directly correlate with the scoring system in place. 
		//That is due to account for the possibility of serendipitous and unlucky circumstances where the buttons are randomised in a certain way that may 
		
		//Declare all constants
		final int MAX_MOVES = 23; //Constant used to represent the maximum number of moves (23). If the user exceeds it (i.e. 24 moves), the result will be a termination of the game.
		final int GAME_SIZE = 16;  //Constant to represent the number of cards used in game, and therefore; the size of cards[].
		
		public MemoryCardGame() //Create graphical method for all the panels and buttons
		{	//Start of method
			super("Prime Ministers Memory Game"); //Name the JFrame (window) to "Prime Ministers Memory Game"
	    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //End the program if the close button (red 'X' in the corner) is hit, otherwise program goes forever.
	    	this.setSize(500, 750); //Set the size of the window (frame) in pixels to 500x750, in order to fit all 16 cards
			
			//Initialise all previously declared and required variables
			numCardsFlipped = 0; //Set numCardsFlipped to 0, since at the advent of the program, no buttons have been flipped just yet.
	    	counter = 0; //Set counter to 0, since no successful moves have been taken yet
	    	numOfMoves = 0; //Set numOfMoves to 0, since no moves have been taken at all 
	    	lastIndex1 = -1; //Set lastIndex1 to -1. It cannot be 0, since 0 is already a valid index number 
	    	lastIndex2 = -1; //Same logic applies here, so consequently, lastIndex2 ought to be set to -1
	    	lastElement1 = ""; //Set the lastElement of the first card that is turned over to an empty string
	    	lastElement2 = ""; //Set the lastElement of the second card that is turned over to an empty string as well
	    	cards = new JButton[GAME_SIZE]; //Initialise the size of cards[] — which store buttons — to 16
	    	   	
	    	//Create a JPanel and organise the subsequent contents of the panel
	    	memoryPanel = (JPanel) this.getContentPane(); //Give the memoryPanel permission to control of all the contents of the JFrame 
	    	memoryLayout = new GridLayout(4, 4); //Set memoryLayout to a 4x4 grid layout for 16 cards
	    	memoryPanel.setLayout(memoryLayout); //Assign this 4x4 grid layout to the panel  
	    	memoryPanel.setCursor(canadaCursor); //Set the cursor of the panel to canadaCursor. This means if memoryPanel.contains(position of mouse) = true, the cursor will turn into the icon created outside this method
	    	
	    	//Execute methods
	    	createButtons(); //Run the createButtons() method, which will create all the buttons and add them to the array
	    	shuffleCards(); //Run the shuffleCards() method, which will shuffle all the contents of the array cards[]  	
	    	this.setVisible(true); //Set the panel as visible. Without this, noting can be seen
		} //End of method
	    	
	    private void createButtons() //Void method that returns nothing, but creates all the buttons and add them to the JPanel and the cards[] array
	    { 
		    for (int i = 0; i < pictureNames.length; i++) //Loop that increments by one, and that runs so long as i is less than the array length
		    {
		    	JButton button = new JButton(); //Create the first button 
		    	button.setIcon(backOfCard); //Set its image to backOfCard, which was created previously
	    		memoryPanel.add(button); //Add it to the memoryPanel
	    		button.addActionListener(this); //Add action listener so when it is pressed, something will actually happen	 
	    		button.setVisible(true); //Set the button as visible
	    		button.setName(Integer.toString(i)); //Name the button to whatever number is i. Therefore; the buttons will be named 0, 1, 2..., 15 
	    		button.setBorder(BorderFactory.createBevelBorder(0, Color.RED, Color.RED)); //Set the border of all the buttons to a red bevel border
	    		cards[i] = button;  //Add the button to the array at position i
		    } //End of loop
	    } //End of Method
	    
	    private void shuffleCards() //Void method that returns nothing, but shuffles the content of cards[]
	    { //Start of method
	    	for (int j = 0; j < cards.length; j++) //A loop that increments by one, and that runs so long as the counter is less then the array length
	    	{
	    		int x = (int)(Math.random()*16); //Create an temporary integer variable called x, which is a random number from 0 to 16, but not including 16
	    		String swap = pictureNames[j]; //Create a temporary string variable, and set it to whatever index j is of the array pictureNames[] 
	    		pictureNames[j] = pictureNames[x]; //Set the pictureName at j at to the picture name at x 
	    		pictureNames[x] = swap; //Set the picture name at x, to the temporary variable (picture name at i). A full swap has now occurred
	    	} //End of loop
	    } //End of method 	
	    
	    public void actionPerformed(ActionEvent e) //Void method that is called when any button (with an ActionListener) is pushed
	    { //Start of method    	
	    	//Determine what button was pressed
	    	JButton pressed = (JButton) e.getSource(); //Get the information of the button
	    	String cardName = pressed.getName(); //Create a string variable which stores the name of the button
	    	int index = Integer.parseInt(cardName); //Create an index variable which changes the cardName to the index
	    	
	    	//Appropriately change the button after it was pressed
	    	ImageIcon frontOfCard = new ImageIcon(pictureNames[index]); //Create new icon of the picture of the button to the corresponding picture name
	    	numCardsFlipped++; //Indicate that a card has now been flipped
	    	
	    	//Decisions
	    	if (numCardsFlipped == 1) //If only one card was flipped
	    	{
		    	cards[index].setIcon(frontOfCard); //Set the picture on the button to the previously declared image
		    	lastIndex1 = index; //Store the last index
		    	lastElement1 = pictureNames[index]; //Store the name of the picture
	    	}
	    	else //If more than card card was flipped
	    	{
		    	cards[index].setIcon(frontOfCard); //Set the picture of the second button to the corresponding picture in the pictureNames[] array
	    		lastIndex2 = index; //Store the index of the second button
	    		lastElement2 = pictureNames[index]; //Store the name of the picture of the button
	    		numOfMoves++; //Add one to the number of moves, since at this point two cards are up
	    		
	    		//Pause program and hold both cards up for a period of time
	    		this.paint(this.getGraphics()); //Let the paint method to be paused
	    		pause(900); //Pause the paint method for 900 milliseconds
	    		
	    		//Nested Decisions
	    		if (isMatch(lastElement1, lastElement2)) //If the names of the corresponding card names match
	    		{
	    			//Disable both cards
	    			cards[lastIndex1].setEnabled(false); 
	    			cards[lastIndex2].setEnabled(false);
	    			
	    			counter+=2; //Add 2 to counter to indicate that 2 cards are now face up
	    			numCardsFlipped = 0; //Set the number of cards that have been flipped (which can only be 0, 1, or 2) back to 0
	    		}
	    		else //If the names of the two corresponding card names do not match
	    		{
	    			//Reset the images on the back of the card
			    	cards[lastIndex1].setIcon(backOfCard); 
			    	cards[lastIndex2].setIcon(backOfCard);
			    	numCardsFlipped = 0; //Set the number of cards that have been flipped, abck to 0
	    		}
	    	}
	    	repaint(); //Repaint the screen and update it
	    }
	    private boolean isMatch(String prev, String current) //Boolean method with two String parameters, that checks if the Strings (pictureName[]) match up
	    { //Start of method
	    	//Declare variables
	    	boolean match; //Whether the two strings match or not
	    	
	    	//Decisions
	    	if (prev.equalsIgnoreCase(current)) //If the name of the first picture is equivalent (ignoring case) with the name of the second picture
	    	{
	    		match = true; //They are a match
	    	}
	    	else //If the two picture names are not equivalent
	    	{
	    		match = false; //They are not a match
	    	}
	    	return match; //Return whether or not match is true or false
	    }  //End of method
	    
	    public void pause(int a) //Void method with an Integer parameter, that pauses the program for 'a' seconds. A is the variable that is passed to the method
	    { //Start of method
	    	try //Try to execute the block of code below
	    	{ 
	    		Thread.sleep(a); //Sleep and stop the program for 'a' seconds
	    	}
	    	catch (Exception e) { //Handle any errors found
	    	}
	    } //End of method
	    
	    public void paint(Graphics g) //Void method that paints the JPanel
	    { //Start of Method
	    	super.paint(g); //Enable panel to be painted
	    	
	    	//Declare font objects
    		Font titleFont = new Font("Times Roman", Font.BOLD, 100); //Set font for title ("YOU WIN" or "GAME OVER") as TNR Bold, size 100
    		Font underFont = new Font("Times Roman", Font.BOLD, 36); //Set font for the message underneath the title as TNR Bold, size 36
    		g.setColor(Color.BLACK); //Set the drawing colour to black
    		
    		//Decisions
    		if ((counter == 16) && (numOfMoves <= MAX_MOVES)) //If 16 cards were flipped moves are made, and the Number of moves does not exceed 23 
    		{
    			//Nested Decisions
	    		if (numOfMoves <= 12) //If the number of moves is less than 12
	    		{
		    		 g.setFont(titleFont); //Set the font to the title font
		    		 g.drawString("YOU WIN!", 10, 375); //Draw the strings that indicates that the user won
		    		 g.setFont(underFont); //Set the font to the under font
		    		 
		    		 //Draw the strings that indicate the details
		    		 g.drawString("You either are an absolute", 25, 490);
		    		 g.drawString("genius or just really lucky", 30, 530);
		    		 g.drawString("since you only took", 80, 570);
		    		 g.drawString("" + numOfMoves + " moves", 170, 610);
		    		 
		    		 //Score
		    		 score = findScore(numOfMoves); //Find the score by passing it to the findScore method
		    		 g.drawString("Score = " + score, 150, 670); //Draw what the score equals
	    		}
	    		else if ((numOfMoves >= 13) && (numOfMoves <= 16)) //If the number of moves is between 13 and 16
	    		{
	    			 g.setFont(titleFont); //Set the font to the title font
		    		 g.drawString("YOU WIN!", 10, 375); //Draw the strings that indicates that the user won
		    		 g.setFont(underFont); //Set the font to the under font
		    		 
		    		 //Draw the strings that indicate the details
		    		 g.drawString("You have a pretty good", 50, 490);
		    		 g.drawString("memory since you only", 50, 530);
		    		 g.drawString("took " + numOfMoves + " moves", 125, 570);
		    		 
		    		 //Score
		    		 score = findScore(numOfMoves); //Find the score by passing it to the findScore method
		    		 g.drawString("Score = " + score, 150, 670); //Draw what the score equals
	    		}
	    		else if ((numOfMoves >= 17) && (numOfMoves <= 20)) //If the number of moves is between 17 and 20
	    		{
	    			 g.setFont(titleFont); //Set the font to the title font
		    		 g.drawString("YOU WIN!", 10, 375); //Draw the strings that indicates that the user won
		    		 g.setFont(underFont); //Set the font to the under font
		    		 
		    		 //Draw the strings that indicate the details
		    		 g.drawString("Good Job, you took ", 70, 490);
		    		 g.drawString("" + numOfMoves + " moves", 160, 530);
		    		 
		    		 //Score
		    		 score = findScore(numOfMoves); //Find the score by passing it to the findScore method
		    		 g.drawString("Score = " + score, 150, 670); //Draw what the score equals
	    		}
	    		else //If the number of moves is above 20
	    		{
	    			 g.setFont(titleFont); //Set the font to the title font
		    		 g.drawString("YOU WIN!", 10, 375); //Draw the strings that indicates that the user won
		    		 g.setFont(underFont); //Set the font to the under font
		    		 
		    		 //Draw the strings that indicate the details
		    		 g.drawString("You took "  + numOfMoves + " moves", 70, 490);
		    		
		    		 //Score
		    		 score = findScore(numOfMoves); //Find the score by passing it to the findScore method
		    		 g.drawString("Score = " + score, 150, 670); //Draw what the score equals
	    		} 
    		}
    		else if (numOfMoves > MAX_MOVES) //If the number of moves is greater than 23
    		{ 
    			//Loops
    			for (int count = 0; count < cards.length; count++) //Increment loop by one, and run until count is no longer less than the length of the cards[] array
    			{
    				cards[count].setEnabled(false); //Disable the button in the cards[] array at index count
    			}
    			//All buttons are now disabled and cannot be pressed
    			
    			g.setFont(titleFont); //Set the font to the titleFont
    			
    			//Indicate that the user lost
	    		g.drawString("GAME", 100, 275);
	    		g.drawString("OVER", 110, 385);
	    		
	    		//Draw the strings that indicate the details
	    		g.setFont(underFont); //Set the font to the underFont
	    		g.drawString("You took more than", 80, 490);
	    		g.drawString("23 moves", 140, 530);
	    		 
	    		//Score
	    		score = findScore(numOfMoves); //Find the score by passing it to the findScore method
	    		g.drawString("Score = " + score, 150, 670); //Draw what the score equals
    		}
    		else //If neither are the case, and the game is still going
    		{
    			counter += 0; //Do not change the number of successful moves
    			numOfMoves += 0; //Do not change the total number of moves
    		} 
	    } //End of method
	    
	    public int findScore (int moves) //Integer method with an integer parameter that is used to find the final score 
	    {
	    	//Declare variables
	    	double resultScore; //Create a double to hold the score
	    	
	    	//Decisions
	    	if(moves <= MAX_MOVES) //If the number of moves is less than or equal to the maximum number of moves
	    	{
	    		resultScore = (100.0/moves); //Set the score to 100 ÷ the number of moves
	    	}
	    	else //If the number of moves exceeds the maximum
	    	{
	    		resultScore = 0; //Set the score to 0, since this means that the user lost
	    	}
	    	int roundedScore = (int)Math.round(resultScore); //Create an integer variable that stores the score rounded to the nearest whole number
	    	roundedScore *= 5; //Multiply the score by 5
	    	return roundedScore; //Return the Integer rounded score
	    }  
}