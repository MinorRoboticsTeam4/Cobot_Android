package com.emilflach.cobot.ViewControllers;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.Order;
import com.emilflach.cobot.Models.Product;
import com.emilflach.cobot.R;

import java.util.List;

public class OrderRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

        }
    }

    List<Product> coffees;
    Order order;

    OrderRVAdapter(List<Product> coffees, Order order){
        this.coffees = coffees;
        this.order = order;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        if(type == 0) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.status_item, viewGroup, false);
            return new StatusViewHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item, viewGroup, false);
            return new COVHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if (i == 0) {
            StatusViewHolder h = (StatusViewHolder) holder;
            h.location.setText(String.valueOf(order.getDelivery_status()));
            h.status.setText(String.valueOf(order.getLocation()));
            h.time.setText(String.valueOf(order.getDelivered_at()));
        } else {
            i = i - 1;
            COVHolder h = (COVHolder) holder;
            h.coffeeName.setText(coffees.get(i).getName());
            h.coffeePhoto.setImageResource(CobotMain.coffeeImage(coffees.get(i).getType()));
            h.coffeeStrength.setText(String.valueOf(coffees.get(i).getStrength()));
            h.coffeeMilk.setText(String.valueOf(coffees.get(i).getMilk()));
            h.coffeeSugar.setText(String.valueOf(coffees.get(i).getSugar()));
            h.coffeeMug.setText(coffees.get(i).isMug() ? "Yes" : "No");
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
}



