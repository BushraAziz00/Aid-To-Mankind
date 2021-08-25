package app.techsol.aidtomankind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;

public class UserDashboardActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    LinearLayout FindMedLL, MyOrderLL, SpecialOffersLL, NearbyLL;
    int images[] = {R.drawable.slider_1,R.drawable.slider_2, R.drawable.slider_3, R.drawable.slider_4, R.drawable.slider_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        auth=FirebaseAuth.getInstance();
        FindMedLL=findViewById(R.id.FindMedLL);
        FindMedLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserDashboardActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getBaseContext(), PlaceOrderActivity.class));
            }
        });
        MyOrderLL=findViewById(R.id.MyOrderLL);
        MyOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MyOrdersActivity.class));
            }
        });
        SpecialOffersLL=findViewById(R.id.SpecialOffersLL);
        NearbyLL=findViewById(R.id.NearbyLL);

        ViewFlipper simpleViewFlipper = findViewById(R.id.simpleViewFlipper); // get the reference of ViewFlipper
        for (int i = 0; i < images.length; i++) {
            // create the object of ImageView
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(images[i]); // set image in ImageView
            simpleViewFlipper.addView(imageView); // add the created ImageView in ViewFlipper
        }
        // Declare in and out animations and load them using AnimationUtils class
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        // set the animation type's to ViewFlipper
        simpleViewFlipper.setInAnimation(in);
        simpleViewFlipper.setOutAnimation(out);
        // set interval time for flipping between views
        simpleViewFlipper.setFlipInterval(3000);
        // set auto start for flipping between views
        simpleViewFlipper.setAutoStart(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout_item) {
            auth.signOut();
            startActivity(new Intent(UserDashboardActivity.this, MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}