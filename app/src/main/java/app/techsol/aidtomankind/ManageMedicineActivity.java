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

import app.techsol.aidtomankind.Models.MedicineModel;

public class ManageMedicineActivity extends AppCompatActivity {

    DatabaseReference MedRef;
    RecyclerView mCustomerRecycVw;
    FirebaseAuth auth;
    FrameLayout view;
    private String userid;
    private Dialog dialog;
    private Button btnCancel;
    private EditText storyET;
    Button btnAddStory;
    private TextView donorStoryTV, seekerStoryTV;
    private Button btnAddQnty;
    EditText quantityET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicine);
        View rootview = findViewById(android.R.id.content);

        auth = FirebaseAuth.getInstance();
        userid = auth.getCurrentUser().getUid();
        MedRef = FirebaseDatabase.getInstance().getReference().child("Medicines");
        view = findViewById(R.id.customer_content_main);
        mCustomerRecycVw = findViewById(R.id.main_recycler_vw);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mCustomerRecycVw.setLayoutManager(mLayoutManager);
        FirebaseRecyclerOptions<MedicineModel> options = new FirebaseRecyclerOptions.Builder<MedicineModel>()
                .setQuery(MedRef, MedicineModel.class)
                .build();

        final FirebaseRecyclerAdapter<MedicineModel, CustomersViewHolder> adapter = new FirebaseRecyclerAdapter<MedicineModel, CustomersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CustomersViewHolder holder, final int position, @NonNull final MedicineModel model) {


                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width

                holder.MedQntyTV.setText(model.getQuantity());
                holder.MedNameTV.setText(model.getName());
                int quantitiy = Integer.parseInt(model.getQuantity());
                if (quantitiy < 10) {
                    holder.MedStatusTV.setText("Critically Low");
                } else if (quantitiy < 100) {
                    holder.MedStatusTV.setText("Low");
                } else {
                    holder.MedStatusTV.setText("Enough");
                }
                holder.MedStatusTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewStoryDialog(model.getId(), quantitiy);
                    }
                });
            }

            @NonNull
            @Override
            public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_manage_medicine_layout, viewGroup, false);
                CustomersViewHolder customersViewHolder = new CustomersViewHolder(view);
                return customersViewHolder;
            }
        };
        mCustomerRecycVw.setAdapter(adapter);
        adapter.startListening();
    }


    public static class CustomersViewHolder extends RecyclerView.ViewHolder {


        TextView MedQntyTV, MedNameTV, MedStatusTV;

        public CustomersViewHolder(@NonNull View itemView) {
            super(itemView);
            MedQntyTV = itemView.findViewById(R.id.MedQntyTV);
            MedNameTV = itemView.findViewById(R.id.MedNameTV);
            MedStatusTV = itemView.findViewById(R.id.MedStatusTV);

        }
    }

    private void viewStoryDialog(String MedId, int previousQnty) {
        dialog = new Dialog(ManageMedicineActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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
                MedRef.child(MedId).setValue((previousQnty + newQnty) + "").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ManageMedicineActivity.this, "New Medicine Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        dialog.show();
    }

}