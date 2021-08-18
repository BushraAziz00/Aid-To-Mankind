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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.techsol.aidtomankind.Models.MedicineModel;
import app.techsol.aidtomankind.Models.OrdersModel;

public class OrdersActivity extends AppCompatActivity {

    DatabaseReference CustomerReference;
    RecyclerView mCustomerRecycVw;
    FirebaseAuth auth;
    FrameLayout view;
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
        setContentView(R.layout.activity_orders);
        View rootview = findViewById(android.R.id.content);

        auth = FirebaseAuth.getInstance();
        CustomerReference = FirebaseDatabase.getInstance().getReference().child("Medicines");
        OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        view = findViewById(R.id.customer_content_main);
        mCustomerRecycVw = findViewById(R.id.main_recycler_vw);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mCustomerRecycVw.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions<MedicineModel> options = new FirebaseRecyclerOptions.Builder<MedicineModel>()
                .setQuery(CustomerReference, MedicineModel.class)
                .build();

        final FirebaseRecyclerAdapter<MedicineModel, CustomersViewHolder> adapter = new FirebaseRecyclerAdapter<MedicineModel, CustomersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CustomersViewHolder holder, final int position, @NonNull final MedicineModel model) {


                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width


                holder.MedNameTV.setText(model.getName());
                holder.MedPriceTV.setText("Rs/- "+model.getPrice());
                holder.ForwardImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog(model.getId(), model.getName(), model.getPrice());
                    }
                });
                holder.medInfoImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewStoryDialog("model.getInfo()");
                    }
                });





            }

            @NonNull
            @Override
            public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_medince_layout, viewGroup, false);
                CustomersViewHolder customersViewHolder = new CustomersViewHolder(view);
                return customersViewHolder;
            }
        };

        mCustomerRecycVw.setAdapter(adapter);
        adapter.startListening();

    }


    public static class CustomersViewHolder extends RecyclerView.ViewHolder {


        TextView userName;
        TextView DateTV;
        TextView MedNameTV, MedPriceTV, donationReqTV, AddStoryTV;

        ImageView ForwardImgBtn;
        ImageView imgMedItem, medInfoImgBtn;

        public CustomersViewHolder(@NonNull View itemView) {
            super(itemView);
            ForwardImgBtn = itemView.findViewById(R.id.ForwardImgBtn);
            MedNameTV = itemView.findViewById(R.id.MedNameTV);
            MedPriceTV = itemView.findViewById(R.id.MedPriceTV);

            medInfoImgBtn = itemView.findViewById(R.id.medInfoImgBtn);
            imgMedItem = itemView.findViewById(R.id.imgMedItem);
        }

    }

    private void openDialog(String id, String MedName, String price) {
        dialog = new Dialog(OrdersActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setContentView(R.layout.item_manageorder_dialog_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_AppCompat_DayNight_Dialog_Alert;
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.setCancelable(true);

        storyET = dialog.findViewById(R.id.storyET);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        quantityET = dialog.findViewById(R.id.quantityET);
        btnPlaceOrder = dialog.findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pushid=OrderRef.push().getKey();
//                String userId=auth.getCurrentUser().getUid();

                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                OrdersModel model=new OrdersModel(pushid, "UserId", currentDate, MedName, quantityET.getText().toString(), price);
                OrderRef.child(pushid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(OrdersActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void viewStoryDialog(String MedInfo) {
        dialog = new Dialog(OrdersActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setContentView(R.layout.item_viewmedinfo_dialog_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_AppCompat_DayNight_Dialog_Alert;
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.setCancelable(true);
        if (MedInfo==null){
            MedInfo="No Story Added Yet";
        }
        MedInfoTV = dialog.findViewById(R.id.medInfoTV);
        MedInfoTV.setText(MedInfo);
        dialog.show();
    }


}
