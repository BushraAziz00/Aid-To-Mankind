package app.techsol.aidtomankind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import app.techsol.aidtomankind.Models.UserModel;

public class ManageUsersActivity extends AppCompatActivity {

    DatabaseReference CustomerReference;
    RecyclerView mCustomerRecycVw;
    FirebaseAuth auth;
    FrameLayout view;
    private String userid;
    private Dialog dialog;
    private Button btnCancel;
    private EditText storyET;
    Button btnAddStory;
    private TextView donorStoryTV, seekerStoryTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        View rootview = findViewById(android.R.id.content);

        auth = FirebaseAuth.getInstance();
        userid=auth.getCurrentUser().getUid();
        CustomerReference = FirebaseDatabase.getInstance().getReference().child("Users");
        view = findViewById(R.id.customer_content_main);
        mCustomerRecycVw = findViewById(R.id.main_recycler_vw);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mCustomerRecycVw.setLayoutManager(mLayoutManager);
        FirebaseRecyclerOptions<UserModel> options = new FirebaseRecyclerOptions.Builder<UserModel>()
                .setQuery(CustomerReference, UserModel.class)
                .build();

        final FirebaseRecyclerAdapter<UserModel, CustomersViewHolder> adapter = new FirebaseRecyclerAdapter<UserModel, CustomersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CustomersViewHolder holder, final int position, @NonNull final UserModel model) {


                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width

                holder.userName.setText(model.getName());
                holder.userPhone.setText(model.getPhoneno());
            }

            @NonNull
            @Override
            public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_item_layout, viewGroup, false);
                CustomersViewHolder customersViewHolder = new CustomersViewHolder(view);
                return customersViewHolder;
            }
        };
        mCustomerRecycVw.setAdapter(adapter);
        adapter.startListening();
    }


    public static class CustomersViewHolder extends RecyclerView.ViewHolder {


        TextView userName, userPhone;
        TextView DateTV;
        TextView donorPhone, blodTypeTV, donationReqTV, AddStoryTV;


        ImageView viewStoryIV;

        public CustomersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName =  itemView.findViewById(R.id.userName);
            userPhone =  itemView.findViewById(R.id.userPhone);

        }

    }
}