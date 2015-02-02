package solutions.alterego.android.unisannio.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import solutions.alterego.android.unisannio.MainActivity;
import solutions.alterego.android.unisannio.R;

public class MapFragment extends Fragment {

    private static View view;

    private static GoogleMap mMap;

    private static MainActivity mActivity;

    private List<UniPoint> mMarkers = new ArrayList<UniPoint>();

    public static Fragment newInstance(List<UniPoint> markers) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("MARKERS", (ArrayList<? extends android.os.Parcelable>) markers);

        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * ** Sets up the map if it is possible to do so ****
     */
    public static void setUpMapIfNeeded(List<UniPoint> markers) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFragment = (SupportMapFragment) mActivity.getSupportFragmentManager()
                    .findFragmentById(R.id.location_map);
            if (mapFragment != null) {
                mMap = mapFragment.getMap();
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    setUpMap(markers);
                }
            }
        }
    }

    private static void setUpMap(List<UniPoint> markers) {
        // For showing a move to my location button
        mMap.setMyLocationEnabled(true);

        for (UniPoint uniPoint : markers) {
            mMap.addMarker(new MarkerOptions().position(uniPoint.getGeopoint()).title(uniPoint.getName()).snippet(uniPoint.getAddress()));
        }

        // For zooming automatically to the last Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(markers.size() - 1).getGeopoint(), 16.0f));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        try {
            view = inflater.inflate(R.layout.fragment_maps, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        Bundle bundle = getArguments();
        mMarkers = bundle.getParcelableArrayList("MARKERS");

        setUpMapIfNeeded(mMarkers);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (mMap != null) {
            setUpMap(mMarkers);
        }

        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFragment = (SupportMapFragment) mActivity.getSupportFragmentManager()
                    .findFragmentById(R.id.location_map);
            if (mapFragment != null) {
                mMap = mapFragment.getMap();
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    setUpMap(mMarkers);
                }
            }
        }
    }

    /**
     * * The mapfragment's id must be removed from the FragmentManager *** or else if the same it is passed on the next time then *** app will crash
     * ***
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (mMap != null) {
                mActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .remove(mActivity.getSupportFragmentManager().findFragmentById(R.id.location_map))
                        .commit();
                mMap = null;
            }
        } catch (IllegalStateException e) {
            // Just gimme a break!
        }
    }
}