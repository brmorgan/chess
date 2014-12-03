package edu.morgan.chess;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity 
{
	static Bitmap bBishop;
	static Bitmap bKing;
	static Bitmap bKnight;
	static Bitmap bPawn;
	static Bitmap bQueen;
	static Bitmap bRook;
	static Bitmap wBishop;
	static Bitmap wKing;
	static Bitmap wKnight;
	static Bitmap wPawn;
	static Bitmap wQueen;
	static Bitmap wRook;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bBishop = BitmapFactory.decodeResource(getResources(), R.drawable.bbishop);
		bKing = BitmapFactory.decodeResource(getResources(), R.drawable.bking);
		bKnight = BitmapFactory.decodeResource(getResources(), R.drawable.bknight);
		bPawn = BitmapFactory.decodeResource(getResources(), R.drawable.bpawn);
		bQueen = BitmapFactory.decodeResource(getResources(), R.drawable.bqueen);
		bRook = BitmapFactory.decodeResource(getResources(), R.drawable.brook);
		wBishop = BitmapFactory.decodeResource(getResources(), R.drawable.wbishop);
		wKing = BitmapFactory.decodeResource(getResources(), R.drawable.wking);
		wKnight = BitmapFactory.decodeResource(getResources(), R.drawable.wknight);
		wPawn = BitmapFactory.decodeResource(getResources(), R.drawable.wpawn);
		wQueen = BitmapFactory.decodeResource(getResources(), R.drawable.wqueen);
		wRook = BitmapFactory.decodeResource(getResources(), R.drawable.wrook);
		
		startActivity(new Intent(this, Gameboard.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
