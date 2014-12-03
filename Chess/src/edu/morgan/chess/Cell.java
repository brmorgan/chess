package edu.morgan.chess;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.GridLayout;
import android.widget.Toast;

public class Cell
{
	private Context boardContext;
	private Paint cellpaint;
	private Paint highlight;
	private Canvas cellcanvas;
	private int cellsize;
	private Cell me = this;
	private int x;
	private int y;
	Piece localpiece = null;
	Bitmap bmp;
	ImageView iv;
	
	static Cell fromCell = null;
	static Cell toCell = null;
	
	// The cell constructor takes a position, a size, a color, a context, and a layout
	public Cell(int locx, int locy, int size, int color, Context context, GridLayout layout)
	{	
		boardContext = context;
		cellsize = size;
		cellpaint = new Paint();
		cellpaint.setColor(color);
		cellpaint.setStyle(Style.FILL);
		highlight = new Paint();
		highlight.setColor(Color.BLUE);
		highlight.setStyle(Style.STROKE);
		highlight.setStrokeWidth(5);
		
		x = locx;
		y = locy;
		
		iv = new ImageView(boardContext);
		iv.setOnTouchListener(new touchListener());
		
		bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		cellcanvas = new Canvas(bmp);
		
		cellcanvas.drawRect(0, 0, cellsize-1, cellsize-1, cellpaint);
		
		iv.setImageBitmap(bmp);
	}
	
	public Cell(Cell cell)
	{
		x = cell.x;
		y = cell.y;
		localpiece = cell.localpiece;
		boardContext = cell.boardContext;
		cellpaint = cell.cellpaint;
		highlight = cell.highlight;
		cellcanvas = cell.cellcanvas;
		cellsize = cell.cellsize;
		me = cell;
		bmp = cell.bmp;
		iv = cell.iv;
	}
	
	// absolute value function
	public int abs(int x)
	{
		return (x<0) ? 0-x : x;
	}
	
	public void setPiece(Piece piece)
	{	
		if(piece.owner == Piece.WHITE)
		{	
			switch(piece.type)
			{
			case Piece.KING:
				cellcanvas.drawBitmap(MainActivity.wKing, null, new Rect(0,0,cellsize-1,cellsize-1), cellpaint);
				Gameboard.wKingloc = me;
				break;
				
			case Piece.QUEEN:
				cellcanvas.drawBitmap(MainActivity.wQueen, null, new Rect(0,0,cellsize-1,cellsize-1), cellpaint);
				break;
				
			case Piece.BISHOP:
				cellcanvas.drawBitmap(MainActivity.wBishop, null, new Rect(0,0,cellsize-1,cellsize-1), cellpaint);
				break;
				
			case Piece.KNIGHT:
				cellcanvas.drawBitmap(MainActivity.wKnight, null, new Rect(0,0,cellsize-1,cellsize-1), cellpaint);
				break;
				
			case Piece.ROOK:
				cellcanvas.drawBitmap(MainActivity.wRook, null, new Rect(0,0,cellsize-1,cellsize-1), cellpaint);
				break;
				
			case Piece.PAWN:
				cellcanvas.drawBitmap(MainActivity.wPawn, null, new Rect(0,0,cellsize-1,cellsize-1), cellpaint);
				if(y == 0)
				{
					((Gameboard) boardContext).promote(this, piece);
				}
				break;
			}
		}
		else
		{
			switch(piece.type)
			{
			case Piece.KING:
				cellcanvas.drawBitmap(MainActivity.bKing, null, new Rect(0,0,cellsize-1,cellsize-1), cellpaint);
				Gameboard.bKingloc = me;
				break;
				
			case Piece.QUEEN:
				cellcanvas.drawBitmap(MainActivity.bQueen, null, new Rect(0,0,cellsize-1,cellsize-1), cellpaint);
				break;
				
			case Piece.BISHOP:
				cellcanvas.drawBitmap(MainActivity.bBishop, null, new Rect(0,0,cellsize-1,cellsize-1), cellpaint);
				break;
				
			case Piece.KNIGHT:
				cellcanvas.drawBitmap(MainActivity.bKnight, null, new Rect(0,0,cellsize-1,cellsize-1), cellpaint);
				break;
				
			case Piece.ROOK:
				cellcanvas.drawBitmap(MainActivity.bRook, null, new Rect(0,0,cellsize-1,cellsize-1), cellpaint);
				break;
				
			case Piece.PAWN:
				cellcanvas.drawBitmap(MainActivity.bPawn, null, new Rect(0,0,cellsize-1,cellsize-1), cellpaint);
				if(y == 7)
				{
					((Gameboard) boardContext).promote(this, piece);
				}
				break;
			}
		}
		
		piece.location = me;
		piece.x = x;
		piece.y = y;
		localpiece = piece;
		iv.setImageBitmap(bmp);
		iv.invalidate();
	}
	
	public void removePiece()
	{
		localpiece = null;
		
		iv.setImageDrawable(null);

		bmp.eraseColor(Color.TRANSPARENT);
		cellcanvas.drawBitmap(bmp, 0, 0, cellpaint);
		cellcanvas.drawRect(0, 0, cellsize-1, cellsize-1, cellpaint);
		
		iv.setImageBitmap(bmp);
		iv.invalidate();
	}
	
	public boolean isValidMove()
	{
		int deltax = x - fromCell.localpiece.x;
		int deltay = y - fromCell.localpiece.y;
		
		if(deltax == 0 && deltay == 0)
			return false;
		
		switch(fromCell.localpiece.type)
		{
		case Piece.KING:
			if(abs(deltay) > 1)
				return false;
			if(abs(deltax) > 2)
				return false;
			if(fromCell.localpiece.moves == 0)
			{
				if(deltax == 2 && deltay == 0)
				{
					if(((Gameboard) boardContext).check()
							|| Gameboard.cells[fromCell.localpiece.x+3][y].localpiece == null
							|| Gameboard.cells[fromCell.localpiece.x+3][y].localpiece.moves != 0
							|| Gameboard.cells[fromCell.localpiece.x+1][y].localpiece != null
							|| !Gameboard.cells[fromCell.localpiece.x+1][y].makeMove()
							|| Gameboard.cells[fromCell.localpiece.x+2][y].localpiece != null
							|| !Gameboard.cells[fromCell.localpiece.x+2][y].makeMove())
					{
						return false;
					}
					else
					{
						Gameboard.cells[fromCell.localpiece.x+1][y].setPiece(Gameboard.cells[fromCell.localpiece.x+3][y].localpiece);
						Gameboard.cells[fromCell.localpiece.x+3][y].removePiece();	
					}
				}
				else if(deltax == -2 && deltay == 0)
				{
					if(((Gameboard) boardContext).check()
							||Gameboard.cells[fromCell.localpiece.x-4][y].localpiece == null
							|| Gameboard.cells[fromCell.localpiece.x-4][y].localpiece.moves != 0
							|| Gameboard.cells[fromCell.localpiece.x-1][y].localpiece != null
							|| !Gameboard.cells[fromCell.localpiece.x-1][y].makeMove()
							|| Gameboard.cells[fromCell.localpiece.x-2][y].localpiece != null
							|| !Gameboard.cells[fromCell.localpiece.x-2][y].makeMove()
							|| Gameboard.cells[fromCell.localpiece.x-3][y].localpiece != null
							|| !Gameboard.cells[fromCell.localpiece.x-3][y].makeMove())
					{
						return false;
					}
					else
					{
						Gameboard.cells[fromCell.localpiece.x-1][y].setPiece(Gameboard.cells[fromCell.localpiece.x-4][y].localpiece);
						Gameboard.cells[fromCell.localpiece.x-4][y].removePiece();
					}
				}
				else if(abs(deltax) > 1)
					return false;
			}
			else if(abs(deltax) > 1)
				return false;
			break;
		
		case Piece.QUEEN:
			boolean bishopmove = false;
			boolean rookmove = false;
			fromCell.localpiece.type = Piece.BISHOP;
			bishopmove = isValidMove();
			if(!bishopmove)
			{
				fromCell.localpiece.type = Piece.ROOK;
				rookmove = isValidMove();
			}
			fromCell.localpiece.type = Piece.QUEEN;
			return (bishopmove || rookmove);
			
		case Piece.BISHOP:
			if(abs(deltax) != abs(deltay))
				return false;
			if(deltax < 0)
			{
				if(deltay < 0)
				{
					for(int i=1; i<abs(deltax); i++ )
					{
						if(Gameboard.cells[fromCell.x - i][fromCell.y - i].localpiece != null)
							return false;
					}
				}
				else
				{
					for(int i=1; i<abs(deltax); i++ )
					{
						if(Gameboard.cells[fromCell.x - i][fromCell.y + i].localpiece != null)
							return false;
					}
				}
			}
			else
			{
				if(deltay < 0)
				{
					for(int i=1; i<abs(deltax); i++ )
					{
						if(Gameboard.cells[fromCell.x + i][fromCell.y - i].localpiece != null)
							return false;
					}
				}
				else
				{
					for(int i=1; i<abs(deltax); i++ )
					{
						if(Gameboard.cells[fromCell.x + i][fromCell.y + i].localpiece != null)
							return false;
					}
				}
			}
			break;
			
		case Piece.KNIGHT:
			if(!(abs(deltax) == 1 && abs(deltay) == 2) && !(abs(deltax) == 2 && abs(deltay) == 1))
				return false;
			break;
			
		case Piece.ROOK:
			if(abs(deltax) > 0)
			{
				if(deltay != 0)
					return false;
				if(deltax > 0)
				{
					for(int i=1; i<abs(deltax); i++)
					{
						if(Gameboard.cells[fromCell.x + i][y].localpiece != null)
							return false;
					}
				}
				else
				{
					for(int i=1; i<abs(deltax); i++)
					{
						if(Gameboard.cells[fromCell.x - i][y].localpiece != null)
							return false;
					}
				}
			}
			if(abs(deltay) > 0)
			{
				if(deltax != 0)
					return false;
				if(deltay > 0)
				{
					for(int i=1; i<abs(deltay); i++)
					{
						if(Gameboard.cells[x][fromCell.y + i].localpiece != null)
							return false;
					}
				}
				else
				{
					for(int i=1; i<abs(deltay); i++)
					{
						if(Gameboard.cells[x][fromCell.y - i].localpiece != null)
							return false;
					}
				}
			}
			break;
			
		case Piece.PAWN:
			deltay = (fromCell.localpiece.owner == Piece.WHITE) ? 0-deltay : deltay;
			if(deltay < 1)
				return false;
			if(localpiece != null)
			{
				if(abs(deltax) != 1)
					return false;
				if(deltay != 1)
					return false;
			}
			else
			{
				if(deltax != 0)
					return false;
				if(deltay > 2)
					return false;
				if(fromCell.localpiece.moves > 0 && deltay > 1)
					return false;
			}
			break;
		}
		
		return true;
	}
	
	public boolean makeMove()
	{
		Piece tempfrom = fromCell.localpiece;
		Piece tempto = localpiece;
		
		if(Gameboard.cells[0][0].localpiece != null)
			Gameboard.cells[0][0].localpiece.moves++;
		if(Gameboard.cells[7][0].localpiece != null)
			Gameboard.cells[7][0].localpiece.moves++;
		if(Gameboard.cells[0][7].localpiece != null)
			Gameboard.cells[0][7].localpiece.moves++;
		if(Gameboard.cells[7][7].localpiece != null)
			Gameboard.cells[7][7].localpiece.moves++;
		
		if(localpiece != null)
		{
			localpiece.location = null;
		}
		setPiece(fromCell.localpiece);
		
		fromCell.removePiece();
		
		if(((Gameboard) boardContext).check())
		{
			removePiece();
			if(tempto != null)
			{
				setPiece(tempto);
			}
			
			fromCell.setPiece(tempfrom);
			
			if(Gameboard.cells[0][0].localpiece != null)
				Gameboard.cells[0][0].localpiece.moves--;
			if(Gameboard.cells[7][0].localpiece != null)
				Gameboard.cells[7][0].localpiece.moves--;
			if(Gameboard.cells[0][7].localpiece != null)
				Gameboard.cells[0][7].localpiece.moves--;
			if(Gameboard.cells[7][7].localpiece != null)
				Gameboard.cells[7][7].localpiece.moves--;
			
			return false;
		}
		else
		{
			removePiece();
			if(tempto != null)
			{
				setPiece(tempto);
			}
			fromCell.setPiece(tempfrom);
			
			if(Gameboard.cells[0][0].localpiece != null)
				Gameboard.cells[0][0].localpiece.moves--;
			if(Gameboard.cells[7][0].localpiece != null)
				Gameboard.cells[7][0].localpiece.moves--;
			if(Gameboard.cells[0][7].localpiece != null)
				Gameboard.cells[0][7].localpiece.moves--;
			if(Gameboard.cells[7][7].localpiece != null)
				Gameboard.cells[7][7].localpiece.moves--;
			
			return true;
		}
	}
	
	class touchListener implements View.OnTouchListener
	{
		public boolean onTouch(View v, MotionEvent event)
		{
			if(fromCell == null)
			{
				if(localpiece != null && localpiece.owner == Gameboard.turn)
				{
					cellcanvas.drawRect(0, 0, cellsize-1, cellsize-1, highlight);
					iv.setImageBitmap(bmp);
					fromCell = me;
				}
			}
			else
			{
				if(fromCell != me && (localpiece == null || localpiece.owner != Gameboard.turn) && isValidMove())
				{
					if(makeMove())
					{
						if(localpiece != null && localpiece.owner != Gameboard.turn)
						{
							((Gameboard) boardContext).capture(localpiece);
							removePiece();
						}
						
						setPiece(fromCell.localpiece);
						localpiece.moves++;
						
						fromCell.removePiece();
						fromCell = null;
						
						if(Gameboard.turn == Piece.WHITE)
						{
							Gameboard.turn = Piece.BLACK;
						}
						else
						{
							Gameboard.turn = Piece.WHITE;
						}
						
						if(((Gameboard) boardContext).checkmate())
						{
							if(((Gameboard) boardContext).check())
							{
								Dialog dialog = null;
								AlertDialog.Builder popup;
								popup = new AlertDialog.Builder(boardContext);
								popup.setMessage("Checkmate")
									.setCancelable(false)
									.setPositiveButton("Exit", new DialogInterface.OnClickListener(){
										public void onClick(DialogInterface dialog, int id){((Gameboard) boardContext).finish();}
									});
								dialog = popup.create();
								dialog.show();
							}
							else
							{
								Dialog dialog = null;
								AlertDialog.Builder popup;
								popup = new AlertDialog.Builder(boardContext);
								popup.setMessage("Stalemate")
									.setCancelable(false)
									.setPositiveButton("Exit", new DialogInterface.OnClickListener(){
										public void onClick(DialogInterface dialog, int id){((Gameboard) boardContext).finish();}
									});
								dialog = popup.create();
								dialog.show();
							}
						}
					}
					else
					{
						Toast toast = Toast.makeText(boardContext, "Check", Toast.LENGTH_SHORT);
						toast.show();
						
						if(fromCell != me)
						{
							fromCell.cellcanvas.drawBitmap(fromCell.bmp, 0, 0, fromCell.cellpaint);
							fromCell.cellcanvas.drawRect(0, 0, cellsize-1, cellsize-1, fromCell.cellpaint);
							fromCell.setPiece(fromCell.localpiece);
							fromCell = null;
						}
					}
				}
				else if(fromCell != me)
				{
					fromCell.cellcanvas.drawBitmap(fromCell.bmp, 0, 0, fromCell.cellpaint);
					fromCell.cellcanvas.drawRect(0, 0, cellsize-1, cellsize-1, fromCell.cellpaint);
					fromCell.setPiece(fromCell.localpiece);
					fromCell = null;
				}
			}
			return true;
		}
	}
}
