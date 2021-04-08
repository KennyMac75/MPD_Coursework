/*Kenny MacInnes
    S1710196 */

package org.me.gcu.version2_coursework_kmacin204;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapDisplay extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap earthquakeMap;
    private ArrayList<PullParser> alist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_display);
        alist = (ArrayList<PullParser>) getIntent().getExtras().getSerializable("Items");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        earthquakeMap = googleMap;

        LatLng uk = new LatLng(55.86, -4.25);

        for (int i = 0; i < alist.size(); i++) {
            BitmapDescriptor bitmap = null;

            if (Float.parseFloat(alist.get(i).getMagnitude()) < 0.8) {
                bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            } else if (Float.parseFloat(alist.get(i).getMagnitude()) >= 0.8 && Float.parseFloat(alist.get(i).getMagnitude()) <= 1.8) {
                bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            } else if (Float.parseFloat(alist.get(i).getMagnitude()) > 1.8) {
                bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            }
            earthquakeMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(alist.get(i).getLatitude()), Double.parseDouble(alist.get(i).getLongitude()))).icon(bitmap).title(alist.get(i).getLocation()));
        }
        earthquakeMap.moveCamera(CameraUpdateFactory.newLatLng(uk));
    }


}