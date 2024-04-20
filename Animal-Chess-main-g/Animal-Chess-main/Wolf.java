import java.util.*;


public class Wolf extends Piece
{
	public Wolf (Coordinate coord, boolean inRed, String side)
	{
		super (coord, inRed, "C:\\Users\\dell\\Downloads\\Compressed\\Animal-Chess-main\\Animal-Chess-main\\imagePieces\\" + side + "_wolf.png", 3);
	}
	
	/**
		Searches and returns the piece that is found in coordinate, null otherwise
		
//		@param pieces the ArrayList of pieces
//		@param coords the coordinate of the piece to be found
//		@return the Piece that is found in coords coordinates, null otherwise
	*/
	@Override
	public boolean equals (Object obj)
	{
		if (obj != null)
		{
			Piece otherPiece = (Piece) obj;
			return super.getCoordinate().equals((otherPiece.getCoordinate()));
		}
		else return false;
	}
}