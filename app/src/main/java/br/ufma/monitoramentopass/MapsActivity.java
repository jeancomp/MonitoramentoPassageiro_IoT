package br.ufma.monitoramentopass;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    // Para debug
    public static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    Marker marcador;
    LatLng latLng;
    LatLng lat_long_Final;
    Geocoder geocoder;

    Button button1;
    Button telaPrincipal;
    EditText searchview1;
    TextView latlongLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        geocoder = new Geocoder(this, Locale.getDefault());

        lat_long_Final = null;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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

        Toast.makeText(MapsActivity.this, "Selecione o destino", Toast.LENGTH_LONG).show();

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-2.5520319,-44.2537906);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("DestinoPassageiro"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Método responsável por selecionar o destino através clique no MAPA
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                //save current location
                latLng = point;

                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                android.location.Address address = addresses.get(0);

                if (address != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
                        sb.append(address.getAddressLine(i) + "\n");
                    }
                    Log.i(TAG,"##### Local selecionado: " + point.toString());
                    //Toast.makeText(MapsActivity.this, sb.toString(), Toast.LENGTH_LONG).show();

                }

                //remove previously placed Marker
                if (marcador != null) {
                    marcador.remove();
                }

                //place marker where user just clicked
                marcador = mMap.addMarker(new MarkerOptions().position(point).title("DestinoPassageiro")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                latlongLocation.setText("Latitude:" + latLng.latitude + ", Longitude:"
                        + latLng.longitude);
                lat_long_Final = latLng;
            }
        });

        latlongLocation = findViewById(R.id.latlongLocation);
        searchview1 = findViewById(R.id.searchView1);
        telaPrincipal = findViewById(R.id.telaPrincipal);
        button1 = findViewById(R.id.button1);


        // Método responsável por pesquisar o Destino através do endereço digitado na tela
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String g = searchview1.getText().toString();

                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;

                try {
                    // Getting a maximum of 3 Address that matches the input
                    // text
                    addresses = geocoder.getFromLocationName(g, 3);
                    if (addresses != null && !addresses.equals(""))
                        search(addresses);

                } catch (Exception e) {

                }

            }
        });

        telaPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lat_long_Final != null){
                    Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                    intent.putExtra("lat_lon", lat_long_Final.toString());
                    startActivity(intent);
                }
            }
        });
    }

    protected void search(List<Address> addresses) {

        Address address = (Address) addresses.get(0);
        double home_long = address.getLongitude();
        double home_lat = address.getLatitude();
        latLng = new LatLng(address.getLatitude(), address.getLongitude());

        String addressText = String.format(
                "%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address
                        .getAddressLine(0) : "", address.getCountryName());

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title(addressText);

        mMap.clear();
        marcador = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        latlongLocation.setText("Latitude:" + address.getLatitude() + ", Longitude:"
                + address.getLongitude());

        lat_long_Final = latLng;
    }
}