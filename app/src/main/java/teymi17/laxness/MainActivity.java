package teymi17.laxness;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import teymi17.laxness.model.Quote;


public class MainActivity extends AppCompatActivity {
    private Quote quoteOfTheDay;

    //View Variables
    Button shareButton;
    private TextView mQuoteText;
    private TextView mQuoteNovel;
    private TextView mQuoteDate;
    File imagePath;

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
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,broadcast);

        //Call function that finds and sets quote
        quoteOfTheDay = new Quote("Dáið er alt án drauma; og dapur heimurinn.", "Barn náttúrunnar,", 1919);


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
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();


                /*Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareQuote = quoteOfTheDay.getText() +" \t\t "+ quoteOfTheDay.getNovel() + " " + String.valueOf(quoteOfTheDay.getYear());
                String shareNovel = quoteOfTheDay.getNovel();
                String shareDate = " " + String.valueOf(quoteOfTheDay.getYear());
                myIntent.putExtra(intent.EXTRA_SUBJECT,"Laxness, tilvitnun dagsins");
                myIntent.putExtra(Intent.EXTRA_TEXT,shareQuote);
                startActivity(Intent.createChooser(myIntent,"Share using"));
*/
            }
        });




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
    }


}
