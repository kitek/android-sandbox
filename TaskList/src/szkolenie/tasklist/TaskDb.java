package szkolenie.tasklist;

import szkolenie.tasklist.TaskContract.Task;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDb extends SQLiteOpenHelper {

	private static final Object dbMonitor = new Object();
	
	public static Object getDbMonitor() {
		return dbMonitor;
	}
	
	public TaskDb(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+Task.TASK_TABLE+" (" 
				+ Task._ID+" INTEGER PRIMARY KEY,"
				+ Task.C_NAME+" TEXT,"
				+ Task.C_DESCR+" TEXT"
				+ ");"
				);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int arg2) {
		switch (oldVersion) {
		case 1:
		case 2:
			updateV2();
			break;

		default:
			break;
		}

	}

	private void updateV2() {

	}

}
