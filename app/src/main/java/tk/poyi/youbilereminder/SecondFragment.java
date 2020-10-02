package tk.poyi.youbilereminder;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SecondFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //rdec-key-123-45678-011121314
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://opendata.cwb.gov.tw/api/v1/rest/datastore/O-A0003-001?Authorization=rdec-key-123-45678-011121314&locationName=%E6%96%B0%E5%BA%97&elementName=WDIR,WDSD,TEMP,HUMD,PRES,H_UVI,VIS")
                //.url("https://api.openweathermap.org/data/2.5/weather?q=%E6%96%B0%E5%BA%97%E5%8D%80,%E6%96%B0%E5%8C%97%E5%B8%82&appid=44d31a1e35fb9166af8c0af891f9cf10")
                .method("GET", null)
                .addHeader("Connection", "keep-alive")
                //.addHeader("Accept-Encoding", "gzip")
                .addHeader("Accept", "*/*")
                .addHeader("User-Agent", "PoyiAndroidBot(Project:YouBikeReminder)/0.1")
                .addHeader("Host", "data.ntpc.gov.tw")
                .build();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Response response = client.newCall(request).execute();
            JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
            String[] desp = {"風向","風速(km/h)","溫度","相對濕度","氣壓(mBar)","紫外線指數","能見度(km)"};
            for (int i = 0; i < json.getJSONObject("records").getJSONArray("location").getJSONObject(0).getJSONArray("weatherElement").length(); i++) {
                TextView tv = new TextView(getContext());
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25f);
                tv.setText(desp[i]+":"
                        +json.getJSONObject("records").getJSONArray("location").getJSONObject(0).getJSONArray("weatherElement").getJSONObject(i).getString("elementValue"));
                if (i ==3){
                    tv.setText(desp[i]+":"
                            +Float.parseFloat( json.getJSONObject("records").getJSONArray("location").getJSONObject(0).getJSONArray("weatherElement").getJSONObject(i).getString("elementValue"))*100);
                }
                if (i ==1){
                    tv.setText(desp[i]+":"
                            +Float.parseFloat( json.getJSONObject("records").getJSONArray("location").getJSONObject(0).getJSONArray("weatherElement").getJSONObject(i).getString("elementValue"))*36);
                }

                //tv.setText(desp[i]+json.getJSONObject("main").getJSONObject("temp"));
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout2sec);
                linearLayout.addView(tv);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            String WeatherJsonString = SendWebRequestAsync("https://opendata.cwb.gov.tw/api/v1/rest/datastore/O-A0003-001?Authorization=rdec-key-123-45678-011121314&locationName=%E6%96%B0%E5%BA%97&elementName=WDIR,WDSD,TEMP,HUMD,PRES,H_UVI,VIS");

            String[] desp = {"風向", "風速(km/h)", "溫度", "相對濕度", "氣壓(mBar)", "紫外線指數", "能見度(km)"};
            LinearLayout linearLayout =view.findViewById(R.id.linearLayout2sec);
            getActivity().runOnUiThread(() -> {
                linearLayout.removeAllViews();
                try {
                    JSONObject WeatherJsonObject = new JSONObject(Objects.requireNonNull(WeatherJsonString));
                    for (int i = 0; i < WeatherJsonObject.getJSONObject("records").getJSONArray("location").getJSONObject(0).getJSONArray("weatherElement").length(); i++) {
                        TextView tv = new TextView(getContext());
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });


        }).start();

    }
    private String SendWebRequestAsync(String Url) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Url)
                .method("GET", null)
                .addHeader("Connection", "keep-alive")
                //.addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept", "*/*")
                .addHeader("User-Agent", "PoyiAndroidBot(Project:YouBikeReminder)/0.1")
                .addHeader("Host", "data.ntpc.gov.tw")
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}