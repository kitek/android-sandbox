package pl.kitek.sudoku;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Sudoku extends Activity implements OnClickListener {

	private static final String TAG = "Sudoku";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		View continueButton = findViewById(R.id.continue_button);
		continueButton.setOnClickListener(this);

		View newButton = findViewById(R.id.new_button);
		newButton.setOnClickListener(this);

		View aboutButton = findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);

		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, Pref.class));
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_button:
			openNewGameDialog();
			break;
		case R.id.about_button:
			Intent i = new Intent(this, About.class);
			startActivity(i);
			break;
		case R.id.exit_button:
			finish();
			break;

		}

	}

	private void openNewGameDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.new_game_title)
				.setItems(R.array.difficulty,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								startGame(which);
							}
						}).show();

	}

	private void startGame(int i) {
		Log.d(TAG, "Clicked "+i);
		
		Intent intent = new Intent(Sudoku.this, Game.class);
		intent.putExtra(Game.KEY_DIFFICULTY, i);
		startActivity(intent);
	}

}
