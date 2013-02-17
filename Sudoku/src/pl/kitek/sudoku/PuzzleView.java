package pl.kitek.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PuzzleView extends View {

	private static final String TAG = "Sudoku";
	private Game game;
	private float width;
	private float height;
	private int selX;
	private int selY;
	private final Rect selRect = new Rect();
	private final Paint background;
	private final Paint dark;
	private final Paint hilite;
	private final Paint light;
	private final Paint foreground;
	private final Paint selected;
	private final Paint hint;

	public PuzzleView(Context context) {
		super(context);
		this.game = (Game) context;
		setFocusable(true);
		setFocusableInTouchMode(true);

		background = new Paint();
		background.setColor(getResources().getColor(R.color.puzzle_background));

		dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));

		hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.puzzle_hilite));

		light = new Paint();
		light.setColor(getResources().getColor(R.color.puzzle_light));

		foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextAlign(Paint.Align.CENTER);

		selected = new Paint();
		selected.setColor(getResources().getColor(R.color.puzzle_selected));

		hint = new Paint();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w / 9f;
		height = h / 9f;
		getRect(selX, selY, selRect);
		Log.d(TAG, "onSizeChanged: w " + width + ",h " + height);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// Aktualizujemy dane foregrouda - musimy to zrobic w tym miejscu
		// poniewaz w konstruktorze nie dysponujemy
		// wysokoscia i szerokoscia widoku
		foreground.setTextSize(height * 0.75f);
		foreground.setTextScaleX(width / height);

		// Rysujemy tlo planszy
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);

		// Rysujemy mniejsze linie
		for (int i = 0; i < 9; i++) {
			canvas.drawLine(0, i * height, getWidth(), i * height, light);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1,
					hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), light);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(),
					hilite);
		}

		// Rysujemy wieksze linie
		for (int i = 0; i < 9; i++) {
			if (i % 3 != 0) {
				continue;
			}
			canvas.drawLine(0, i * height, getWidth(), i * height, dark);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1,
					hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(),
					hilite);
		}

		// Rysujemy cyferki
		FontMetrics fm = foreground.getFontMetrics();
		float x = width / 2;
		float y = height / 2 - (fm.ascent + fm.descent) / 2;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				canvas.drawText(this.game.getTitleString(i, j), i * width + x,
						j * height + y, foreground);
			}
		}

		// Rysujemy zaznacznie (aktywny kwadracik)
		// canvas.drawRect(selRect, selected);

		// Rysujemy podpowiedzi
		int c[] = { getResources().getColor(R.color.puzzle_hint_0),
				getResources().getColor(R.color.puzzle_hint_1),
				getResources().getColor(R.color.puzzle_hint_2), };
		Rect r = new Rect();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int movesLeft = 9 - game.getUsedTiles(i, j).length;
				if(movesLeft < c.length) {
					getRect(i, j, r);
					hint.setColor(c[movesLeft]);
					canvas.drawRect(r, hint);
				}
			}
		}

		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN) {
			return super.onTouchEvent(event);
		}

		// Zaznaczamy aktualnie wybrany kwadracik
		select((int) (event.getX() / width), (int) (event.getY() / height));
		game.showKeypadOrError(selX, selY);

		return true;
	}

	public void setSelectedTile(int tile) {
		if (game.setTileIfValid(selX, selY, tile)) {
			invalidate();
		} else {
			Log.d(TAG, "selectedTile: invalid " + tile);
		}

	}

	private void select(int x, int y) {
		invalidate(selRect);
		selX = Math.min(Math.max(x, 0), 8);
		selY = Math.min(Math.max(y, 0), 8);
		getRect(selX, selY, selRect);
		invalidate(selRect);
	}

	private void getRect(int x, int y, Rect rec) {
		rec.set((int) (x * width), (int) (y * height),
				(int) (x * width + width), (int) (y * height + height));

	}

}
