package com.emilflach.cobot.ViewControllers.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.ApiError;
import com.emilflach.cobot.Models.ApiMessage;
import com.emilflach.cobot.Models.Order;
import com.emilflach.cobot.Models.OrderCount;
import com.emilflach.cobot.Models.Product;
import com.emilflach.cobot.R;
import com.emilflach.cobot.ViewControllers.Adapters.OrderRVAdapter;
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
public class OrdersFragment extends Fragment implements View.OnClickListener {
    private RecyclerView rv;
    private static final String ARG_SECTION_NUMBER = "section_number";
    ServiceGenerator.UserClient userClient;
    private SwipeRefreshLayout swipeContainer;
    private OrdersFragment ordersFragment = this;
    private Toast toast;
    private Button clearButton;


    public OrdersFragment() {
    }

    public static OrdersFragment newInstance(int sectionNumber) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        CobotMain.ordersFragment = fragment;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.orders_fragment, container, false);
        userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setOrder();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorPrimary);
        clearButton = (Button) v.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(this);

        toast = Toast.makeText(getActivity(), "Notification", Toast.LENGTH_SHORT);

        return v;
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
            rv.setItemViewCacheSize(20);

            setEmptyAdapter();
            setOrder();
        }
    }

    public void setEmptyAdapter() {
        List<Product> products = new ArrayList<>();
        Order order = new Order();
        CobotMain cobotMain = (CobotMain) getActivity();
        OrderRVAdapter adapter = new OrderRVAdapter(products, order, cobotMain, ordersFragment, true);
        rv.setAdapter(adapter);
    }


    /**
     * Loads the order related to a user and displays it
     */
    public void setOrder() {
        swipeContainer.setRefreshing(true);
        Call<List<Order>> call = userClient.orders(CobotMain.id);
        call.enqueue(new Callback<List<Order>>() {

            @Override
            public void onResponse(Response<List<Order>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    for (Order order : response.body()) {
                        CobotMain.currentOrderId = order.getId();
                        setOrderProducts(order.getId(), order);
                    }
                } else {
                    CobotMain.currentOrderId = 0;
                    setEmptyAdapter();
                    swipeContainer.setRefreshing(false);
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", t.getMessage());
                swipeContainer.setRefreshing(false);
            }
        });
    }

    /**
     * Loads the products related to an order and displays it
     * @param order_id the order for which products should be returned
     */
    private void setOrderProducts(int order_id, final Order order) {
        getOrderCount(order_id);
        Call<List<Product>> call = userClient.orderProducts(order_id);
        call.enqueue(new Callback<List<Product>>() {
            private List<Product> products = new ArrayList<>();
            @Override
            public void onResponse(Response<List<Product>> response, Retrofit retrofit) {

                if (response.isSuccess()) {
                    for (Product product : response.body()) {
                        this.products.add(product);
                    }
                    CobotMain cobotMain = (CobotMain) getActivity();
                    OrderRVAdapter adapter = new OrderRVAdapter(products, order, cobotMain, ordersFragment, false);
                    rv.setAdapter(adapter);
                    swipeContainer.setRefreshing(false);

                } else {
                    swipeContainer.setRefreshing(false);
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", t.getMessage());
                swipeContainer.setRefreshing(false);
            }
        });

    }

    /**
     * Loads the order related to a user and displays it
     */
    public void getOrderCount(int order_id) {
        System.out.print("Order count called");
        Call<OrderCount> call = userClient.orderCount(order_id);
        call.enqueue(new Callback<OrderCount>() {

            @Override
            public void onResponse(Response<OrderCount> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    CobotMain.orderCount = response.body().getOrderCount();
                    Log.d("Order count", String.valueOf(response.body().getOrderCount()));
                } else {
                    CobotMain.orderCount = 0;
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error order count", error.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", t.getMessage());
                swipeContainer.setRefreshing(false);
            }
        });
    }

    public void deleteOrder() {
        Call<ApiMessage> call = userClient.deleteOrder(CobotMain.id, CobotMain.currentOrderId);
        call.enqueue(new Callback<ApiMessage>() {
            @Override
            public void onResponse(Response<ApiMessage> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    toast.setText("Order removed");
                    toast.show();
                    Log.d("Message", response.body().message());
                    ordersFragment.setOrder();
                } else {
                    ordersFragment.setOrder();
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());
                    toast.setText(error.message());
                    toast.show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", t.getMessage());
                toast.setText("Something went wrong!");
                toast.show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearButton:
                deleteOrder();
                break;
        }
    }
}
