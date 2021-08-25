package app.techsol.aidtomankind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {

    private KProgressHUD kProgressHUD;
    Button mLoginBtn;
    TextView goToSignup;
    EditText phoneET, passwordET;
    FirebaseAuth auth;
    private DatabaseReference BookingRef;
    String UserType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        BookingRef = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        phoneET = findViewById(R.id.phoneET);
        passwordET = findViewById(R.id.passwordEt);
        goToSignup = findViewById(R.id.goToSignup);
        goToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), SignupActivity.class));
            }
        });
        mLoginBtn = findViewById(R.id.LoginBtn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (phoneET.getText().toString().isEmpty()) {
                    phoneET.setError("Please enter Phone No.");
                } else if (passwordET.getText().toString().isEmpty()) {
                    passwordET.setError("Please enter Password");
                } else {
                    auth.signInWithEmailAndPassword(phoneET.getText().toString(), passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                getUserType();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
//                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }
            }
        });
    }

    void getUserType() {
        BookingRef.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    UserType = dataSnapshot.child("usertype").getValue().toString();

                if (UserType.equals("User")) {
                    startActivity(new Intent(MainActivity.this, UserDashboardActivity.class));
                    finish();
                } else if (UserType.equals("pharmacist")) {
                    startActivity(new Intent(MainActivity.this, AdminDashboardActivity.class));
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}