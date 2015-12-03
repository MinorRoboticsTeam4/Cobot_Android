package com.emilflach.cobot;

/**
 * Created by Emil on 2015-11-04.
 */
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class RVAdapterOrders extends RecyclerView.Adapter<RVAdapterOrders.COVHolder> {

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

    RVAdapterOrders(List<Product> coffees){
        this.coffees = coffees;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public COVHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_orders, viewGroup, false);
        COVHolder cvh = new COVHolder(v);


        //        if(i == 0) {
//            StatusViewHolder svh = new StatusViewHolder(v);
//        } else {
//
//        }


        return cvh;
    }

    @Override
    public void onBindViewHolder(final COVHolder coffeeViewHolder, int i) {
        coffeeViewHolder.coffeeName.setText(coffees.get(i).name);
        coffeeViewHolder.coffeeStrength.setText(String.valueOf(coffees.get(i).strength));
        coffeeViewHolder.coffeeMilk.setText(String.valueOf(coffees.get(i).milk));
        coffeeViewHolder.coffeeSugar.setText(String.valueOf(coffees.get(i).sugar));
        coffeeViewHolder.coffeeMug.setText(coffees.get(i).mug ? "Yes" : "No");
//        coffeeViewHolder.coffeePhoto.setImageResource(coffees.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return coffees.size();
    }
}



