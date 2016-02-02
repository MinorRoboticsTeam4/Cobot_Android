package com.emilflach.cobot.ViewControllers.Fragments;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.Setting;
import com.emilflach.cobot.R;
import com.emilflach.cobot.ViewControllers.Adapters.SettingsRVAdapter;
import com.emilflach.cobot.api.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

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

            List<Setting> settings = getSettings();
            CobotMain cobotMain = (CobotMain) getActivity();
            SettingsRVAdapter adapter = new SettingsRVAdapter(settings, cobotMain, this);
            rv.setAdapter(adapter);
        }
    }

    /**
     * Gets all the locations and sets the adapter with this data
     */
    private List<Setting> getSettings() {
        List<Setting> settings = new ArrayList<>();
        settings.add(new Setting(0, "Set nfc mug"));
        settings.add(new Setting(1, "Set location"));
        settings.add(new Setting(2, "Cobot information"));
        return settings;
    }

    /**
     * Opens the location fragment
     */
    public void openLocation() {
        this.getFragmentManager().beginTransaction()
                .replace(R.id.cl, new LocationsFragment())
                .addToBackStack(null)
                .commit();
    }

    /**
     * Opens the location fragment
     */
    public void openInformation() {
        this.getFragmentManager().beginTransaction()
                .replace(R.id.cl, new InformationFragment())
                .addToBackStack(null)
                .commit();
    }


}
