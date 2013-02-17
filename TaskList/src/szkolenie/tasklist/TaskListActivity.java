package szkolenie.tasklist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.WeakHashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import szkolenie.tasklist.TaskContract.Task;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskListActivity extends Activity implements OnItemClickListener {

	private static final int CONTEXT_MENU_DELETE = 1;
	private static final int CON_TIMEOUT = 20000;
	private static final int SO_TIMEOUT = 20000;
	private ListView taskListView;
	private WeakHashMap<View, Long> rowList = new WeakHashMap<View, Long>();
	
	private class CursorLoader extends AsyncTask<TaskDb, Integer, Cursor> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Cursor doInBackground(szkolenie.tasklist.TaskDb... params) {
			return TaskQueries.selectTasks(params[0].getReadableDatabase());
		}
		
		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			adapter.changeCursor(result);
		}
		
	}
	
	private class MyAdapter extends CursorAdapter implements OnClickListener {

		public MyAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
		}

		@Override
		public void bindView(View view, Context arg1, Cursor cursor) {
			TextView nameTextView = (TextView) view.findViewById(R.id.lab_name);
			TextView descriptionTextView = (TextView) view.findViewById(R.id.lab_descr);

			String name = cursor.getString(cursor.getColumnIndex(Task.C_NAME));
			String description = cursor.getString(cursor.getColumnIndex(Task.C_DESCR));

			nameTextView.setText(name);
			descriptionTextView.setText(description);
			
			Long id = cursor.getLong(cursor.getColumnIndex(Task._ID));
			rowList.put(nameTextView, id);
			
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			
			View res = View.inflate(TaskListActivity.this, R.layout.single_item, null);
			TextView nameTv = (TextView)res.findViewById(R.id.lab_name);
			
			nameTv.setClickable(true);
			nameTv.setOnClickListener(this);
			
			return res;
		}

		@Override
		public void onClick(View v) {
			Long rowId = rowList.get(v);
			Toast.makeText(TaskListActivity.this, "Klik na name o id = "+rowId, Toast.LENGTH_SHORT).show();
		}

	};

	private MyAdapter adapter;
	private TaskDb TaskDb;
	private SQLiteDatabase db;
	private Cursor cursor;
	private CursorLoader asyncTask;
	private DefaultHttpClient httpClient;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);

		taskListView = (ListView) findViewById(R.id.task_list);
		taskListView.setOnItemClickListener(this);
		
		registerForContextMenu(taskListView);

		adapter = new MyAdapter(this, null, false);
		taskListView.setAdapter(adapter);
		
		httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), CON_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), SO_TIMEOUT);
		
		
		
	}
	
	private String createWriteRequest() throws JSONException {
		String devID = getDeviceId();
		JSONObject request = new JSONObject();
		request.put("id", devID);
		request.put("tasks", readTaskList());
		return request.toString();
	}
	
	private JSONArray readTaskList() throws JSONException {
		Cursor cursor = null;
		JSONArray retult = new JSONArray();
		try {
			cursor = TaskQueries.selectTasks(TaskDb.getReadableDatabase());
			if(cursor.moveToFirst()) {
				do {
					JSONObject taskObject = new JSONObject();
					taskObject.put(Task._ID, cursor.getLong(cursor.getColumnIndex(Task._ID)));
					taskObject.put(Task.C_NAME, cursor.getString(cursor.getColumnIndex(Task.C_NAME)));
					taskObject.put(Task.C_DESCR, cursor.getString(cursor.getColumnIndex(Task.C_DESCR)));
					retult.put(taskObject);
				} while(cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.d("BUBA",e.getMessage());
		} finally {
			if(cursor!=null) cursor.close();
	 	} 
		return retult; 
	}
	
	private String getDeviceId() {
		return "id_"+Secure.getString(getContentResolver(), Secure.ANDROID_ID); 
	}
	
	private String sendTasks() throws JSONException, ClientProtocolException, IOException {
		HttpPost post = new HttpPost("http://kolektor.arceo.pl/szk/write");
		StringEntity reqEntity = new StringEntity(createWriteRequest(), "UTF-8");
		reqEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		reqEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, HTTP.UTF_8));
		
		post.setEntity(reqEntity);
		
		HttpResponse response = null;
		synchronized (httpClient) {
			response = httpClient.execute(post);
		}
		String result = readHttpResult(response);
		return result;
	}
	
	private String readHttpResult(HttpResponse response) throws IllegalStateException, IOException {
		InputStream content = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		
		StringBuilder result = new StringBuilder();
		String line = null;
		while ((line=reader.readLine())!=null) {
			result.append(line).append("\n");
		}
		content.close();
		return result.toString();
	}
	
	private void reloadList() {
		TaskDb = new TaskDb(this, TaskContract.DB_NAME, null, TaskContract.DB_VER);
		
		asyncTask = new CursorLoader();
		asyncTask.execute(TaskDb);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		reloadList();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (cursor != null) {
			cursor.close();
		}
		db.close();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo aMenuInfo = (AdapterContextMenuInfo) menuInfo;
		
		menu.add(0, CONTEXT_MENU_DELETE, 1, getString(R.string.context_delete));
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ContextMenuInfo menuInfo = item.getMenuInfo();
		AdapterContextMenuInfo aMenuInfo = (AdapterContextMenuInfo) menuInfo;
		
		TaskQueries.deleteTask(TaskDb.getWritableDatabase(), aMenuInfo.id);
		reloadList();
		
		/*
		AlertDialog.Builder adb=new AlertDialog.Builder(TaskListActivity.this);
		adb.setTitle("Usunaç?");
        adb.setMessage("Czy jesteÊ pewien, ˝e chesz usunaç pozycje " + aMenuInfo.id);
        adb.setNegativeButton("Anuluj", null);
        adb.setPositiveButton("Usuƒ", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//Toast.makeText(TaskListActivity.this, "menu ctx id = ", Toast.LENGTH_SHORT).show();
				//TaskQueries.deleteTask(TaskDb.getWritableDatabase(), aMenuInfo.id);
			}
        	
        });
        adb.show();
		*/
		
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_task_list, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_send:
				String response = "";
				
				try {
					response = sendTasks();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				JSONObject result = null;
				try {
					result = new JSONObject(response);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			try {
				if("OK".equals(result.getString("response"))) {
					Toast.makeText(this, "JEST OK", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this, "JEST NIE OK", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
				Toast.makeText(this, response, Toast.LENGTH_LONG).show();
				
				return true;
			case R.id.add_new_item:
				startActivity(new Intent(this, MainActivity.class));
				return true;
			
			case R.id.menu_import:
				try {
					importData();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				reloadList();
				
				return true;
			default:
			    return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent changeData = new Intent(this, MainActivity.class);
		changeData.setAction(Intent.ACTION_EDIT);
		changeData.putExtra("id", arg3);
		
		//startActivity(changeData);
		startActivityForResult(changeData, 1);
		
		Toast.makeText(this, "row id = "+arg3, Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 1) {
			if(resultCode==Activity.RESULT_OK) {
				long editedId = data.getExtras().getLong("id");
				Toast.makeText(this, "edited id = "+editedId, Toast.LENGTH_SHORT).show();
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private String prepareReadRequest() throws JSONException {
		JSONObject req = new JSONObject();
		req.put("id", getDeviceId());
		return req.toString();
	}
	
	private String sendReadRequest() throws JSONException, ClientProtocolException, IOException {
		HttpPost post = new HttpPost("http://kolektor.arceo.pl/szk/read");
		String request = prepareReadRequest();
		StringEntity reqEntity = new StringEntity(request, "UTF-8");
		reqEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		reqEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, HTTP.UTF_8));
		
		post.setEntity(reqEntity);
		
		HttpResponse response = null;
		synchronized (httpClient) {
			response = httpClient.execute(post);
		}
		String result = readHttpResult(response);
		return result;
	}
	
	
	private void importData() throws ClientProtocolException, JSONException, IOException {
		String sendReadRequest = sendReadRequest();
		JSONObject result = new JSONObject(sendReadRequest);
		// Je˝eli coÊ poszlo nie tak to konczymy import
		if(!"OK".equals(result.getString("response"))) return;
		
		JSONObject data = result.getJSONObject("data");
		JSONArray tasks = data.getJSONArray("tasks");
		
		TaskQueries.deleteTasks(TaskDb.getWritableDatabase());
		
		for(int i=0;i<tasks.length();i++) {
			JSONObject task = tasks.getJSONObject(i);

			ContentValues vals = new ContentValues();
			vals.put(Task._ID, task.getLong(Task._ID));
			vals.put(Task.C_NAME, task.getString(Task.C_NAME));
			vals.put(Task.C_DESCR, task.getString(Task.C_DESCR));

			// Dodajemy rekordy do bazy
			TaskQueries.insertTask(TaskDb.getWritableDatabase(), vals);
		}
		
		Toast.makeText(this, "Zaladowalem "+tasks.length()+" zadaƒ.", Toast.LENGTH_SHORT).show();
		
	}

}
