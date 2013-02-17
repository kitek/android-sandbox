package szkolenie.tasklist;

import szkolenie.tasklist.TaskContract.Task;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class TaskQueries {
	
	public static void insertTask(String name, String description, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(Task.C_NAME, name);
		values.put(Task.C_DESCR, description);
		
		db.insert(Task.TASK_TABLE, null, values);
	}
	
	public static void insertTask(SQLiteDatabase writableDatabase, ContentValues vals) {
		
		writableDatabase.insert(Task.TASK_TABLE, null, vals);
	}
	
	public static String[] tasksColumns = {Task._ID, Task.C_NAME, Task.C_DESCR};
	
	public static Cursor selectTasks(SQLiteDatabase db) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(Task.TASK_TABLE);
		return qb.query(db, tasksColumns, null, null, null, null, null);
	}
	
	public static Cursor selectTasks(SQLiteDatabase db, Long id) {
		return db.query(
				Task.TASK_TABLE, 
				tasksColumns, 
				Task._ID + " = ?",
				new String[]{Long.toString(id)}, null, null, null);
	}
	
	public static void updateTask(SQLiteDatabase db, Long id, ContentValues values) {
		db.update(Task.TASK_TABLE, values, Task._ID + " = ?", new String[]{Long.toString(id)});
	}
	
	public static void deleteTask(SQLiteDatabase db, Long id) {
		db.delete(Task.TASK_TABLE, Task._ID + " = ?", new String[] {Long.toString(id)});
	}
	
	public static void deleteTasks(SQLiteDatabase db) {
		db.delete(Task.TASK_TABLE, null, null);
		
	}

	
	
}
