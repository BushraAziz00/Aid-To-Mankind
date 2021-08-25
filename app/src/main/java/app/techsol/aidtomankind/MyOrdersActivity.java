package app.techsol.aidtomankind;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.techsol.aidtomankind.Models.OrdersModel;

public class MyOrdersActivity extends AppCompatActivity {


    RecyclerView mCustomerRecycVw;
    FirebaseAuth auth;
    FrameLayout view;
    TextView OrderQnty;
    private String userid;
    private Dialog dialog;
    private Button btnCancel;
    private EditText storyET;
    Button btnPlaceOrder;
    private TextView MedInfoTV, seekerStoryTV;
    DatabaseReference OrderRef;
    private EditText quantityET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        View rootview = findViewById(android.R.id.content);

        auth = FirebaseAuth.getInstance();
        OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        view = findViewById(R.id.customer_content_main);
        mCustomerRecycVw = findViewById(R.id.main_recycler_vw);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mCustomerRecycVw.setLayoutManager(mLayoutManager);
        userid = auth.getCurrentUser().getUid();

        FirebaseRecyclerOptions<OrdersModel> options = new FirebaseRecyclerOptions.Builder<OrdersModel>()
                .setQuery(OrderRef.orderByChild("userid").equalTo(userid), OrdersModel.class)
                .build();

        final FirebaseRecyclerAdapter<OrdersModel, CustomersViewHolder> adapter = new FirebaseRecyclerAdapter<OrdersModel, CustomersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CustomersViewHolder holder, final int position, @NonNull final OrdersModel model) {


                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width
                holder.OrderStatusTV.setText(model.getQuantity());
                holder.OrderNameTV.setText(model.getMedname());
                holder.OrderPriceTV.setText(model.getPrice());
                holder.OrderQntyTV.setText(model.getQuantity());
                holder.UpdateStatusIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                        intent.putExtra("order_id", model.getId());
                        startActivity(intent);
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

        mCustomerRecycVw.setAdapter(adapter);
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
}

