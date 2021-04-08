/*Kenny MacInnes
    S1710196 */

package org.me.gcu.version2_coursework_kmacin204;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MoreInfo extends AppCompatActivity {

    ListView DetailView;
    ArrayList<String> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info);
        DetailView = findViewById(R.id.DetailView);
        Bundle bundle = getIntent().getExtras();
        info = new ArrayList<String>();

        //uses the bundle made in the mainactivity to get data which is entered into a string and added to the info array list

        String temp_location = "Earthquake Location: " + bundle.getString("location");
        String temp_date = "Date of Earthquake: " + bundle.getString("date");
        String temp_category = "Category " + bundle.getString("category");
        String temp_depth = "Depth of Earthquake: " + bundle.getString("depth") + "KM";
        String temp_latitude = "Latitude: " + bundle.getString("latitude");
        String temp_longitude = "Longitude: " + bundle.getString("longitude");
        String matric_num = "Matriculation Number = S1710196";
        info.add(temp_location);
        info.add(temp_date);
        info.add(temp_category);
        info.add(temp_depth);
        info.add(temp_latitude);
        info.add(temp_longitude);
        info.add(matric_num);

        //the info is then added to the array adapter which is then set to the list view DetailView

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MoreInfo.this, android.R.layout.simple_list_item_1, info);

        DetailView.setAdapter(adapter);

    }


}