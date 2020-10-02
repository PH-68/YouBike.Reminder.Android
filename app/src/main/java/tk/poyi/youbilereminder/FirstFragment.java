package tk.poyi.youbilereminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FirstFragment extends Fragment {

    public static String[] famousWords = {"將自己融入體制，讓考試成為習慣", "生命不是要超越別人，而是要成為考試機器", "沒有失敗，只有一百", "我從不懷疑自己的能力，只懷疑自己沒有背景", "誰要考試人生，他就一事無成；誰不能主宰自己，永遠是一個奴隸"
            , "勤奮的含義不是今天的熱血，而是明天的文憑，後天的保證", "沙漠之所以美麗，是因為你個人喜歡，而不是在不遠處有一片綠洲", "昨天已逝，明日是謎，面對今朝，好自為之，建議篡位", "信心源於實力,實力源於不斷的理解，而不是背書", "成功並不是是別人失敗時還在堅持,硬撐 而是找到方法"
            , "沒有人能改變你，別人只能影響你，能改變你的只有你自己", "貪圖省力的船夫，永遠華往下游，但你不知道的是也許下游有個桃花源", "當你懈怠的時候，請想一下你父母期待的眼神", "鐵杵磨成鐵粉", "沒有目標而生活，恰如沒有羅盤而航行，諷刺的是已經二十一世紀了", "背書永遠戰勝理解",
            "建中生是百分之一的理解，和百分之九十九的補習造成的", "知識的基礎，必須建立在一字不漏的背注釋上面", "待沒有退路之時，便是放棄之時", "要成功，先發瘋，頭腦簡單一直背", "這個世界不是你能做什麼，而是你該做什麼", "生活總會給你另一個機會，這個機會叫放棄", "背書才會傑出",
            "要成功，不要與馬賽跑，要開台跑車，馬上成功", "我一生的嗜好。除了革命外，只有好背書，我一天不背書，便不能生活。 - 孫中山", "人之所以痛苦，在於追求錯誤的東西", "沒考試，毋寧死！",
            "人生就像一本書，愚蠢的人一頁頁很快的翻過去，聰明的人則會仔細閱讀，但最後都會有看完的一天，結局一樣，何必當初呢?也許翻得快的人看得多而廣", "如果你不把自己推出舒適圈，你永遠都不會進步。", "有幽默感的人就能夠享受人類的矛盾，但也常常深陷於矛盾之中。",
            "世界不會在意你的態度，人們看的只是你的學歷。在你沒有學歷以前，最好閉嘴。", "你必需百分之百的把自己推銷給自己，尋求自我安慰", "鹹魚翻身，還是鹹魚。", "補習、原住民加分、背書，是人類活動的三大要素。"
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         new Thread(() -> {


            String JsonString = SendWebRequestAsync("https://data.ntpc.gov.tw/api/datasets/71CD1490-A2DF-4198-BEF1-318479775E8A/json?page=0&size=650");


            TextView textView1 = view.findViewById(R.id.textView1);
            TextView textView2 = view.findViewById(R.id.textView2);
            ImageView imageView1 = view.findViewById(R.id.imageView1);
            TextView textView3 = view.findViewById(R.id.textView3);
            TextView textView4 = view.findViewById(R.id.textView4);
            ImageView imageView2 = view.findViewById(R.id.imageView2);
            TextView textView5 = view.findViewById(R.id.textView5);
            TextView textView6 = view.findViewById(R.id.textView6);
            ImageView imageView3 = view.findViewById(R.id.imageView3);
            getActivity().runOnUiThread(() -> {
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
        ///////////////////////////////////////////////////////////////////
        Random random = new Random();
        TextView textView7 = (TextView) view.findViewById(R.id.textView7);
        textView7.setText(famousWords[random.ints(1, (famousWords.length + 1)).findFirst().getAsInt()]);
        /*view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/

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