package com.emilflach.cobot;



import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CoffeeViewHolder> {

    public static class CoffeeViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView coffeeName;
        ImageView coffeePhoto;
        SeekBar coffeeStrength;
        SeekBar coffeeMilk;
        SeekBar coffeeSugar;
        CheckBox coffeeMug;
        TextView coffeeLocation;
        ImageButton coffeeArrow;
        RelativeLayout coffeeWrap;
        RelativeLayout coffeeHeader;
        RelativeLayout coffeeBody;
        Button orderButton;
        Button atoButton;
        int coffeeType;
        Boolean collapsed = false;

        CoffeeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            coffeeName = (TextView)itemView.findViewById(R.id.coffee_name);
            coffeePhoto = (ImageView)itemView.findViewById(R.id.coffee_photo);
            coffeeStrength = (SeekBar)itemView.findViewById(R.id.seekBarStrength);
            coffeeMilk = (SeekBar)itemView.findViewById(R.id.seekBarMilk);
            coffeeSugar = (SeekBar)itemView.findViewById(R.id.seekBarSugar);
            coffeeMug = (CheckBox)itemView.findViewById(R.id.checkBoxMug);
            coffeeLocation = (TextView)itemView.findViewById(R.id.textViewLocation);
            coffeeArrow = (ImageButton)itemView.findViewById(R.id.coffee_arrow);
            coffeeWrap = (RelativeLayout)itemView.findViewById(R.id.coffee_wrap);
            coffeeHeader = (RelativeLayout)itemView.findViewById(R.id.header_wrap);
            coffeeBody = (RelativeLayout)itemView.findViewById(R.id.body_wrap);
            orderButton = (Button)itemView.findViewById(R.id.orderButton);
            atoButton = (Button)itemView.findViewById(R.id.addToOrderButton);
        }
    }

    List<Product> coffees;
    MainActivity mainActivity;

    RVAdapter(List<Product> coffees, MainActivity mainActivity){
        this.coffees = coffees;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onViewAttachedToWindow(CoffeeViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CoffeeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new CoffeeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CoffeeViewHolder holder, int i) {
        holder.coffeeName.setText(coffees.get(i).name);
        holder.coffeePhoto.setImageResource(coffeeImage(coffees.get(i).type));
        holder.coffeeType = coffees.get(i).type;
        holder.coffeeStrength.setProgress(coffees.get(i).strength);
        holder.coffeeMilk.setProgress(coffees.get(i).milk);
        holder.coffeeSugar.setProgress(coffees.get(i).sugar);
        holder.coffeeMug.setChecked(coffees.get(i).mug);
//        holder.coffeeLocation.setText(String.valueOf(coffees.get(i).location));

        holder.coffeeHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                collapse(holder);
            }
        });
        holder.orderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                order(holder);
            }
        });
        holder.atoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addToOrder(holder, 0);
            }
        });

        //Close most cards on startup
        if (i != 0) {
            close(holder);
        } else {
            MainActivity.theCard = holder;
        }

    }


    @Override
    public int getItemCount() {
        return coffees.size();
    }

    /**
     * Collapses a card also sets card height on first run
     * @param holder the selected CoffeeViewHolder
     */
    public void collapse(CoffeeViewHolder holder) {
        if(MainActivity.cardHeight == 0) {
            MainActivity.cardHeight = MainActivity.theCard.coffeeBody.getHeight();
        }

        if(holder.collapsed) {
            holder.collapsed = false;

            //resize card
            ResizeAnimation resizeAnimation = new ResizeAnimation(holder.coffeeBody, 0, MainActivity.cardHeight);
            resizeAnimation.setDuration(200);
            holder.coffeeWrap.startAnimation(resizeAnimation);

            //rotate arrow
            Animation an = new RotateAnimation(0.0f, -180.0f, holder.coffeeArrow.getPivotX(), holder.coffeeArrow.getPivotY());
            an.setDuration(200);
            an.setFillAfter(true);
            holder.coffeeArrow.setAnimation(an);

        } else {
            holder.collapsed = true;

            //resize card
            ResizeAnimation resizeAnimation = new ResizeAnimation(holder.coffeeBody, MainActivity.cardHeight, 0);
            resizeAnimation.setDuration(200);
            holder.coffeeWrap.startAnimation(resizeAnimation);

            //rotate arrow
            Animation an = new RotateAnimation(-180.0f, 0.0f, holder.coffeeArrow.getPivotX(), holder.coffeeArrow.getPivotY());
            an.setDuration(200);
            an.setFillAfter(true);
            holder.coffeeArrow.setAnimation(an);
        }
    }


    /**
     * Closes cards without animation
     * @param holder selected CoffeeViewHolder
     */
    public void close(CoffeeViewHolder holder) {
        holder.collapsed = true;
        ResizeAnimation resizeAnimation = new ResizeAnimation(holder.coffeeBody, MainActivity.cardHeight, 0);
        resizeAnimation.setDuration(0);
        holder.coffeeWrap.startAnimation(resizeAnimation);
    }


    class ResizeAnimation extends Animation {
        protected final int originalHeight;
        protected final View view;
        protected float perValue;

        public ResizeAnimation(View view, int fromHeight, int toHeight) {
            this.view = view;
            this.originalHeight = fromHeight;
            this.perValue = (toHeight - fromHeight);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            view.getLayoutParams().height = (int) (originalHeight + perValue * interpolatedTime);
            view.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }


    /**
     * Responds the corresponding integer of a drawable from coffee type
     * @param $i type of coffee
     * @return corresponding image
     */
    public int coffeeImage(int $i) {
        switch ($i) {
            case 0: //Black
                return R.drawable.black;
//            case 1: //Cappuccino
//                return R.drawable.black;
//            case 2: //Espresso
//                return R.drawable.black;
//            case 3: //Cafe au Lait
//                return R.drawable.black;
//            case 4: //Wiener Melange
//                return R.drawable.black;
//            case 5: //Double Espresso
//                return R.drawable.black;
//            case 6: //Cafe Mocca
//                return R.drawable.black;
//            case 7: //Cafe Macchiato
//                return R.drawable.black;
//            case 8: //Espresso choc
//                return R.drawable.black;
//            case 9://Hot Chocolate
//                return R.drawable.black;
            default:
                return R.drawable.black;
        }

    }


    /**
     * Creates an order for a user
     * @param holder The CoffeeViewHolder which has to be ordered
     */
    public void order(final CoffeeViewHolder holder) {
        ServiceGenerator.UserClient userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        Call<Order> call = userClient.createOrder(MainActivity.id);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    System.out.println("Order add success");
                    addToOrder(holder, response.body().id);
                } else {
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());
                    //TODO: User notification or a smarter action, add the product to a current order?
                    System.out.println("Failed");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    /**
     * Adds a product to an order
     * @param holder CoffeeViewHolder that should be added
     * @param id optional id for chaining to an order
     */
    public void addToOrder(final CoffeeViewHolder holder, int id) {
        if (id == 0) {
            id = MainActivity.currentOrderId;
            if (id == 0) {
                Log.d("No valid order id:", String.valueOf(id));
                //TODO: User notification
                return;
            }
        }
        ServiceGenerator.UserClient userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);

        Call<Product> call = userClient.createOrderProduct(
                id,
                String.valueOf(holder.coffeeName.getText()),
                holder.coffeeType,
                holder.coffeeType,
                holder.coffeeStrength.getProgress(),
                holder.coffeeMilk.getProgress(),
                holder.coffeeSugar.getProgress(),
                holder.coffeeMug.isChecked() ? 1 : 0
        );

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Response<Product> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    System.out.println("Product add success");

                    mainActivity.setAdapter();
                    //TODO: User notification
                } else {
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());
                    //TODO: User notification
                    System.out.println("Failed");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

    }
}



