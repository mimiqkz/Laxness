package teymi17.laxness;

import android.graphics.Bitmap;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import teymi17.laxness.model.Quote;


public class MainActivity extends AppCompatActivity {
    private Quote quoteOfTheDay;
    public static final String TAG = MainActivity.class.getSimpleName();

    //View Variables
    @BindView(R.id.shareButton)
    ImageButton shareButton;

    @BindView(R.id.todays_date)
    TextView mCurrentDate;

    @BindView(R.id.quote_text)
    TextView mQuoteText;

    @BindView(R.id.quote_subtext)
    TextView mQuoteSubText;


    File imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Sends a notification each day at a specific time
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,12);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        final Intent intent = new Intent("Team17.Laxness.DISPLAY_NOTIFICATION");
        PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,broadcast);
        getQuote();



        shareButton = (ImageButton)findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();
              }
        });

    }


    public static String getDateInIcelandic() {
        Locale icelandicLocale = new Locale("is");
        Date currentDate = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, icelandicLocale);
        return dateFormat.format(currentDate).toString();
    }

    /**
     * takes in id and gets data from api
     * and then renders sed data
     */
    private void getQuote() {
        String quoteUrl = "https://laxnessapi.herokuapp.com/api/today";
        System.out.println(quoteUrl);
        if (isNetworkAvailable()) {
            System.out.println("ping1");
            // toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(quoteUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("ping2");


    }


                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println("ping3");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            System.out.println(jsonData);
                            quoteOfTheDay = parseQuoteDetails(jsonData);
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
                            alertUserAbuoterror();
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

    /**
     * function that checks for network availability
     * @return true if there is network avalable
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo!= null && networkInfo.isConnected()) isAvailable = true;
        return isAvailable;
    }
    /*
    fix this maby
     */
    private void alertUserAbuoterror() {
        // AlertDialogFragment dialog = new AlertDialogFragment();
        // dialog.show(getFragmentManager(), "error_dialog");
        System.out.println("Alert");
    }

    /**
     * gets json data
     * @param jsonData
     * @return the quote that is in the data
     * @throws JSONException
     */
    private Quote parseQuoteDetails(String jsonData) throws JSONException{
        Quote quote = new Quote();
        quote.setNovel(getNovelName(jsonData));
        quote.setYear(getYearQuote(jsonData));
        quote.setText(getTextQuote(jsonData));

        return quote;
    }

    /**
     * gets the novel name form json
     * @param jsonData
     * @return
     * @throws JSONException
     */
    private String getNovelName(String jsonData) throws JSONException{
        JSONObject quote = new JSONObject(jsonData);
        String novel = quote.getString("book");
        return  novel;
    }

    /**
     * gets the year from json
     * @param jsonData
     * @return
     * @throws JSONException
     */
    private  String getYearQuote(String jsonData) throws JSONException{
        JSONObject quote = new JSONObject(jsonData);
        String year = quote.getString("year");
        return  year;
    }

    /**
     * gets the quote from json
     * @param jsonData
     * @return
     * @throws JSONException
     */
    private String getTextQuote(String jsonData) throws JSONException {
        JSONObject quote = new JSONObject(jsonData);
        String theQuote = quote.getString("quote");
        return  theQuote;
    }

    /**
     * updates the display
     */
    private void updateDisplay() {
        mCurrentDate.setText(getDateInIcelandic());
        mQuoteText.setText("„" + quoteOfTheDay.getText() + "“");
        mQuoteSubText.setText(quoteOfTheDay.getNovel() + ", " + quoteOfTheDay.getYear());
    }

    public Bitmap takeScreenshot(){
        View rootView =findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }
    private void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(),e);
        }catch (IOException e){
            Log.e("GREC", e.getMessage(), e);
        }
    }
    private void shareIt(){
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM,uri);

        startActivity(Intent.createChooser(sharingIntent,"Share via"));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // toggleRefresh();
            }
        });
        alertUserAbuoterror();

    }
}



