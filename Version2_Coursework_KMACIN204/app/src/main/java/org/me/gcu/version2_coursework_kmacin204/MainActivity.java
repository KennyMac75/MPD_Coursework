/*Kenny MacInnes
    S1710196 */

package org.me.gcu.version2_coursework_kmacin204;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.AsyncTask;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import android.graphics.Color;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    private Button startButton;
    private Button mapView;
    private Button dateRange;
    private String result = "";
    private String url1 = "";
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    LinkedList<PullParser> alist = new LinkedList<PullParser>();
    LinearLayout feedData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag", "in onCreate");
        // Set up the raw links to the graphical components
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        Log.e("MyTag", "after startButton");
        dateRange = (Button) findViewById(R.id.SpecifyDate);
        dateRange.setOnClickListener(this);
        mapView = (Button) findViewById(R.id.ViewMap);
        mapView.setOnClickListener(this);
        // More Code goes here
        feedData = (LinearLayout) findViewById(R.id.xmlFeed);

    }

    @Override
    public void onClick(View aview) {

        //case statement which contains the buttons for the users to click on and either show the data or navigate to other screens

        switch (aview.getId()) {

            case R.id.startButton:
                startProgress();
                break;

            case R.id.SpecifyDate:
                Intent intent1 = new Intent(MainActivity.this, SpecifyDate.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Items", alist);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;

            case R.id.ViewMap:
                Intent intent2 = new Intent(MainActivity.this, MapDisplay.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("Items", alist);
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;

            default:
                break;
        }

    }

    public void startProgress() {
        GetAsync runFeed = new GetAsync();
        runFeed.execute();
    }


    private class GetAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(urlSource);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag", "after ready");
                //
                // Now read the data. Make sure that there are no specific hedrs
                // in the data file that you need to ignore.
                // The useful data that you need is in each of the item entries
                //
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                    Log.e("MyTag", inputLine);

                }
                in.close();
                return result;
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception in run");
            }

            //
            // Now that you have the xml data you can parse it
            //


            return null;
        }


        @Override
        protected void onPostExecute(String string) {

            LinkedList<PullParser> alist = parseData(result);

            if (alist != null) {
                Log.e("MyTag", "List not null");
                for (Object o : alist) {
                    Log.e("MyTag", o.toString());
                }
            } else {
                Log.e("MyTag", "List is null");
            }

            for (int i = 0; i < alist.size(); i++) {
                Button button = new Button(MainActivity.this);
                button.setText(alist.get(i).getLocation() + "\n" + alist.get(i).getMagnitude());

                String latitude = alist.get(i).getLatitude();
                String longitude = alist.get(i).getLongitude();
                String category = alist.get(i).getCategory();
                String depth = alist.get(i).getDepth();
                String magnitude = alist.get(i).getMagnitude();
                String location = alist.get(i).getLocation();
                String pubDate = alist.get(i).getPubDate();
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MoreInfo.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Items", alist);
                        intent.putExtras(bundle);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                        intent.putExtra("category", category);
                        intent.putExtra("depth", depth);
                        intent.putExtra("magnitude", magnitude);
                        intent.putExtra("location", location);
                        intent.putExtra("date", pubDate);
                        startActivity(intent);
                    }
                });
                if (Float.parseFloat(alist.get(i).getMagnitude()) <= 0.8) {
                    button.setBackgroundColor(Color.parseColor("green"));
                } else if (Float.parseFloat(alist.get(i).getMagnitude()) > 0.8 && Float.parseFloat(alist.get(i).getMagnitude()) <= 1.8) {
                    button.setBackgroundColor(Color.parseColor("yellow"));
                } else if (Float.parseFloat(alist.get(i).getMagnitude()) > 1.8) {
                    button.setBackgroundColor(Color.parseColor("red"));
                }
                feedData.addView(button);
                button.setTextSize(18);
            }
        }


        private LinkedList<PullParser> parseData(String dataParse) {

            PullParser item = new PullParser();
            try {
                boolean insideItem = false;
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(dataParse));
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    // Found a start tag
                    if (eventType == XmlPullParser.START_TAG) {
                        // Check which Tag we have
                        // if the data is within the item then inside item is set to true and throughout the loop if the data is within the item it will take the data
                        if (xpp.getName().equalsIgnoreCase("item")) {

                            Log.e("MyTag", "Item Start Tag found");
                            item = new PullParser();
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {

                            if (insideItem) {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag", "Title is -->" + temp);
                                item.setTitle(temp);
                            }

                        } else if (xpp.getName().equalsIgnoreCase("description")) {

                            if (insideItem) {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                Log.e("MyTag", "Description is --> " + temp);
                                item.setDescription(temp);
                            }
                        } else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("link")) {

                                if (insideItem) {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("MyTag", "Link is -->" + temp);
                                    item.setLink(temp);
                                }
                            } else
                                // Check which Tag we have
                                if (xpp.getName().equalsIgnoreCase("pubDate")) {

                                    if (insideItem) {

                                        // Now just get the associated text
                                        String temp = xpp.nextText();
                                        // Do something with text
                                        Log.e("MyTag", "Publish Date is -->" + temp);
                                        item.setPubDate(temp);
                                    }
                                } else
                                    // Check which Tag we have
                                    if (xpp.getName().equalsIgnoreCase("category")) {

                                        if (insideItem) {

                                            // Now just get the associated text
                                            String temp = xpp.nextText();
                                            // Do something with text
                                            Log.e("MyTag", "Category is -->" + temp);
                                            item.setCategory(temp);
                                        }
                                    } else
                                        // Check which Tag we have
                                        if (xpp.getName().equalsIgnoreCase("geo:lat")) {

                                            if (insideItem) {

                                                // Now just get the associated text
                                                String temp = xpp.nextText();
                                                // Do something with text
                                                Log.e("MyTag", "Latitude is --> " + temp);
                                                item.setLatitude(temp);
                                            }
                                        } else
                                            // Check which Tag we have
                                            if (xpp.getName().equalsIgnoreCase("geo:long")) {

                                                if (insideItem) {

                                                    // Now just get the associated text
                                                    String temp = xpp.nextText();
                                                    // Do something with text
                                                    Log.e("MyTag", "Longitude is --> " + temp);
                                                    item.setLongitude(temp);
                                                }
                                            }


                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {


                        alist.add(item);

                    }


                    // Get the next event
                    eventType = xpp.next();

                } // End of while
            } catch (XmlPullParserException ae1) {
                Log.e("MyTag", "Parsing error" + ae1.toString());
            } catch (IOException ae1) {
                Log.e("MyTag", "IO error during parsing");
            }

            Log.e("MyTag", "End document");

            return alist;

        }

    }
}