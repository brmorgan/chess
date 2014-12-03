package edu.morgan.chess;

public class Piece 
{
	static final int KING = 1;
	static final int QUEEN = 2;
	static final int BISHOP = 3;
	static final int KNIGHT = 4;
	static final int ROOK = 5;
	static final int PAWN = 6;
	static final boolean WHITE = true;
	static final boolean BLACK = false;
	
	boolean owner;
	Cell location;
	int type;
	int id;
	int x;
	int y;
	int moves = 0;
	
	public Piece(int t, boolean o, int locx, int locy)
	{
		owner = o;
		type = t;
		x = locx;
		y = locy;
	}
	
	public Piece(Piece piece)
	{
		if(piece != null)
		{
			owner = piece.owner;
			location = piece.location;
			type = piece.type;
			id = piece.id;
			x = piece.x;
			y = piece.y;
			moves = piece.moves;
		}
	}
}
