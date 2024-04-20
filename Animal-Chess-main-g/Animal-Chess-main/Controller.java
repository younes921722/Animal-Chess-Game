import java.awt.event.*;
import java.awt.BorderLayout;
import javax.swing.*;
import java.util.*;
import java.awt.event.ActionListener;

public class Controller
{
	private boolean chooseDone = false;
	private boolean bRedTurn = true;
	private AnimalChess game;
	private View view;
	private BoardPanel board;
	private Cell[][] cells;
	private Piece choice;
	
	public Controller (AnimalChess model, View view)
	{
		game =model;
		this.view = view;
		board  = view.getBoard();
		cells = board.getButtons();
		
		addEventListeners();
	}
	
	/**
		Adds ActionListeners to cell buttons
	*/
	
	private void addEventListeners()
	{
		int i, j;
		for (i = 0; i < 9; i++)
		{
			for (j = 0; j < 7; j++)
			{
				cells[j][i].addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							if((game.getRules().isWin(game.getPieces(), game.getRedAlivePieces(), game.getBlackAlivePieces()))==null)
								processSelection(e);
						}
					}
				);
			}
		}
	}
	
	/**
		processes the selection ActionEvent e
		@param e the ActionEvent to be processed
	*/
	private void processSelection(ActionEvent e)
	{
		Coordinate choiceCoord = null;
		Piece tempChoicePiece = null;
		int i, j;
		// the source of click
		Object source = e.getSource();
		for (i = 0; i < 9; i++)
		{
			for (j = 0; j < 7; j++)
			{
				if (cells[j][i] == source)
				{
					choiceCoord = new Coordinate (j, i);
				}
			}
		}
		// the first click
		if (chooseDone == false)
		{
			tempChoicePiece = searchPiece (game.getPieces(), choiceCoord);
			if (tempChoicePiece!= null && tempChoicePiece.isInRed() == bRedTurn)
			{
				this.choice = tempChoicePiece;
				chooseDone = !chooseDone;
			}
		}
		// the second click
		else
		{
			if (game.getRules().isMoveValid(game.getPieces(), choice, choiceCoord, bRedTurn))
			{
				game.move(choice, choiceCoord);
				board.getButtons()[choice.getCoordinate().getX()][choice.getCoordinate().getX()].free();
				board.repaint();
				if (bRedTurn == true)
					bRedTurn = false;
				else
					bRedTurn = true;
			}
			chooseDone = !chooseDone;
			choice = null;
		}
		// Detect the winner
		if((game.getRules().isWin(game.getPieces(), game.getRedAlivePieces(), game.getBlackAlivePieces()))!=null)
			winMessageWindow (game.getRules().isWin(game.getPieces(), game.getRedAlivePieces(), game.getBlackAlivePieces()));

	}
	
	/**
		Displays a winner message window when called
		@param winner The string containing the name of the side of the winner
	*/
	private void winMessageWindow (String winner)
	{
		JFrame winMessage = new JFrame ("Congratulations!");
		winMessage.setLayout(new BorderLayout());
		winMessage.setSize(300, 200);
		winMessage.setResizable (false);
		
		JTextArea messageArea = new JTextArea();
		messageArea.setText("Congratulations!\n" + winner);
        messageArea.setEditable(false);

		// Create a JButton
		JButton button = new JButton("Play again");
		button.setBounds(100, 50, 100, 30); // Set the position and size of the button within the window

		// Add the button to the frame
		winMessage.add(button);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				winMessage.dispose();
				view.getFrame().dispose();
				AnimalChess game = new AnimalChess();
				Controller cont = new Controller (game, new View(game));
				winMessage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
		
		winMessage.add(messageArea, BorderLayout.CENTER);
		winMessage.setVisible(true);
	}
	
	/**
		Searches and returns the piece that is found in coordinate, null otherwise
		
		@param pieces the ArrayList of pieces
		@param coords the coordinate of the piece to be found
		@return the Piece that is found in coords coordinates, null otherwise
	*/	
	private Piece searchPiece (ArrayList<Piece> pieces, Coordinate coords)
	{
		Piece found = null;
		int x = 0;
		do
		{
			if (pieces.get(x).getCoordinate().equals(coords) && pieces.get(x).isAlive())
			{
				found = pieces.get(x);
			}
			x++;
		}while(x < pieces.size() && found == null);
		return found;
	}
}