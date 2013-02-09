package pl.kitek.universalgridviewimages;

// https://github.com/nostra13/Android-Universal-Image-Loader
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	String[] imageUrls = new String[] {
			"http://dl.dropbox.com/u/2804933/android-sandbox/drawable/kot_1.jpg",
			"http://dl.dropbox.com/u/2804933/android-sandbox/drawable/kot_2.jpg",
			"http://dl.dropbox.com/u/2804933/android-sandbox/drawable/kot_3.jpg",
			"http://dl.dropbox.com/u/2804933/android-sandbox/drawable/kot_4.jpg",
			"http://dl.dropbox.com/u/2804933/android-sandbox/drawable/kot_5.jpg",
			"http://dl.dropbox.com/u/2804933/android-sandbox/drawable/kot_6.jpg",
			"http://dl.dropbox.com/u/2804933/android-sandbox/drawable/kot_7.jpg",
			"http://dl.dropbox.com/u/2804933/android-sandbox/drawable/kot_8.jpg",
			"http://tabletpcssource.com/wp-content/uploads/2011/05/android-logo.png",
			"http://simpozia.com/pages/images/stories/windows-icon.png",
			"https://si0.twimg.com/profile_images/1135218951/gmail_profile_icon3_normal.png",
			"http://www.krify.net/wp-content/uploads/2011/09/Macromedia_Flash_dock_icon.png",
			"http://radiotray.sourceforge.net/radio.png",
			"http://www.bandwidthblog.com/wp-content/uploads/2011/11/twitter-logo.png",
			"http://weloveicons.s3.amazonaws.com/icons/100907_itunes1.png",
			"http://weloveicons.s3.amazonaws.com/icons/100929_applications.png"
	};

	DisplayImageOptions options;
	ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty).cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(options)
				.build();

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);

		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(new ImageAdaper());
		gridView.setOnScrollListener(new PauseOnScrollListener(true, true));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_clear_memory_cache:
			imageLoader.clearMemoryCache();
			Toast.makeText(this, "Memory cache cleared", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.menu_clear_disc_cache:
			imageLoader.clearDiscCache();
			Toast.makeText(this, "Disc cache cleared", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return false;
		}
	}

	public class ImageAdaper extends BaseAdapter {

		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(
						R.layout.single_item, parent, false);
			} else {
				imageView = (ImageView) convertView;
			}
			imageLoader.displayImage(imageUrls[position], imageView, options);
			return imageView;
		}
	}

}
