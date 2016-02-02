package com.emilflach.cobot.ViewControllers.Adapters;

/**
 * Cobot
 * by Emil on 2015-11-04.
 */
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.Setting;
import com.emilflach.cobot.R;
import android.support.v4.app.Fragment;

import com.emilflach.cobot.ViewControllers.Fragments.LocationsFragment;
import com.emilflach.cobot.ViewControllers.Fragments.SettingsFragment;
import com.emilflach.cobot.api.ServiceGenerator;

import java.util.List;

public class SettingsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ServiceGenerator.UserClient userClient;
    CobotMain cobotMain;
    SettingsFragment settingsFragment;
    List<Setting> settings;
    Toast toast;

    public static class SettingsViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemWrap;
        TextView item_description;
        ImageView item_icon;
        int settingId;

        SettingsViewHolder(View view) {
            super(view);
            itemWrap = (RelativeLayout) view.findViewById(R.id.item_wrap);
            item_description = (TextView) view.findViewById(R.id.item_description);
            item_icon = (ImageView) view.findViewById(R.id.item_icon);
        }
    }

    public SettingsRVAdapter(List<Setting> settings, CobotMain cobotMain, SettingsFragment settingsFragment){
        this.cobotMain = cobotMain;
        this.settings = settings;
        this.settingsFragment = settingsFragment;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.settings_item, viewGroup, false);
        toast = Toast.makeText(cobotMain.getApplicationContext(), "Notification", Toast.LENGTH_SHORT);
        return new SettingsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int i) {
        final SettingsViewHolder h = (SettingsViewHolder) holder;
        h.item_description.setText(settings.get(i).getName());
        h.settingId = settings.get(i).getId();
        h.itemWrap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (h.settingId == 1) {
                    settingsFragment.openLocation();
                }
                if (h.settingId == 2) {
                    settingsFragment.openInformation();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    /**
     * Logs out user by removing credentials from sharedpreferences
     */
    public void logout() {
        //TODO: Move this away from sharedpreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cobotMain);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id", 0);
        editor.putString("email", null);
        editor.putString("password", null);
        editor.apply();
        CobotMain.currentOrderId = 0;
        cobotMain.setAdapter(0);
    }

//    public static int settingIcon(int i) {
//        switch (i) {
//            case 0: //nfc icon
//                return R.drawable.coffee_black;
//            case 1: //Map icon
//                return R.drawable.ic_dialog_map;
//            case 2: //i icon
//                return R.drawable.ic_dialog_info;
//
//            default:
//                return R.drawable.black;
//        }
//    }

}



