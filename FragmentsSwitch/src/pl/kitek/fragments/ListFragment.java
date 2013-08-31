package pl.kitek.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListFragment extends Fragment {

	private OnItemListSelected callback;
	private ListView listview;
	private String[] values = new String[] { "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
			"OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2", "Android", "iPhone", "WindowsMobile" };

	// Container Activity must implement this interface
	public interface OnItemListSelected {
		public void onItemSelected(int position);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		SimpleArrayAdapter listAdapter = new SimpleArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, values);

		View view = inflater.inflate(R.layout.list, container, false);
		listview = (ListView) view.findViewById(R.id.simpleList);
		listview.setAdapter(listAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				callback.onItemSelected(position);
			}
		});

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			callback = (OnItemListSelected) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
		}

	}

	private class SimpleArrayAdapter extends ArrayAdapter<String> {

		public SimpleArrayAdapter(Context context, int resource, String[] objects) {
			super(context, resource, objects);
		}

	}
}
