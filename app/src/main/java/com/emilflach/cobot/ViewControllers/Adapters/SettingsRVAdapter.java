package com.emilflach.cobot.ViewControllers.Adapters;

/**
 * Cobot
 * by Emil on 2015-11-04.
 */
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.ApiError;
import com.emilflach.cobot.Models.ApiMessage;
import com.emilflach.cobot.Models.Order;
import com.emilflach.cobot.Models.Product;
import com.emilflach.cobot.R;
import com.emilflach.cobot.api.ErrorUtils;
import com.emilflach.cobot.api.ServiceGenerator;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SettingsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ServiceGenerator.UserClient userClient;

    public static class LogoutViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemWrap;

        LogoutViewHolder(View view) {
            super(view);
            itemWrap = (RelativeLayout) view.findViewById(R.id.item_wrap);
        }
    }

    CobotMain cobotMain;

    public SettingsRVAdapter(CobotMain cobotMain){
        this.cobotMain = cobotMain;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        switch (type) {
            case 0:
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.logout_item, viewGroup, false);
                return new LogoutViewHolder(v);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int i) {
        switch (i) {
            case 0:
                LogoutViewHolder h = (LogoutViewHolder) holder;
                h.itemWrap.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        logout();
                    }
                });
            default:
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }


    /**
     * Returns type as position
     * @param position position of the adapter
     * @return returns the type of adapter
     */
    @Override
    public int getItemViewType(int position) {
        return position;
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

}



