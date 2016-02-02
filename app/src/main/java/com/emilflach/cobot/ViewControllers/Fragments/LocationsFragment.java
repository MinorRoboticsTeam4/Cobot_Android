package com.emilflach.cobot.ViewControllers.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.ApiError;
import com.emilflach.cobot.Models.Location;
import com.emilflach.cobot.R;
import com.emilflach.cobot.ViewControllers.Adapters.LocationsRVAdapter;
import com.emilflach.cobot.api.ErrorUtils;
import com.emilflach.cobot.api.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * cobot
 * by Emil on 2015-11-07.
 */
public class LocationsFragment extends Fragment {

    ServiceGenerator.UserClient userClient;
    private RecyclerView rv;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public LocationsFragment() {
    }

    public static LocationsFragment newInstance(int sectionNumber) {
        LocationsFragment fragment = new LocationsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        return inflater.inflate(R.layout.locations_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Set an empty adapter on initialization after which data gets retrieved
        View v = getView();
        if (v != null) {
            rv = (RecyclerView) v.findViewById(R.id.rv);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);
            rv.setHasFixedSize(true);

            List<Location> locations = new ArrayList<>();

            CobotMain cobotMain = (CobotMain) getActivity();
            LocationsRVAdapter adapter = new LocationsRVAdapter(locations, cobotMain);
            rv.setAdapter(adapter);

            initializeLocations();

        }
    }

    /**
     * Gets all the locations and sets the adapter with this data
     */
    private void initializeLocations() {
        Call<List<Location>> call = userClient.locations();
        call.enqueue(new Callback<List<Location>>() {

            private List<Location> locations = new ArrayList<>();

            @Override
            public void onResponse(Response<List<Location>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    for (Location location : response.body()) {
                        this.locations.add(location);
                    }
                    CobotMain cobotMain = (CobotMain) getActivity();
                    LocationsRVAdapter adapter = new LocationsRVAdapter(locations, cobotMain);
                    rv.setAdapter(adapter);
                } else {
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }


}
