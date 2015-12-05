package com.emilflach.cobot.api;


import android.util.Base64;

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.ApiMessage;
import com.emilflach.cobot.Models.Order;
import com.emilflach.cobot.Models.Product;
import com.emilflach.cobot.Models.User;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.*;

public class ServiceGenerator {

    public static final String API_BASE_URL = "http://cobot.emilflach.com";

    private static OkHttpClient httpClient = new OkHttpClient();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        String username = CobotMain.email;
        String password = CobotMain.password;
        if (username != null && password != null) {
            String credentials = username + ":" + password;
            final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            httpClient.interceptors().clear();
            httpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);

                }
            });
        }
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

    public interface UserClient {

        @GET("/users/authenticate")
        Call<User> authenticate();

        @GET("/users/{id}")
        Call<User> user(
                @Path("id") int id
        );

        @GET("/users/{id}/products")
        Call<List<Product>> products(
                @Path("id") int id
        );

        @GET("/users/{id}/orders")
        Call<List<Order>> orders(
                @Path("id") int id
        );

        @GET("/orders/{id}/products")
        Call<List<Product>> orderProducts(
                @Path("id") int id
        );

        @FormUrlEncoded
        @POST("/users/")
        Call<User> createUser(
                @Field("name") String name,
                @Field("email") String email,
                @Field("password") String password,
                @Field("location") int location
        );

        @POST("/users/{id}/orders")
        Call<Order> createOrder(
                @Path("id") int id
        );

        @FormUrlEncoded
        @POST("/orders/{id}/products")
        Call<Product> createOrderProduct(
                @Path("id") int id,
                @Field("name") String name,
                @Field("type") int type,
                @Field("option_strength") int strength,
                @Field("option_milk") int milk,
                @Field("option_sugar") int sugar,
                @Field("option_mug") int mug
        );

        @DELETE("orders/{orderid}/products/{productid}")
        Call<ApiMessage> deleteOrderProduct(
                @Path("orderid") int orderid,
                @Path("productid") int productid
        );

        @FormUrlEncoded
        @PUT("/users/{userid}/products/{productid}")
        Call<Product> updateProduct(
                @Path("userid") int userid,
                @Path("productid") int productid,
                @Field("name") String name,
                @Field("type") int type,
                @Field("option_strength") int strength,
                @Field("option_milk") int milk,
                @Field("option_sugar") int sugar,
                @Field("option_mug") int mug
        );

    }
}