package com.emilflach.cobot.ViewControllers;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.ApiError;
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

/**
 * ${NAME}Created by Emil on 2015-11-06.
 */
public class OrdersFragment extends Fragment {
    private RecyclerView rv;
    private static final String ARG_SECTION_NUMBER = "section_number";
    ServiceGenerator.UserClient userClient;

    private TextView location;
    private TextView time;
    private TextView status;

    public OrdersFragment() {
    }

    public static OrdersFragment newInstance(int sectionNumber) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        return inflater.inflate(R.layout.orders_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Set the adapter with retrieved information
        View view = getView();
        if (view != null) {
        rv = (RecyclerView) view.findViewById(R.id.rv);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);
            rv.setHasFixedSize(true);

            List<Product> products = new ArrayList<>();
            OrderRVAdapter adapter = new OrderRVAdapter(products);
            rv.setAdapter(adapter);

            setOrders(view);
        }
    }


    /**
     * Loads the order related to a user and displays it
     * @param view the view to which the order should be set
     */
    private void setOrders(View view) {
        location = (TextView) view.findViewById(R.id.textViewLocation);
        time = (TextView) view.findViewById(R.id.textViewTime);
        status = (TextView) view.findViewById(R.id.textViewStatus);

        Call<List<Order>> call = userClient.orders(CobotMain.id);
        call.enqueue(new Callback<List<Order>>() {

            @Override
            public void onResponse(Response<List<Order>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    for (Order order : response.body()) {
                        location.setText(String.valueOf(order.getId()));
                        time.setText(String.valueOf(order.getUser()));
                        status.setText(String.valueOf(order.getDelivery_status()));
                        CobotMain.currentOrderId = order.getId();
                        setOrderProducts(order.getId());
                    }
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

    /**
     * Loads the products related to an order and displays it
     * @param order_id the order for which products should be returned
     */
    private void setOrderProducts(int order_id) {
        Call<List<Product>> call = userClient.orderProducts(order_id);
        call.enqueue(new Callback<List<Product>>() {

            private List<Product> products = new ArrayList<>();

            @Override
            public void onResponse(Response<List<Product>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    for (Product product : response.body()) {
                        this.products.add(product);
                    }
                    OrderRVAdapter adapter = new OrderRVAdapter(products);
                    rv.setAdapter(adapter);
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

}
