package app.techsol.aidtomankind;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.techsol.aidtomankind.databinding.ActivityNearbyShopsBinding;

public class NearbyShopsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityNearbyShopsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNearbyShopsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng readerCollege = new LatLng(32.07626987425718, 72.69022867722524);
        LatLng readerCollege1 = new LatLng(32.07683351405815, 72.68859789418114);
        LatLng clinix = new LatLng(32.08467811573734, 72.6888007367479);
        LatLng DHQPharmacy = new LatLng(32.08602344897322, 72.69041006212035);
        mMap.addMarker(new MarkerOptions().position(readerCollege).title("Madina Pharmacy"));
        mMap.addMarker(new MarkerOptions().position(readerCollege1).title("Bismillah Pharmacy"));
        mMap.addMarker(new MarkerOptions().position(clinix).title("Bismillah Pharmacy"));
        mMap.addMarker(new MarkerOptions().position(DHQPharmacy).title("DHQ Pharmacy"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.07626987425718, 72.69022867722524), 12.0f));

    }
}