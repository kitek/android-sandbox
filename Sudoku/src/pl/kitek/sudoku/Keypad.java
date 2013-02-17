package pl.kitek.sudoku;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Keypad extends Dialog {

	protected static final String TAG = "Sudoku";
	
	private final View keys[] = new View[9];
	private View keypad;
	
	private final int useds[];
	private final PuzzleView puzzleView;
	
	public Keypad(Context context, int useds[], PuzzleView puzzleView) {
		super(context);
		this.useds = useds;
		this.puzzleView = puzzleView;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.keypad);
		findViews();
		for (int element : useds) {
			if(element != 0) {
				// Na starcie ukrywamy wszystkie przyciski klawiatury
				keys[element - 1].setVisibility(View.INVISIBLE);
			}
		}
		setListeners();
	}

	private void setListeners() {
		// Ustawiamy eventy klikniecia na buttony i nasza klawiature
		for (int i = 0; i < keys.length; i++) {
			final int t = i + 1;
			keys[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					returnResult(t);
				}
			});
		}
		keypad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				returnResult(0);
			}
		});
		
	}

	protected void returnResult(int tile) {
		puzzleView.setSelectedTile(tile);
		dismiss();
	}

	private void findViews() {
		// Mapujemy nasza klawiature wraz z kazdym przyciskiem
		keypad = findViewById(R.id.keypad);
		keys[0] = findViewById(R.id.keypad_1);
		keys[1] = findViewById(R.id.keypad_2);
		keys[2] = findViewById(R.id.keypad_3);
		keys[3] = findViewById(R.id.keypad_4);
		keys[4] = findViewById(R.id.keypad_5);
		keys[5] = findViewById(R.id.keypad_6);
		keys[6] = findViewById(R.id.keypad_7);
		keys[7] = findViewById(R.id.keypad_8);
		keys[8] = findViewById(R.id.keypad_9);
	}
	
	private boolean isValid(int tile) {
		for(int t: useds) {
			if(tile == t) {
				return false;
			}
		}
		return true;
	}

}
