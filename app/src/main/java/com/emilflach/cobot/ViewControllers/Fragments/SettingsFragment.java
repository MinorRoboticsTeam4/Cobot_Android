package com.emilflach.cobot.ViewControllers.Fragments;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.emilflach.cobot.Models.Product;
import com.emilflach.cobot.R;
import com.emilflach.cobot.ViewControllers.Adapters.CoffeeRVAdapter;
import com.emilflach.cobot.ViewControllers.Adapters.SettingsRVAdapter;
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
public class SettingsFragment extends Fragment {

    ServiceGenerator.UserClient userClient;
    private RecyclerView rv;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(int sectionNumber) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        return inflater.inflate(R.layout.settings_fragment, container, false);
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

            CobotMain cobotMain = (CobotMain) getActivity();
            SettingsRVAdapter adapter = new SettingsRVAdapter(cobotMain);
            rv.setAdapter(adapter);

        }
    }


}
