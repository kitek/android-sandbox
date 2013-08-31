package pl.kitek.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import pl.kitek.fragments.ListFragment;

public class MainActivity extends FragmentActivity implements ListFragment.OnItemListSelected {

	public static final String TAG = "FragmentsMainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.d(TAG, "onCreate");

		// Check that the activity is using the layout version with
		// the fragment_container FrameLayout
		if (findViewById(R.id.fragmentContainer) != null ) {
			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			
			if (savedInstanceState != null) {
				Log.d(TAG, "onCreate return savedInstanceState");
				return;
			}
			
			FragmentTransaction fragmentsTransation = getSupportFragmentManager().beginTransaction();
			ListFragment listFragment = new ListFragment();
			fragmentsTransation.replace(R.id.fragmentContainer, listFragment);
			fragmentsTransation.commit();
		}
	}

	@Override
	public void onItemSelected(int position) {
		Log.d(TAG, "onItemSelected " + Integer.toString(position));

		DetailFragment detailsFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.rightFragment);
		if (detailsFragment != null && detailsFragment.isInLayout()) {
			// If details fragment is available, we're in two-pane layout...
			// Call a method in the DetailsFragment to update its content
			detailsFragment.updateDetailsView(position);
		} else {
			// Otherwise, we're in the one-pane layout and must swap frags...
			// Create fragment and give it an argument for the selected article
			DetailFragment newFragment = new DetailFragment();
			Bundle args = new Bundle();
			args.putInt(DetailFragment.ARG_POSITION, position);
			newFragment.setArguments(args);

			FragmentTransaction fragmentsTransation = getSupportFragmentManager().beginTransaction();
			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack so the user can
			// navigate back
			fragmentsTransation.replace(R.id.fragmentContainer, newFragment);
			fragmentsTransation.addToBackStack(null);
			fragmentsTransation.commit();
		}

	}

}
