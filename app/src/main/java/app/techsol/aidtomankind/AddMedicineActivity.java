package app.techsol.aidtomankind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import app.techsol.aidtomankind.Models.MedicineModel;

public class AddMedicineActivity extends AppCompatActivity {

    EditText MedNameET, MedQntyET, MedFormulaET, medInfoET, MedPriceET;
    String MednameStr, MedQntyStr, MedFormulaStr, MedInfoStr;
    Spinner MedTypeSpnr;
    Button AddMedBtn;
    TextView gotoLoginTV;
    FrameLayout view;
    ProgressBar mProgressBar;
    FirebaseAuth auth;
    DatabaseReference MedRef;
    private LocationManager locationManager;
    private String MedPriceStr;
    private StorageReference mProfilePicStorageReference;

    Button mSelectedImgBtn;
    ImageView profileImageView;
    String downloadUri;

    private static final int RC_PHOTO_PICKER = 1;
    private Uri selectedProfileImageUri;


    DatabaseReference databaseReference;
    StorageReference profilePicRef;
    private ImageView mProfilePic;
    private Button mSelectImgBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        getSupportActionBar().hide();
        MedRef = FirebaseDatabase.getInstance().getReference("Medicines");
        mProfilePicStorageReference= FirebaseStorage.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        view = findViewById(R.id.getView);
        mProfilePic = findViewById(R.id.selectedImg);
        mSelectImgBtn = findViewById(R.id.btn_selectimg);
        mSelectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfilePicture();
            }
        });


        MedPriceET = findViewById(R.id.MedPriceET);
        mProgressBar = findViewById(R.id.mProgressBar);
        MedNameET = findViewById(R.id.MedNameET);
        MedQntyET = findViewById(R.id.MedQntyET);
        MedFormulaET = findViewById(R.id.MedFormulaET);
        medInfoET = findViewById(R.id.medInfoET);
        MedTypeSpnr = findViewById(R.id.MedTypeSpnr);
        AddMedBtn = findViewById(R.id.AddMedBtn);

        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        AddMedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStrings();
                if (MednameStr.isEmpty()) {
                    MedNameET.setError("Enter Name");
                } else if (MedQntyStr.isEmpty()) {
                    MedQntyET.setError("Enter Email");
                } else if (MedFormulaStr.isEmpty()) {
                    MedFormulaET.setError("Enter Password");
                } else if (MedInfoStr.isEmpty()) {
                    medInfoET.setError("Enter Phone No");
                }  else {
                    profilePicRef = mProfilePicStorageReference.child(selectedProfileImageUri.getLastPathSegment());
                    profilePicRef.putFile(selectedProfileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUri = uri.toString();
                                    uploadUser(downloadUri);
                                }
                            });
                        }
                    });

                }
            }
        });

    }

    void getStrings() {
        MednameStr=MedNameET.getText().toString();
        MedQntyStr=MedQntyET.getText().toString();
        MedFormulaStr=MedFormulaET.getText().toString();
        MedInfoStr=medInfoET.getText().toString();
        MedPriceStr=MedPriceET.getText().toString();
    }

    void uploadUser(String ImageUrl){
        String id = MedRef.push().getKey();
        String MedTypeStr=MedTypeSpnr.getSelectedItem().toString();
        MedicineModel model = new MedicineModel(id, MednameStr, MedQntyStr,MedTypeStr, MedFormulaStr, MedInfoStr, MedPriceStr, ImageUrl);
        MedRef.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mProgressBar.setVisibility(View.GONE);
                    final Snackbar snackbar = Snackbar.make(view, "Medicine Added Successfully", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(AddMedicineActivity.this, ManageMedicineActivity.class));
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(AddMedicineActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getProfilePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            selectedProfileImageUri = selectedImageUri;
            mProfilePic.setImageURI(selectedImageUri);
            mProfilePic.setVisibility(View.VISIBLE);
        }

    }
}