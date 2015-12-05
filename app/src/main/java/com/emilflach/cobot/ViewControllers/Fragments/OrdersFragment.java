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

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.ApiError;
import com.emilflach.cobot.Models.Order;
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
public class OrdersFragment extends Fragment {
    private RecyclerView rv;
    private static final String ARG_SECTION_NUMBER = "section_number";
    ServiceGenerator.UserClient userClient;
    private SwipeRefreshLayout swipeContainer;
    private OrdersFragment ordersFragment = this;


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

}
