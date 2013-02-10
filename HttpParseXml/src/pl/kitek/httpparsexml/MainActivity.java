package pl.kitek.httpparsexml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.xmlpull.v1.XmlPullParserException;

import pl.kitek.httpparsexml.AtomParser.Entry;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

// Zadania:
//  - æciˆgani«cie pliku XML z podanego adresu
//  - przeparsowanie pliku i zwr—cenie interesujˆcych nas danych (entry

public class MainActivity extends Activity {

	private static final int CON_TIMEOUT = 20000;
	private static final int SO_TIMEOUT = 20000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		InputStream content = null;
		StringBuilder result = new StringBuilder();
		HttpResponse response = null;
		AtomParser atomParser = new AtomParser();
		List<Entry> entries = null;
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), CON_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpClient.getParams(), SO_TIMEOUT);
			
			HttpGet httpGet = new HttpGet(getResources().getString(R.string.url));
			httpGet.setHeader("User-Agent", getResources().getString(R.string.user_agent));
			
			synchronized (httpClient) {
				response = httpClient.execute(httpGet);
			}
			content = response.getEntity().getContent();
			entries = atomParser.parse(content);
			
			for(Entry entry : entries) {
				result.append(entry.id.replace("tag:fotka.pl/photo/"+entry.title+"/", ""));
				result.append("|");
				result.append(entry.title);
				result.append("|");
				result.append(entry.link);
				
				result.append("\n");
			}
			
			TextView tv = (TextView) findViewById(R.id.tv);
			tv.setText(result.toString());
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(content != null) {
				try {
					content.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
