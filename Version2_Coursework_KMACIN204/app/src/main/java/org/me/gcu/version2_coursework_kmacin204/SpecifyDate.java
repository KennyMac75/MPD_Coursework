/*Kenny MacInnes
    S1710196 */

package org.me.gcu.version2_coursework_kmacin204;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SpecifyDate extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<PullParser> alist;
    private TextView startDate;
    private TextView endDate;
    private Button submitButton;
    private ListView output;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specify_date);
        alist = (ArrayList<PullParser>) getIntent().getExtras().getSerializable("Items");
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        output = findViewById(R.id.output);
        data = new ArrayList<String>();
    }

//when the submit button is clicked
    @Override
    public void onClick(View v) {
        if (v == submitButton) {
            findInfo();
        }
    }
//the date entered is collected and parsed to use as comparison with the dates in the earthquake items
    //if the dates are within the timeframe entered they are used for displaying information
    private void findInfo() {
        SimpleDateFormat enterDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat collectDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        ArrayList<PullParser> inPull = new ArrayList();

        try {
            Date starting_date = enterDate.parse(startDate.getText().toString());
            Date ending_date = enterDate.parse(endDate.getText().toString());

            for (int i = 0; i < alist.size(); i++) {
                try {
                    Date itemDate = collectDate.parse(alist.get(i).getPubDate());
                    if (itemDate.after(starting_date) && itemDate.before(ending_date)) {
                        inPull.add(alist.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            PullParser easterly = null;
            PullParser westerly = null;
            PullParser northerly = null;
            PullParser southerly = null;
            PullParser magnitude = null;
            PullParser largest_depth = null;
            PullParser smallest_depth = null;


            for (int i = 0; i < inPull.size(); i++) {

                if (i == 0) {
                    northerly = inPull.get(0);
                    southerly = inPull.get(0);
                    westerly = inPull.get(0);
                    easterly = inPull.get(0);
                    largest_depth = inPull.get(0);
                    smallest_depth = inPull.get(0);

                } else {
                    if (Float.parseFloat(inPull.get(i).getLatitude()) > Float.parseFloat(northerly.getLatitude())) {
                        northerly = inPull.get(i);
                    } else if (Float.parseFloat(inPull.get(i).getLatitude()) < Float.parseFloat(southerly.getLatitude())) {
                        southerly = inPull.get(i);
                    } else if (Float.parseFloat(inPull.get(i).getLongitude()) < Float.parseFloat(westerly.getLongitude())) {
                        westerly = inPull.get(i);
                    } else if (Float.parseFloat(inPull.get(i).getLongitude()) > Float.parseFloat(easterly.getLongitude())) {
                        easterly = inPull.get(i);
                    }
                }
                if (i == 0) {
                    magnitude = inPull.get(0);
                } else if (Float.parseFloat(inPull.get(i).getMagnitude()) > Float.parseFloat(magnitude.getMagnitude())) {
                    magnitude = inPull.get(i);
                }
                if (i == 0) {
                    largest_depth = inPull.get(0);
                } else if (Float.parseFloat(inPull.get(i).getDepth()) > Float.parseFloat(largest_depth.getDepth())) {
                    largest_depth = inPull.get(i);
                }
                if (i == 0) {
                    smallest_depth = inPull.get(0);
                } else if (Float.parseFloat(inPull.get(i).getDepth()) < Float.parseFloat(smallest_depth.getDepth())) {
                    smallest_depth = inPull.get(i);
                }
            }
            // adds data to the array list data which is then put into an arrayadapter to display in the listview
            String most_north = ("Most Northerly: " + northerly.getLocation());
            data.add(most_north);

            String most_south = ("Most Southerly: " + southerly.getLocation());
            data.add(most_south);

            String most_west = ("Most Westerly: " + westerly.getLocation());
            data.add(most_west);

            String most_east = ("Most Easterly: " + easterly.getLocation());
            data.add(most_east);


            String greatest_magnitude = ("Greatest Magnitude: " + magnitude.getMagnitude() + " " + magnitude.getLocation());
            data.add(greatest_magnitude);


            String greatest_depth = ("Largest Depth: " + largest_depth.getDepth() + "Kilometers" + " " + largest_depth.getLocation());
            data.add(greatest_depth);

            String shallowest_depth = ("Smallest Depth: " + smallest_depth.getDepth() + "Kilometers"  + " " + smallest_depth.getLocation());
            data.add(shallowest_depth);

            String matric_number = ("Matriculation Number: S1710196");
            data.add(matric_number);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SpecifyDate.this, android.R.layout.simple_list_item_1, data);

            output.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
