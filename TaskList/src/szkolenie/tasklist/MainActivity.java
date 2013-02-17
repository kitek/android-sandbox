package szkolenie.tasklist;

import szkolenie.tasklist.TaskContract.Task;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

public class MainActivity extends Activity {

	private Animation animacja;
	private View przyciski;
	private EditText nameEdit;
	private EditText descrEdit;
	private TaskDb TaskDb;
	private boolean editMode;
	private long rowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		animacja = AnimationUtils.loadAnimation(this, R.anim.animacja);
		przyciski = findViewById(R.id.l_btns);
		przyciski.setAnimation(animacja);
		
		assignViews();
		TaskDb = new TaskDb(this, TaskContract.DB_NAME, null, TaskContract.DB_VER);
		
		Intent intent = getIntent();
		if(savedInstanceState!=null) {
			rowId = savedInstanceState.getLong("rowId");
			editMode = savedInstanceState.getBoolean("editMode");
		}
		if(Intent.ACTION_EDIT.equals(intent.getAction())) {
			editMode = true;
			rowId = intent.getExtras().getLong("id");
			loadData();
		} else {
			editMode = false;
		}
	}

	private void loadData() {
		Cursor selectTasks = null;
		try {
			selectTasks = TaskQueries.selectTasks(TaskDb.getReadableDatabase(), rowId);
			if(selectTasks.moveToFirst()) {
				String name = selectTasks.getString(selectTasks.getColumnIndex(Task.C_NAME));
				String desc = selectTasks.getString(selectTasks.getColumnIndex(Task.C_DESCR));
				
				nameEdit.setText(name);
				descrEdit.setText(desc);
				
			}
		} finally {
			if(selectTasks!=null) selectTasks.close();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {	
		super.onSaveInstanceState(outState);
		outState.putLong("rowId", rowId);
		outState.putBoolean("editMode", editMode);
	}

	private void assignViews() {
		nameEdit = (EditText) findViewById(R.id.nameText);
		descrEdit = (EditText) findViewById(R.id.descrText);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		animacja.start();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void onBtnCancelClick(View view) {
		// Klikniecie na anuluj
		finish();
	}
	
	public void onBtnOkClick(View view) {
		// Klikniecie na ok
		String name = nameEdit.getText().toString();
		String description = descrEdit.getText().toString();
		
		SQLiteDatabase db = TaskDb.getWritableDatabase();
		if(editMode) {
			ContentValues values = new ContentValues();
			values.put(Task.C_NAME, name);
			values.put(Task.C_DESCR, description);
			
			TaskQueries.updateTask(TaskDb.getWritableDatabase(), rowId, values);
			
			Intent resultIntent = new Intent();
			resultIntent.putExtra("id", rowId);
			setResult(RESULT_OK, resultIntent);
		} else {
			TaskQueries.insertTask(name, description, db);
		}
		
		finish();
	}

	
}

