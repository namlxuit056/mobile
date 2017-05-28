package com.opensourceassignment.singkaraoke;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Created by namlxuit on 10/04/2017.
 */
public class RestoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
        final EditText email = (EditText)findViewById(R.id.email);
        EditText pass = (EditText)findViewById(R.id.password);
        Button button = (Button)findViewById(R.id.btn_restore);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().equals("") || email.getText().equals("")) {
                    Toast toast = new Toast(v.getContext());
                    toast.setText("Không bỏ trống email hoặc password");
                    toast.show();
                    return;
                }
                ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Đang phục hồi");
                progressDialog.show();
            }
        });
    }
}
