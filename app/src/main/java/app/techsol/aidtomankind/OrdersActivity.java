package app.techsol.aidtomankind;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.techsol.aidtomankind.Models.MedicineModel;
import app.techsol.aidtomankind.Models.OrdersModel;
import app.techsol.aidtomankind.Models.UserModel;

public class OrdersActivity extends AppCompatActivity {

    DatabaseReference CustomerReference, OrdersRef;

    RecyclerView mCustomerRecycVw;
    FirebaseAuth auth;
    FrameLayout view;
    private String userid;
    private Dialog dialog;
    private Button btnCancel;
    private EditText storyET;
    Button btnPlaceOrder;
    private TextView MedInfoTV;
    DatabaseReference UserRef;
    private EditText quantityET;
    private DatabaseReference MedicineRef;
    private String userLat, userLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        auth = FirebaseAuth.getInstance();
        OrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        MedicineRef = FirebaseDatabase.getInstance().getReference().child("Medicines");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        view = findViewById(R.id.customer_content_main);
        mCustomerRecycVw = findViewById(R.id.main_recycler_vw);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mCustomerRecycVw.setLayoutManager(mLayoutManager);
        getUserLocation(auth.getCurrentUser().getUid());

        FirebaseRecyclerOptions<MedicineModel> options = new FirebaseRecyclerOptions.Builder<MedicineModel>()
                .setQuery(MedicineRef, MedicineModel.class)
                .build();

        final FirebaseRecyclerAdapter<MedicineModel, CustomersViewHolder> adapter = new FirebaseRecyclerAdapter<MedicineModel, CustomersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CustomersViewHolder holder, final int position, @NonNull final MedicineModel model) {


                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width


                holder.MedNameTV.setText(model.getName());
                holder.MedPriceTV.setText("Rs/- " + model.getPrice());
                holder.ForwardImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog(model.getId(), model.getName(), model.getPrice(), model.getQuantity(), model.getFormula());
                    }
                });
                holder.medInfoImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewInfoDialog(model.getInfo());
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

    private void openDialog(String id, String MedName, String price, String Quantity, String Formula) {
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
                Toast.makeText(OrdersActivity.this, Quantity, Toast.LENGTH_SHORT).show();
                int previousQnty = Integer.parseInt(Quantity);
                int demandedQnty = Integer.parseInt(quantityET.getText().toString());
                Toast.makeText(OrdersActivity.this, ""+quantityET, Toast.LENGTH_SHORT).show();
                if (previousQnty < demandedQnty) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent = new Intent(getBaseContext(), SugestedMedicineActivity.class);
                                    intent.putExtra("formula", Formula);
                                    startActivity(intent);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(OrdersActivity.this);
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                } else {
//                    String pushid = UserRef.push().getKey();
//
//                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//
//                    OrdersModel model = new OrdersModel(pushid, auth.getCurrentUser().getUid(), currentDate, MedName, quantityET.getText().toString(), price, userLat, userLong, "Placed");
//                    OrdersRef.child(pushid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull @NotNull Task<Void> task) {
//                            Toast.makeText(OrdersActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                    });
                }
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

    private void viewInfoDialog(String MedInfo) {
        dialog = new Dialog(OrdersActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setContentView(R.layout.item_viewmedinfo_dialog_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_AppCompat_DayNight_Dialog_Alert;
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.setCancelable(true);
        if (MedInfo == null) {
            MedInfo = "No Story Added Yet";
        }
        MedInfoTV = dialog.findViewById(R.id.medInfoTV);
        MedInfoTV.setText(MedInfo);
        dialog.show();
    }
    void getUserLocation(String userid) {
        UserRef.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    userLat = dataSnapshot.child("userlat").getValue().toString();
                userLong = dataSnapshot.child("userlong").getValue().toString();
//                Toast.makeText(MainActivity.this, UserType, Toast.LENGTH_SHORT).show();


                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}


