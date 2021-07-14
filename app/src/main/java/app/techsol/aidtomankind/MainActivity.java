package app.techsol.aidtomankind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

public class MainActivity extends AppCompatActivity {

    private KProgressHUD kProgressHUD;
    Button mLoginBtn;
    TextView goToSignup;
    EditText phoneET, passwordET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

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
                    kProgressHUD = KProgressHUD.create(MainActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setAnimationSpeed(2)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                            .setLabel("Authenticating")
                            .setDetailsLabel("Please Wait...")
                            .setDimAmount(0.3f)
                            .show();

//                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }
            }
        });
    }
}