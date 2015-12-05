package com.emilflach.cobot.ViewControllers.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.ApiError;
import com.emilflach.cobot.Models.User;
import com.emilflach.cobot.R;
import com.emilflach.cobot.api.ErrorUtils;
import com.emilflach.cobot.api.ServiceGenerator;

import java.util.Random;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * cobot
 * by Emil on 2015-11-07.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_SECTION_NUMBER = "section_number";

    private EditText loginEmail;
    private EditText loginPassword;
    private Toast toast;

    public LoginFragment() {
    }

    public static LoginFragment newInstance(int sectionNumber) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.login_fragment, container, false);

        //Buttons and their click listeners
        Button r = (Button) v.findViewById(R.id.register);
        Button l = (Button) v.findViewById(R.id.login);

        r.setOnClickListener(this);
        l.setOnClickListener(this);

        loginEmail = (EditText) v.findViewById(R.id.editTextLoginEmail);
        loginPassword = (EditText) v.findViewById(R.id.editTextLoginPassword);

        toast = Toast.makeText(getActivity(), "Notification", Toast.LENGTH_SHORT);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                register(v, 0);
                break;
            case R.id.login:
                login(v);
                break;
        }
    }


    /**
     * Function that gets called after login button press
     * @param v view to get login fields from
     */
    public void login(View v) {
        String email = this.loginEmail.getText().toString();
        String password = this.loginPassword.getText().toString();
        authenticate(email, password);
    }


    /**
     * Registers the user after button press, after successful registration authenticate() is called
     * @param v
     */
    public void register(final View v, int tries) {
        final int currentTries = tries + 1;

        Random random = new Random();
        final String name = "Bound to phone";
        final int location = 1;
        final String email = generateString(random, "abcdefghijklmnopqrstxyz", 30) + "@anonymous.user";
        final String password = generateString(random, "abdcdefghijklmnopqrstxyz", 30);

        ServiceGenerator.UserClient userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        Call<User> call = userClient.createUser(name, email, password, location);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    setCredentials(email, password, response.body().getId());
                } else {
                    if (currentTries < 2) {
                        register(v, currentTries);
                    }
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());
                    toast.setText(error.message());
                    toast.show();

                    for (String message : error.validation_messages()) {
                        Log.d("validation message", message);
                    }
                    System.out.println("Failed");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", t.getMessage());
                toast.setText("Something went wrong");
                toast.show();
            }
        });

    }


    /**
     * Authenticates a user by email and password,
     * basically returns user ID and stores it along with the email and password
     * @param email email to authenticate with
     * @param password password to authenticate with
     */
    public void authenticate(final String email, final String password) {
        CobotMain.email = email;
        CobotMain.password = password;

        ServiceGenerator.UserClient userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        Call<User> call = userClient.authenticate();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    setCredentials(email, password, response.body().getId());
                    System.out.println("Success");
                } else {
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());
                    toast.setText(error.message());
                    toast.show();
                    System.out.println("Failed");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", t.getMessage());
                toast.setText("Something went wrong");
                toast.show();
            }
        });



    }

    public void setCredentials(String email, String password, int id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id", id);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
        CobotMain main = (CobotMain) getActivity();
        main.setAdapter(1);
    }

    public static String generateString(Random rng, String characters, int length)
    {
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

}
