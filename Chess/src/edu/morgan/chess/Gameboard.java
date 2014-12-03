package edu.morgan.chess;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.GridLayout.Spec;
import android.widget.ImageButton;

public class Gameboard extends Activity 
{
	static final int LIGHT = Color.WHITE;
	static final int DARK = Color.GRAY;
	static boolean turn;
	static Cell[][] cells = new Cell[8][8];
	static Cell wKingloc;
	static Cell bKingloc;
	
	ArrayList<Integer> wKeys;
	ArrayList<Integer> bKeys;
	HashMap<Integer, Piece> wPieces;
	HashMap<Integer, Piece> bPieces;
	private int cellsize;
	private GridLayout layout;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gameboard);
		
		wPieces = new HashMap<Integer, Piece>();
		bPieces = new HashMap<Integer, Piece>();
		wKeys = new ArrayList<Integer>();
		bKeys = new ArrayList<Integer>();
		
		layout = (GridLayout)findViewById(R.id.myLayout);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point screensize = new Point();
		display.getSize(screensize);
		if(screensize.x < screensize.y)
		{
			cellsize = screensize.x / 9;
		}
		else
		{
			cellsize = screensize.y / 9;
		}
		
		for(int i=0; i<=7; i++)
		{
			for(int j=0; j<=7; j++)
			{	
				if((i+j)%2 == 0)
				{
					cells[i][j] = new Cell(i, j, cellsize, LIGHT, this, layout);
				}
				else
				{
					cells[i][j] = new Cell(i, j, cellsize, DARK, this, layout);
				}
				
				Spec row = GridLayout.spec(j);
				Spec col = GridLayout.spec(i);
				GridLayout.LayoutParams param = new GridLayout.LayoutParams(row, col);
				layout.addView(cells[i][j].iv, param);
			}
		}
		
		initializeBoard();
		turn = Piece.WHITE;
	}
	
	private void initializeBoard()
	{
		wPieces.put(0, new Piece(Piece.KING, Piece.WHITE, 4, 7));
		wPieces.put(1, new Piece(Piece.QUEEN, Piece.WHITE, 3, 7));
		wPieces.put(2, new Piece(Piece.BISHOP, Piece.WHITE, 2, 7));
		wPieces.put(3, new Piece(Piece.BISHOP, Piece.WHITE, 5, 7));
		wPieces.put(4, new Piece(Piece.KNIGHT, Piece.WHITE, 1, 7));
		wPieces.put(5, new Piece(Piece.KNIGHT, Piece.WHITE, 6, 7));
		wPieces.put(6, new Piece(Piece.ROOK, Piece.WHITE, 0, 7));
		wPieces.put(7, new Piece(Piece.ROOK, Piece.WHITE, 7, 7));
		bPieces.put(0, new Piece(Piece.KING, Piece.BLACK, 4, 0));
		bPieces.put(1, new Piece(Piece.QUEEN, Piece.BLACK, 3, 0));
		bPieces.put(2, new Piece(Piece.BISHOP, Piece.BLACK, 2, 0));
		bPieces.put(3, new Piece(Piece.BISHOP, Piece.BLACK, 5, 0));
		bPieces.put(4, new Piece(Piece.KNIGHT, Piece.BLACK, 1, 0));
		bPieces.put(5, new Piece(Piece.KNIGHT, Piece.BLACK, 6, 0));
		bPieces.put(6, new Piece(Piece.ROOK, Piece.BLACK, 0, 0));
		bPieces.put(7, new Piece(Piece.ROOK, Piece.BLACK, 7, 0));
		
		wKingloc = cells[4][7];
		bKingloc = cells[4][0];
		
		for(int i=0; i<=7; i++)
		{
			wPieces.put(8+i, new Piece(Piece.PAWN, Piece.WHITE, i, 6));
			wKeys.add(i);
			bPieces.put(8+i, new Piece(Piece.PAWN, Piece.BLACK, i, 1));
			bKeys.add(i);
		}
		for(int i=8; i<=15; i++)
		{
			wKeys.add(i);
			bKeys.add(i);
		}
		for(int i : wKeys)
		{
			Piece piece = wPieces.get(i);
			
			cells[piece.x][piece.y].setPiece(piece);
			//cells[piece.x][piece.y].drawPiece(piece);
			piece.location = cells[piece.x][piece.y];
			piece.id = i;
		}
		for(int i : bKeys)
		{
			Piece piece = bPieces.get(i);
			
			cells[piece.x][piece.y].setPiece(piece);
			//cells[piece.x][piece.y].drawPiece(piece);
			piece.location = cells[piece.x][piece.y];
			piece.id = i;
		}
	}
	
	public void capture(Piece piece)
	{
		if(piece.owner == Piece.WHITE)
		{
			wPieces.remove(piece.id);
		}
		else
		{
			bPieces.remove(piece.id);
		}
	}
	
	public boolean checkmate()
	{
		Cell tempcell = Cell.fromCell;
		
		if(turn == Piece.WHITE)
		{
			for(int i : wKeys)
			{
				if(wPieces.get(i) != null)
				{
					Cell.fromCell = wPieces.get(i).location;
					for(Cell[] row : cells)
					{
						for(Cell cell : row)
						{
							if(cell.isValidMove() && cell.makeMove())
							{
								Cell.fromCell = tempcell;
								return false;
							}
						}
					}
				}
			}
		}
		else
		{
			for(int i : bKeys)
			{
				if(bPieces.get(i) != null)
				{
					Cell.fromCell = bPieces.get(i).location;
					for(Cell[] row : cells)
					{
						for(Cell cell : row)
						{
							if(cell.isValidMove() && cell.makeMove())
							{
								Cell.fromCell = tempcell;
								return false;
							}
						}
					}
				}
			}
		}
		
		Cell.fromCell = tempcell;
		return true;
	}
	
	public boolean check()
	{
		Cell tempcell = Cell.fromCell;
		
		if(turn == Piece.WHITE)
		{
			for(int i : bKeys)
			{
				if(bPieces.get(i) != null)
				{
					Cell.fromCell = bPieces.get(i).location;
					if(Cell.fromCell != null)
					{
						if(wKingloc.isValidMove())
						{
							Cell.fromCell = tempcell;
							//Toast toast = Toast.makeText(context, "Check", Toast.LENGTH_SHORT);
							//toast.show();
							
							//if(checkmate())
							//{
							//	finish();
							//}
							return true;
						}
					}
				}
			}
		}
		else
		{
			for(int i : wKeys)
			{
				if(wPieces.get(i) != null)
				{
					Cell.fromCell = wPieces.get(i).location;
					if(Cell.fromCell != null)
					{
						if(bKingloc.isValidMove())
						{
							Cell.fromCell = tempcell;
							//Toast toast = Toast.makeText(context, "Check", Toast.LENGTH_SHORT);
							//toast.show();
							
							//if(checkmate())
							//{
							//	finish();
							//}
							return true;
						}
					}
				}
			}
		}
		
		Cell.fromCell = tempcell;
		return false;
	}
	
	public void remove(int index)
	{
		if(wPieces.get(index).owner == Piece.WHITE)
		{
			wPieces.remove(index);
		}
		else
		{
			bPieces.remove(index);
		}
	}

	public void promote(final Cell cell, final Piece piece)
	{
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.promotionlayout);
		dialog.setTitle("Promotion");
		dialog.setCancelable(false);
		
		ImageButton bishopButton = (ImageButton) dialog.findViewById(R.id.bishopButton);
		bishopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				piece.type = Piece.BISHOP;
				cell.removePiece();
				cell.setPiece(piece);
				dialog.dismiss();
			}
		});
		ImageButton knightButton = (ImageButton) dialog.findViewById(R.id.knightButton);
		knightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				piece.type = Piece.KNIGHT;
				cell.removePiece();
				cell.setPiece(piece);
				dialog.dismiss();
			}
		});
		ImageButton rookButton = (ImageButton) dialog.findViewById(R.id.rookButton);
		rookButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				piece.type = Piece.ROOK;
				cell.removePiece();
				cell.setPiece(piece);
				dialog.dismiss();
			}
		});
		ImageButton queenButton = (ImageButton) dialog.findViewById(R.id.queenButton);
		queenButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				piece.type = Piece.QUEEN;
				cell.removePiece();
				cell.setPiece(piece);
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gameboard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
