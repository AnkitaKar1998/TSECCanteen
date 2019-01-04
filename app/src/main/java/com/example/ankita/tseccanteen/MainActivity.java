package com.example.ankita.tseccanteen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ankita.tseccanteen.Orders.OrdersActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText etLogin, etPassword;
    Button btnLogin;
    FirebaseAuth firebaseAuth;

    String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLogin = findViewById(R.id.et_login);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        firebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputLoginId = etLogin.getText().toString();
                String inputPassword = etPassword.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(inputLoginId, inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            currentUserId = firebaseAuth.getCurrentUser().getUid();
                            Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                            intent.putExtra("currentUser", currentUserId);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Not Logged in", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }


}
