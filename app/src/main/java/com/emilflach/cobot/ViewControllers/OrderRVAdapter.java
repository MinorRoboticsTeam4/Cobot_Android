package com.emilflach.cobot.ViewControllers;

/**
 * Cobot
 * by Emil on 2015-11-04.
 */
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emilflach.cobot.Models.Product;
import com.emilflach.cobot.R;

import java.util.List;

public class OrderRVAdapter extends RecyclerView.Adapter<OrderRVAdapter.COVHolder> {

    //    public static class StatusViewHolder extends RecyclerView.ViewHolder {
//
//        TextView status;
//        TextView location;
//        TextView time;
//
//        StatusViewHolder(View view) {
//            super(view);
//
//        }
//
//    }

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

    OrderRVAdapter(List<Product> coffees){
        this.coffees = coffees;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public COVHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item, viewGroup, false);

        //        if(i == 0) {
//            StatusViewHolder svh = new StatusViewHolder(v);
//        } else {
//
//        }


        return new COVHolder(v);
    }

    @Override
    public void onBindViewHolder(final COVHolder coffeeViewHolder, int i) {
        coffeeViewHolder.coffeeName.setText(coffees.get(i).getName());
        coffeeViewHolder.coffeeStrength.setText(String.valueOf(coffees.get(i).getStrength()));
        coffeeViewHolder.coffeeMilk.setText(String.valueOf(coffees.get(i).getMilk()));
        coffeeViewHolder.coffeeSugar.setText(String.valueOf(coffees.get(i).getSugar()));
        coffeeViewHolder.coffeeMug.setText(coffees.get(i).isMug() ? "Yes" : "No");
//        coffeeViewHolder.coffeePhoto.setImageResource(coffees.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return coffees.size();
    }
}



