package com.emilflach.cobot.ViewControllers;

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

import com.emilflach.cobot.CobotMain;
import com.emilflach.cobot.Models.ApiError;
import com.emilflach.cobot.Models.User;
import com.emilflach.cobot.R;
import com.emilflach.cobot.api.ErrorUtils;
import com.emilflach.cobot.api.ServiceGenerator;

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

    private EditText name;
    private EditText location;
    private EditText email;
    private EditText password;

    private EditText loginEmail;
    private EditText loginPassword;

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

        name = (EditText) v.findViewById(R.id.editTextName);
        location = (EditText) v.findViewById(R.id.editTextLocation);
        email = (EditText) v.findViewById(R.id.editTextEmail);
        password = (EditText) v.findViewById(R.id.editTextPassword);

        loginEmail = (EditText) v.findViewById(R.id.editTextLoginEmail);
        loginPassword = (EditText) v.findViewById(R.id.editTextLoginPassword);

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
                register(v);
                break;
            case R.id.login:
                login(v);
                break;
        }
    }

    /**
     * Sets the registration fields
     * @param name name field
     * @param location location field
     * @param email email field
     * @param password password field
     */
    public void setText(String name, String location, String email, String password) {
        this.name.setText(name);
        this.location.setText(location);
        this.email.setText(email);
        this.password.setText(password);
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
    public void register(View v) {

        final String name = this.name.getText().toString();
        final String location = this.location.getText().toString();
        final String email = this.email.getText().toString();
        final String password = this.password.getText().toString();

        setText("", "", "", "");

        ServiceGenerator.UserClient userClient = ServiceGenerator.createService(ServiceGenerator.UserClient.class);
        Call<User> call = userClient.createUser(name, email, password, location);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    setCredentials(email, password, response.body().getId());
                } else {
                    setText(name, location, email, password);
                    ApiError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("error message", error.message());

                    for (String message : error.validation_messages()) {
                        Log.d("validation message", message);
                    }
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
                    System.out.println("Failed");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });



    }

    public void setCredentials(String email, String password, int id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id", id);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
        CobotMain main = (CobotMain) getActivity();
        main.setAdapter();
    }

}
