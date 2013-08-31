package pl.kitek.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends Fragment {

	public static final String TAG = "DetailFragment";
	public static final String ARG_POSITION = "position";

	private TextView detailsTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		Log.d(TAG, "onCreateView");
		
		View view = inflater.inflate(R.layout.details, container, false);
		detailsTitle = (TextView) view.findViewById(R.id.detailsTitle);
		detailsTitle.setText(getResources().getString(R.string.default_details));

		Bundle bundle = getArguments();

		if (bundle != null) {
			int position = bundle.getInt(ARG_POSITION);
			updateDetailsView(position);
		}

		return view;
	}

	public void updateDetailsView(int position) {
		Log.d(TAG, "updateDetailsView detailsTitle==null? " + (detailsTitle == null ? "yes" : "no") + " getView()==null?" + (getView() == null ? "yes" : "no"));
		
		//detailsTitle = (TextView) getView().findViewById(R.id.detailsTitle);
		detailsTitle.setText("Selected item: " + Integer.toString(position));
	}
}
