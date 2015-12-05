package com.emilflach.cobot.ViewControllers.Adapters;



import android.animation.ObjectAnimator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.ApiError;
import com.emilflach.cobot.Models.Order;
import com.emilflach.cobot.Models.Product;
import com.emilflach.cobot.R;
import com.emilflach.cobot.ViewControllers.Fragments.OrdersFragment;
import com.emilflach.cobot.api.ErrorUtils;
import com.emilflach.cobot.api.ServiceGenerator;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CoffeeRVAdapter extends RecyclerView.Adapter<CoffeeRVAdapter.CoffeeViewHolder> {

    ServiceGenerator.UserClient userClient;
    List<Product> coffees;
    CobotMain cobotMain;

    public CoffeeRVAdapter(List<Product> coffees, CobotMain cobotMain){
        this.coffees = coffees;
        this.cobotMain = cobotMain;
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.coffee_item, viewGroup, false);
        userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        return new CoffeeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CoffeeViewHolder holder, int i) {
        holder.productId = coffees.get(i).getId();
        holder.coffeeType = coffees.get(i).getType();
        holder.coffeeName.setText(coffees.get(i).getName());
        holder.coffeePhoto.setImageResource(CobotMain.coffeeImage(coffees.get(i).getType()));
        holder.coffeeStrength.setProgress(coffees.get(i).getStrength() * CobotMain.milkScale);
        holder.strengthValue.setText(String.valueOf(coffees.get(i).getStrength()));
        holder.coffeeMilk.setProgress(coffees.get(i).getMilk() * CobotMain.milkScale);
        holder.milkValue.setText(String.valueOf(coffees.get(i).getMilk()));
        holder.coffeeSugar.setProgress(coffees.get(i).getSugar() * CobotMain.milkScale);
        holder.sugarValue.setText(String.valueOf(coffees.get(i).getSugar()));
        holder.coffeeMug.setChecked(coffees.get(i).isMug());
//        holder.coffeeLocation.setText(String.valueOf(coffees.get(i).location));

        holder.coffeeStrength.setOnSeekBarChangeListener(seekBarUpdater(holder));
        holder.coffeeMilk.setOnSeekBarChangeListener(seekBarUpdater(holder));
        holder.coffeeSugar.setOnSeekBarChangeListener(seekBarUpdater(holder));
        holder.coffeeMug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateProduct(holder);
            }
        });

        if (CobotMain.currentOrderId == 0) {
            holder.orderButton.setText("Order");
        } else {
            holder.orderButton.setText("Add to order");
        }

        holder.orderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                orderButton(holder);
            }
        });

        holder.coffeeHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                collapse(holder);
            }
        });


        //Close most cards on startup
        if (i != 0) {
            close(holder);
        } else {
            CobotMain.theCard = holder;
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
        if(CobotMain.cardHeight == 0) {
            CobotMain.cardHeight = CobotMain.theCard.coffeeBody.getHeight();
        }

        if(holder.collapsed) {
            holder.collapsed = false;

            //resize card
            ResizeAnimation resizeAnimation = new ResizeAnimation(holder.coffeeBody, 0, CobotMain.cardHeight);
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
            ResizeAnimation resizeAnimation = new ResizeAnimation(holder.coffeeBody, CobotMain.cardHeight, 0);
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
        ResizeAnimation resizeAnimation = new ResizeAnimation(holder.coffeeBody, CobotMain.cardHeight, 0);
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

    public void orderButton(CoffeeViewHolder holder) {
        if (CobotMain.currentOrderId == 0) {
            order(holder);
        } else {
            addToOrder(holder, CobotMain.currentOrderId);
        }
    }

    public SeekBar.OnSeekBarChangeListener seekBarUpdater(final CoffeeViewHolder holder) {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int p = limitProgress(i);
                p = p == 0 ? p : limitProgress(seekBar.getProgress()) / CobotMain.milkScale;
                String sp = String.valueOf(p);

                switch (seekBar.getId()) {
                    case R.id.seekBarStrength:
                        holder.strengthValue.setText(sp);
                    case R.id.seekBarMilk:
                        holder.milkValue.setText(sp);
                    case R.id.seekBarSugar:
                        holder.sugarValue.setText(sp);
                }

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int p = limitProgress(seekBar.getProgress());
                ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, "progress", p);
                animation.setDuration(200); // 0.5 second
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
                updateProduct(holder);
            }
        };
    }

    public int limitProgress(int p) {
        if(p < 50) {
            p = 0;
        } else if ( p > 50 && p < 150) {
            p = 100;
        } else if ( p > 150 && p < 250) {
            p = 200;
        } else if ( p > 250 && p < 350) {
            p = 300;
        } else if ( p > 350) {
            p = 400;
        }
        return p;
    }

    /**
     * Creates an order for a user
     * @param holder The CoffeeViewHolder which has to be ordered
     */
    public void order(final CoffeeViewHolder holder) {
        Call<Order> call = userClient.createOrder(CobotMain.id);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    System.out.println("Order add success");
                    holder.orderButton.setText("Add to order");
                    addToOrder(holder, response.body().getId());
                } else {
                    if(response.message().equalsIgnoreCase("FORBIDDEN")) {
                        addToOrder(holder, CobotMain.currentOrderId);
                    }
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());
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
            id = CobotMain.currentOrderId;
            if (id == 0) {
                Log.d("No valid order id:", String.valueOf(id));
            }
        }
        Call<Product> call = userClient.createOrderProduct(
                id,
                String.valueOf(holder.coffeeName.getText()),
                holder.coffeeType,
                holder.coffeeStrength.getProgress() == 0 ? holder.coffeeStrength.getProgress() : holder.coffeeStrength.getProgress() / CobotMain.milkScale,
                holder.coffeeMilk.getProgress() == 0 ? holder.coffeeMilk.getProgress() : holder.coffeeMilk.getProgress() / CobotMain.milkScale,
                holder.coffeeSugar.getProgress() == 0 ? holder.coffeeSugar.getProgress() : holder.coffeeSugar.getProgress() / CobotMain.milkScale,
                holder.coffeeMug.isChecked() ? 1 : 0
        );

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Response<Product> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    System.out.println("Product add success");
                    CobotMain.ordersFragment.setOrder();
                    //TODO: User notification
                } else {
                    if( response.message().equalsIgnoreCase("Not Found")) {
                        order(holder);
                    }
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());
                    //TODO: User notification
                    System.out.println("Failed");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", "wow" + t.getMessage());
            }
        });

    }

    /**
     * Updates the product's settings on the server
     * @param holder CoffeeViewHolder that has been updated
     */
    public void updateProduct(final CoffeeViewHolder holder) {
        Call<Product> call = userClient.updateProduct(
                CobotMain.id,
                holder.productId,
                String.valueOf(holder.coffeeName.getText()),
                holder.coffeeType,
                holder.coffeeStrength.getProgress() == 0 ? holder.coffeeStrength.getProgress() : holder.coffeeStrength.getProgress() / CobotMain.milkScale,
                holder.coffeeMilk.getProgress() == 0 ? holder.coffeeMilk.getProgress() : holder.coffeeMilk.getProgress() / CobotMain.milkScale,
                holder.coffeeSugar.getProgress() == 0 ? holder.coffeeSugar.getProgress() : holder.coffeeSugar.getProgress() / CobotMain.milkScale,
                holder.coffeeMug.isChecked() ? 1 : 0
        );

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Response<Product> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    System.out.println("Product update success");
                } else {
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());
                    //TODO: User notification
                    System.out.println("Product update failed");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", "wow" + t.getMessage());
            }
        });

    }

    public static class CoffeeViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView coffeeName;
        ImageView coffeePhoto;
        SeekBar coffeeStrength;
        TextView strengthValue;
        SeekBar coffeeMilk;
        TextView milkValue;
        SeekBar coffeeSugar;
        TextView sugarValue;
        CheckBox coffeeMug;
        TextView coffeeLocation;
        ImageButton coffeeArrow;
        RelativeLayout coffeeWrap;
        RelativeLayout coffeeHeader;
        RelativeLayout coffeeBody;
        Button orderButton;
        int coffeeType;
        int productId;
        Boolean collapsed = false;

        CoffeeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            coffeeName = (TextView)itemView.findViewById(R.id.coffee_name);
            coffeePhoto = (ImageView)itemView.findViewById(R.id.coffee_photo);
            coffeeStrength = (SeekBar)itemView.findViewById(R.id.seekBarStrength);
            strengthValue = (TextView)itemView.findViewById(R.id.strengthValue);
            coffeeMilk = (SeekBar)itemView.findViewById(R.id.seekBarMilk);
            milkValue = (TextView)itemView.findViewById(R.id.milkValue);
            coffeeSugar = (SeekBar)itemView.findViewById(R.id.seekBarSugar);
            sugarValue = (TextView)itemView.findViewById(R.id.sugarValue);
            coffeeMug = (CheckBox)itemView.findViewById(R.id.checkBoxMug);
            coffeeLocation = (TextView)itemView.findViewById(R.id.textViewLocation);
            coffeeArrow = (ImageButton)itemView.findViewById(R.id.coffee_arrow);
            coffeeWrap = (RelativeLayout)itemView.findViewById(R.id.coffee_wrap);
            coffeeHeader = (RelativeLayout)itemView.findViewById(R.id.header_wrap);
            coffeeBody = (RelativeLayout)itemView.findViewById(R.id.body_wrap);
            orderButton = (Button)itemView.findViewById(R.id.orderButton);
        }
    }
}



