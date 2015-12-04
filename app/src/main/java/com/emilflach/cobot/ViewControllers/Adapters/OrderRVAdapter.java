package com.emilflach.cobot.ViewControllers.Adapters;

/**
 * Cobot
 * by Emil on 2015-11-04.
 */
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

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OrderRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ServiceGenerator.UserClient userClient;

    public static class StatusViewHolder extends RecyclerView.ViewHolder {

        TextView status;
        TextView location;
        TextView time;

        StatusViewHolder(View view) {
            super(view);
            status = (TextView) view.findViewById(R.id.statusText);
            time = (TextView) view.findViewById(R.id.timeText);
            location = (TextView) view.findViewById(R.id.locationText);
        }

    }

    public static class COVHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView coffeeName;
        TextView coffeeStrength;
        TextView coffeeMilk;
        TextView coffeeSugar;
        TextView coffeeMug;
        ImageView coffeePhoto;
        RelativeLayout coffeeWrap;
        ImageButton deleteButton;
        int productid = 0;

        COVHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            coffeeName = (TextView)itemView.findViewById(R.id.coffee_name);
            coffeePhoto = (ImageView)itemView.findViewById(R.id.coffee_photo);
            coffeeWrap = (RelativeLayout)itemView.findViewById(R.id.coffee_wrap);
            coffeeStrength = (TextView)itemView.findViewById(R.id.textViewStrength);
            coffeeMilk = (TextView)itemView.findViewById(R.id.textViewMilk);
            coffeeSugar = (TextView)itemView.findViewById(R.id.textViewSugar);
            coffeeMug = (TextView)itemView.findViewById(R.id.textViewMug);
            deleteButton = (ImageButton) itemView.findViewById(R.id.deleteButton);

        }
    }

    List<Product> coffees;
    Order order;
    CobotMain cobotMain;
    boolean isEmpty;

    public OrderRVAdapter(List<Product> coffees, Order order, CobotMain cobotMain, boolean isEmpty){
        this.coffees = coffees;
        this.order = order;
        this.cobotMain = cobotMain;
        this.isEmpty = isEmpty;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        if(type == 0) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.status_item, viewGroup, false);
            return new StatusViewHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item, viewGroup, false);
            return new COVHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int i) {
        if (i == 0) {
            StatusViewHolder h = (StatusViewHolder) holder;
            if(isEmpty) {
                h.location.setText(String.valueOf("No location set"));
                h.status.setText(String.valueOf(CobotMain.statusMessage(-1)));
                h.time.setText(String.valueOf("No estimated time"));
            } else {
                h.location.setText(String.valueOf(order.getLocation()));
                h.status.setText(String.valueOf(CobotMain.statusMessage(order.getDelivery_status())));
                h.time.setText(String.valueOf(order.getDelivered_at()));
            }
        } else {
            i = i - 1;
            final COVHolder h = (COVHolder) holder;
            h.productid = coffees.get(i).getId();
            h.coffeeName.setText(coffees.get(i).getName());
            h.coffeePhoto.setImageResource(CobotMain.coffeeImage(coffees.get(i).getType()));
            h.coffeeStrength.setText(String.valueOf(coffees.get(i).getStrength()));
            h.coffeeMilk.setText(String.valueOf(coffees.get(i).getMilk()));
            h.coffeeSugar.setText(String.valueOf(coffees.get(i).getSugar()));
            h.coffeeMug.setText(coffees.get(i).isMug() ? "Yes" : "No");
            h.deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    deleteProduct(h);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return coffees.size() + 1;
    }


    /**
     * Returns type 0 for position 0 and type 1 for everything else
     * @param position position of the adapter
     * @return returns the type of adapter
     */
    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }


    /**
     * Deletes selected product and resets the adapter to refresh data
     * @param h the coffeeviewholder of the product that has to be deleted
     */
    public void deleteProduct(COVHolder h) {
        Call<ApiMessage> call = userClient.deleteOrderProduct(CobotMain.currentOrderId, h.productid);
        call.enqueue(new Callback<ApiMessage>() {
            @Override
            public void onResponse(Response<ApiMessage> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    cobotMain.setAdapter(2);
                    Log.d("Message", response.body().message());
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

    public void clearData() {
        int size = this.coffees.size() + 1;
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.coffees.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }
}



