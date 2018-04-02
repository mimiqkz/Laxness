package teymi17.laxness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.widget.Toast;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.util.Calendar;

import teymi17.laxness.model.Quote;


public class MainActivity extends AppCompatActivity {
    private Quote quoteOfTheDay;
    public static final String TAG = MainActivity.class.getSimpleName();



    //View Variables
    Button shareButton;
    private TextView mQuoteText;
    private TextView mQuoteNovel;
    private TextView mQuoteDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sends a notification each day at a specific time
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.SECOND, 5);
        calendar.set(Calendar.HOUR_OF_DAY,12);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        final Intent intent = new Intent("Team17.Laxness.DISPLAY_NOTIFICATION");
        PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,broadcast);

        //Call function that finds and sets quote
        quoteOfTheDay = new Quote("dddddd dapur heimurinn.", "Barn ssssss,", "19");
        System.out.println("============================");
        getQoute(1);
        System.out.println("============================");
        mQuoteText = (TextView) findViewById(R.id.quote_text);
        mQuoteNovel = (TextView) findViewById(R.id.quote_novel);
        mQuoteDate = (TextView) findViewById(R.id.quote_date);

        mQuoteText.setText(quoteOfTheDay.getText());
        mQuoteNovel.setText(quoteOfTheDay.getNovel());
        mQuoteDate.setText(" " + String.valueOf(quoteOfTheDay.getYear()));

        shareButton = (Button)findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareQuote = quoteOfTheDay.getText() +" \t\t "+ quoteOfTheDay.getNovel() + " " + String.valueOf(quoteOfTheDay.getYear());
                String shareNovel = quoteOfTheDay.getNovel();
                String shareDate = " " + String.valueOf(quoteOfTheDay.getYear());
                myIntent.putExtra(intent.EXTRA_SUBJECT,"Laxness, tilvitnun dagsins");
                myIntent.putExtra(Intent.EXTRA_TEXT,shareQuote);
                startActivity(Intent.createChooser(myIntent,"Share using"));

            }
        });

    }

    private void getQoute(int id) {
        String qouteUrl = "https://laxnessapi.herokuapp.com/api/" + id;
        System.out.println(qouteUrl);
        if (isNetworkAvailable()) {
            System.out.println("ping1");
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(qouteUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("ping2");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println("ping3");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            System.out.println(jsonData);
                            quoteOfTheDay = parseQouteDetails(jsonData);
                            //We are not on main thread
                            //Need to call this method and pass a new Runnable thread
                            //to be able to update the view.
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Call the method to update the view.
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON caught: ", e);
                    }
                }
            });
        } else {
            // Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
      /* if(mQuoteText.getVisibility()== View.INVISIBLE){
           mQuoteText.setVisibility(View.INVISIBLE);
           mQuoteNovel.setVisibility(View.INVISIBLE);
           mQuoteDate.setVisibility(View.INVISIBLE);
        }
        else {
           mQuoteNovel.setVisibility(View.VISIBLE);
           mQuoteText.setVisibility(View.VISIBLE);
           mQuoteDate.setVisibility(View.VISIBLE);

       }*/
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo!= null && networkInfo.isConnected()) isAvailable = true;
        return isAvailable;
    }
    private void alertUserAboutError() {
        // AlertDialogFragment dialog = new AlertDialogFragment();
        // dialog.show(getFragmentManager(), "error_dialog");
        System.out.println("Alert");
    }
    private Quote parseQouteDetails(String jsonData) throws JSONException{
        Quote quote = new Quote();
        System.out.println(jsonData.length());
        quote.setNovel(getNovelName(jsonData));
        quote.setYear(getYearQoute(jsonData));
        quote.setText(getTextQoute(jsonData));

        return quote;
    }
    private String getNovelName(String jsonData) throws JSONException{
        JSONObject qoute = new JSONObject(jsonData.substring(1,jsonData.length()-1));
        String novel = qoute.getString("book");
        return  novel;
    }
    private  String getYearQoute(String jsonData) throws JSONException{
        JSONObject qoute = new JSONObject(jsonData.substring(1,jsonData.length()-1));
        String year = qoute.getString("year");
        return  year;
    }
    private String getTextQoute(String jsonData) throws JSONException {
        JSONObject qoute = new JSONObject(jsonData.substring(1,jsonData.length()-1));
        String theQoute = qoute.getString("quote");
        return  theQoute;
    }
    private void updateDisplay() {
        mQuoteText = (TextView) findViewById(R.id.quote_text);
        mQuoteNovel = (TextView) findViewById(R.id.quote_novel);
        mQuoteDate = (TextView) findViewById(R.id.quote_date);
        System.out.println("here in update "+ quoteOfTheDay.getText());
        mQuoteText.setText(quoteOfTheDay.getText());
        mQuoteNovel.setText(quoteOfTheDay.getNovel());
        mQuoteDate.setText(" " + String.valueOf(quoteOfTheDay.getYear()));
    }
}



