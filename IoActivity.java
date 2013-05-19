package szkolenie.storage;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IoActivity extends Activity {
  
	public static final String PREFS_NAME = "MyPrefsFile";

	
	public void saveText(View view) {
		
		EditText txt1 = (EditText) findViewById(R.id.txt1);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("name", txt1.getText().toString());
		
		editor.commit();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		EditText txt1 = (EditText) findViewById(R.id.txt1);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
	    String name = settings.getString("name", null);
	    
	    txt1.setText(name);
	    
	    
		
		showDirs();
		
		/*try {
			
			File saveDir = getFilesDir();
			File dstFile = new File(saveDir, "plik.txt");
			Writer writer = new OutputStreamWriter(openFileOutput("plik.txt", MODE_PRIVATE));
			writer.append("Lorem ipsum");
			writer.flush();
			writer.close();
			
			Reader reader = new InputStreamReader(openFileInput("plik.txt"));
			char[] buffer = new char[30];
			reader.read(buffer);
			
			msg(new String(buffer));
			reader.close();
			
			
			
			
			InputStream is = getAssets().open("aset.txt");
			reader = new InputStreamReader(is);
			reader.read(buffer);
			msg(new String(buffer));
			reader.close();
			
			is = getResources().openRawResource(R.raw.plik_raw);
			reader = new InputStreamReader(is);
			reader.read(buffer);
			msg(new String(buffer));
			reader.close();
		
		} catch(Exception e) {
			
		}*/
	    
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void msg(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
	private void showDirs() {
		String externalStorage = Environment.getExternalStorageState();
		TextView fsInfo = (TextView) findViewById(R.id.fsInfo);
		
		StringBuilder sb = new StringBuilder();
		sb.append("filesDir: "+getFilesDir()+"\n");
		sb.append("externalStorageState: "+externalStorage+"\n");
		sb.append("externaStorageFilesDir: " + Environment.getExternalStorageDirectory()+"\n");
		sb.append("externalStoragePublic: "+Environment.DIRECTORY_PICTURES+"\n");
		sb.append("external cache: "+getExternalCacheDir()+"\n");
		sb.append("cache: "+getCacheDir()+"\n");
		fsInfo.setText(sb.toString());
	}

}
