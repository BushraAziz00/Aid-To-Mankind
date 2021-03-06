package app.techsol.aidtomankind;

import android.app.Dialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import app.techsol.aidtomankind.Models.OrdersModel;

public class ManageOrdersActivity extends AppCompatActivity {
    DatabaseReference OrdersRef;
    RecyclerView MedRecycView;
    FirebaseAuth auth;
    FrameLayout view;
    private String userid;
    private Dialog dialog;
    private Button btnCancel;
    private EditText storyET;
    Button btnAddStory;
    private TextView donorStoryTV, seekerStoryTV;
    private Button btnAddQnty, quantityET;
    TextView OrderStatusTV, OrderNameTV, OrderPriceTV;
    ImageView UpdateStatusIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);
        View rootview = findViewById(android.R.id.content);

        auth = FirebaseAuth.getInstance();
        userid = auth.getCurrentUser().getUid();
        OrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        view = findViewById(R.id.customer_content_main);
        MedRecycView = findViewById(R.id.main_recycler_vw);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        MedRecycView.setLayoutManager(mLayoutManager);
        FirebaseRecyclerOptions<OrdersModel> options = new FirebaseRecyclerOptions.Builder<OrdersModel>()
                .setQuery(OrdersRef, OrdersModel.class)
                .build();

        final FirebaseRecyclerAdapter<OrdersModel, CustomersViewHolder> adapter = new FirebaseRecyclerAdapter<OrdersModel, CustomersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CustomersViewHolder holder, final int position, @NonNull final OrdersModel model) {


                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width

                holder.OrderStatusTV.setText(model.getOrderstatus());
                holder.OrderNameTV.setText(model.getMedname());
                holder.OrderPriceTV.setText("Rs/-"+model.getPrice());
                holder.OrderQntyTV.setText("Quantity"+model.getQuantity());
                holder.UpdateStatusIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrdersRef.child(model.getId()).child("orderstatus").setValue("Accepted").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ManageOrdersActivity.this, "Order Status Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.iitem_myorders_layout, viewGroup, false);
                CustomersViewHolder customersViewHolder = new CustomersViewHolder(view);
                return customersViewHolder;
            }
        };
        MedRecycView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class CustomersViewHolder extends RecyclerView.ViewHolder {


        TextView OrderStatusTV, OrderNameTV, OrderPriceTV, OrderQntyTV;

        ImageView UpdateStatusIV;

        public CustomersViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderStatusTV = itemView.findViewById(R.id.OrderStatusTV);
            OrderNameTV = itemView.findViewById(R.id.OrderNameTV);
            OrderPriceTV = itemView.findViewById(R.id.OrderPriceTV);
            OrderQntyTV = itemView.findViewById(R.id.OrderQntyTV);
            UpdateStatusIV = itemView.findViewById(R.id.UpdateStatusIV);


        }
    }

    private void viewStoryDialog(String MedId, int previousQnty) {
        dialog = new Dialog(ManageOrdersActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setContentView(R.layout.add_med_dialog_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_AppCompat_DayNight_Dialog_Alert;
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.setCancelable(true);
        quantityET = dialog.findViewById(R.id.quantityET);
        btnAddQnty = dialog.findViewById(R.id.btnAddQnty);
        btnAddQnty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQnty = Integer.parseInt(quantityET.getText().toString());
                OrdersRef.child(MedId).setValue((previousQnty + newQnty) + "").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ManageOrdersActivity.this, "New Medicine Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        dialog.show();
    }

}