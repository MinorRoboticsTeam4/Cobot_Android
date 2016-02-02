package com.emilflach.cobot.ViewControllers.Adapters;

/**
 * Cobot
 * by Emil on 2015-11-04.
 */
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.ApiError;
import com.emilflach.cobot.Models.Location;
import com.emilflach.cobot.Models.Product;
import com.emilflach.cobot.Models.User;
import com.emilflach.cobot.R;
import com.emilflach.cobot.api.ErrorUtils;
import com.emilflach.cobot.api.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LocationsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ServiceGenerator.UserClient userClient;
    CobotMain cobotMain;
    List<Location> locations;
    Toast toast;

    public static class LocationViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemWrap;
        TextView locationName;
        int locationId;

        LocationViewHolder(View view) {
            super(view);
            itemWrap = (RelativeLayout) view.findViewById(R.id.item_wrap);
            locationName = (TextView) view.findViewById(R.id.item_description);
        }
    }

    public LocationsRVAdapter(List<Location> locations, CobotMain cobotMain){
        this.cobotMain = cobotMain;
        this.locations = locations;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_item, viewGroup, false);
        toast = Toast.makeText(cobotMain.getApplicationContext(), "Notification", Toast.LENGTH_SHORT);
        return new LocationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int i) {
        final LocationViewHolder h = (LocationViewHolder) holder;
        h.locationName.setText(locations.get(i).getName());
        h.locationId = locations.get(i).getId();
        h.itemWrap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setLocation(h);
            }
        });


    }

    @Override
    public int getItemCount() {
        return locations.size();
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

    public void setLocation(final LocationViewHolder holder) {

        Call<User> call = userClient.setLocation(CobotMain.id, holder.locationId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    setLocation(String.valueOf(holder.locationName.getText()), holder.locationId);
                    cobotMain.setAdapter(0);
                    toast.setText("Set location");
                    toast.show();
                } else {
                    toast.setText("Something went wrong!");
                    toast.show();
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("Order error message", error.message());

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error set location", t.getMessage());
                toast.setText("Something went wrong!");
                toast.show();
            }
        });
    }

    public void setLocation(String location, int location_id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cobotMain);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("location", location);
        editor.putInt("location_id", location_id);
        editor.apply();
    }

}



