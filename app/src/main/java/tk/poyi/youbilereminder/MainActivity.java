package tk.poyi.youbilereminder;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static int lastPosition = 0;
    public androidx.viewpager.widget.ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        ////////////////////////////////////////////

        fab.setOnClickListener((View.OnClickListener) view -> {
            new Thread(() -> {


                String JsonString = SendWebRequestAsync("https://data.ntpc.gov.tw/api/datasets/71CD1490-A2DF-4198-BEF1-318479775E8A/json?page=0&size=650");

                TextView textView1 = findViewById(R.id.textView1);
                TextView textView2 = findViewById(R.id.textView2);
                ImageView imageView1 = findViewById(R.id.imageView1);
                TextView textView3 = findViewById(R.id.textView3);
                TextView textView4 = findViewById(R.id.textView4);
                ImageView imageView2 = findViewById(R.id.imageView2);
                TextView textView5 = findViewById(R.id.textView5);
                TextView textView6 = findViewById(R.id.textView6);
                ImageView imageView3 = findViewById(R.id.imageView3);
                runOnUiThread(() -> {
                    try {
                        JSONArray JsonObject = new JSONArray(JsonString);

                        for (int i = 0; i < JsonObject.length(); i++) {

                            if (JsonObject.getJSONObject(i).getString("sno").equals("1055")) {
                                textView1.setText(JsonObject.getJSONObject(i).getString("sna"));
                                textView2.setText(JsonObject.getJSONObject(i).getString("sbi"));
                                if (Integer.parseInt(JsonObject.getJSONObject(i).getString("sbi")) > 5) {
                                    imageView1.setImageResource(R.drawable.ok);
                                } else {
                                    imageView1.setImageResource(R.drawable.warning);
                                }
                            } else if (JsonObject.getJSONObject(i).getString("sno").equals("1703")) {
                                textView3.setText(JsonObject.getJSONObject(i).getString("sna"));
                                textView4.setText(JsonObject.getJSONObject(i).getString("bemp"));
                                if (Integer.parseInt(JsonObject.getJSONObject(i).getString("bemp")) > 5) {
                                    imageView2.setImageResource(R.drawable.ok);
                                } else {
                                    imageView2.setImageResource(R.drawable.warning);
                                }
                            } else if (JsonObject.getJSONObject(i).getString("sno").equals("1045")) {
                                textView5.setText(JsonObject.getJSONObject(i).getString("sna"));
                                textView6.setText(JsonObject.getJSONObject(i).getString("bemp"));
                                if (Integer.parseInt(JsonObject.getJSONObject(i).getString("bemp")) > 5) {
                                    imageView3.setImageResource(R.drawable.ok);
                                } else {
                                    imageView3.setImageResource(R.drawable.warning);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });


            }).start();

            new Thread(() -> {
                String WeatherJsonString = SendWebRequestAsync("https://opendata.cwb.gov.tw/api/v1/rest/datastore/O-A0003-001?Authorization=rdec-key-123-45678-011121314&locationName=%E6%96%B0%E5%BA%97&elementName=WDIR,WDSD,TEMP,HUMD,PRES,H_UVI,VIS");

                String[] desp = {"風向", "風速(km/h)", "溫度", "相對濕度", "氣壓(mBar)", "紫外線指數", "能見度(km)"};
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout2sec);
                runOnUiThread(() -> {
                    linearLayout.removeAllViews();
                    try {
                        JSONObject WeatherJsonObject = new JSONObject(Objects.requireNonNull(WeatherJsonString));
                        for (int i = 0; i < WeatherJsonObject.getJSONObject("records").getJSONArray("location").getJSONObject(0).getJSONArray("weatherElement").length(); i++) {
                            TextView tv = new TextView(this);
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f);
                            tv.setText(desp[i] + ":"
                                    + WeatherJsonObject.getJSONObject("records").getJSONArray("location").getJSONObject(0).getJSONArray("weatherElement").getJSONObject(i).getString("elementValue"));
                            if (i == 3) {
                                tv.setText(desp[i] + ":"
                                        + Float.parseFloat(WeatherJsonObject.getJSONObject("records").getJSONArray("location").getJSONObject(0).getJSONArray("weatherElement").getJSONObject(i).getString("elementValue")) * 100);
                            }
                            if (i == 1) {
                                tv.setText(desp[i] + ":"
                                        + Float.parseFloat(WeatherJsonObject.getJSONObject("records").getJSONArray("location").getJSONObject(0).getJSONArray("weatherElement").getJSONObject(i).getString("elementValue")) * 36);
                            }

                            linearLayout.addView(tv);
                        }
                        Snackbar.make(view, "Data updated", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });


            }).start();

            //setContentView(R.layout.activity_main);
        });

        //////////////////////////////////////////

        viewPager = (ViewPager)

                findViewById(R.id.viewPager);

        //ActionBar actionBar = getActionBar();
        TabsPagerAdapter mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        //actionBar.setHomeButtonEnabled(false);
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        ///////////////////////////////////////////////////////////////////////////
    }

    private String SendWebRequestAsync(String Url) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Url)
                .method("GET", null)
                .addHeader("Connection", "keep-alive")
                //.addHeader("Accept-Encoding", "gzip")
                .addHeader("Accept", "*/*")
                .addHeader("User-Agent", "PoyiAndroidBot(Project:YouBikeReminder)/0.1")
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    // Top Rated fragment activity
                    return new FirstFragment();
                case 1:
                    // Games fragment activity
                    return new SecondFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return 2;
        }

    }

}
