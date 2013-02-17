package szkolenie.tasklist;

import android.provider.BaseColumns;

public interface TaskContract {
	public String DB_NAME = "tasks";
	public int DB_VER = 1;
	
	public interface Task extends BaseColumns {
		public String TASK_TABLE = "task";
		public String C_NAME = "name";
		public String C_DESCR = "description";
	}
}

