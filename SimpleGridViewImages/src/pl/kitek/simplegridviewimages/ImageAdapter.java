package pl.kitek.simplegridviewimages;

import pl.kitek.simplegridviewimages.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private Integer[] mThumbsIds = { 
			R.drawable.kot_1, R.drawable.kot_2,
			R.drawable.kot_3, R.drawable.kot_4, R.drawable.kot_5,
			R.drawable.kot_6, R.drawable.kot_7, R.drawable.kot_8,
			R.drawable.kot_1, R.drawable.kot_2,
			R.drawable.kot_3, R.drawable.kot_4, R.drawable.kot_5,
			R.drawable.kot_6, R.drawable.kot_7, R.drawable.kot_8,
			R.drawable.kot_1, R.drawable.kot_2,
			R.drawable.kot_3, R.drawable.kot_4, R.drawable.kot_5,
			R.drawable.kot_6, R.drawable.kot_7, R.drawable.kot_8
	};

	public ImageAdapter(Context c) {
		mContext = c;
	}

	@Override
	public int getCount() {
		return mThumbsIds.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(mThumbsIds[position]);
		return imageView;
	}

}
