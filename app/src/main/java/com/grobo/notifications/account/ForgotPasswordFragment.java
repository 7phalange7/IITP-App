package com.grobo.notifications.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.grobo.notifications.R;
import com.grobo.notifications.network.RetrofitClientInstance;
import com.grobo.notifications.network.UserRoutes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPasswordFragment extends Fragment {

    private OnForgotPasswordInteractionListener mListener;
    private ProgressDialog progressDialog;
    private Context context;

    public ForgotPasswordFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContext() != null)
            this.context = getContext();

        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView login = view.findViewById(R.id.login);
        login.setOnClickListener(v -> mListener.onBackToLoginClicked());

        Button sendHelpButton = view.findViewById(R.id.button_send_help);
        sendHelpButton.setOnClickListener(v -> {
            EditText webmailInput = view.findViewById(R.id.fp_input_webmail);

            if (webmailInput.getText().toString().isEmpty()) {
                webmailInput.setError("Enter a valid webmail");
            } else {
                sendHelp(webmailInput.getText().toString());
            }
        });
    }

    private void sendHelp(String webmail) {

        progressDialog.setMessage("Processing...");
        progressDialog.show();

        Map<String, Object> data = new HashMap<>();
        data.put("email", webmail);

        RequestBody body = RequestBody.create((new JSONObject(data)).toString(), okhttp3.MediaType.parse("application/json; charset=utf-8"));

        UserRoutes service = RetrofitClientInstance.getRetrofitInstance().create(UserRoutes.class);
        Call<ResponseBody> call = service.forgotPassword(body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (progressDialog.isShowing()) progressDialog.dismiss();

                if (response.body() != null) {
                    try {
                        JSONObject o = new JSONObject(response.body().string());
                        String message = o.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Unhandled Error", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Unknown Error, please try again !!!", Toast.LENGTH_LONG).show();
                }

                if (response.code() == 200) {
                    mListener.onCodeSent(webmail);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (t.getMessage() != null) Log.e("failure", t.getMessage());
                if (progressDialog.isShowing()) progressDialog.dismiss();

                Toast.makeText(context, "Failed, please try again !!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnForgotPasswordInteractionListener) {
            mListener = (OnForgotPasswordInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnForgotPasswordInteractionListener {
        void onBackToLoginClicked();
        void onCodeSent(String email);
    }
}
