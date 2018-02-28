package teymi17.laxness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.TextView;
import android.view.View;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private Quote quoteOfTheDay;

    //View Variables
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

        Intent intent = new Intent("Team17.Laxness.DISPLAY_NOTIFICATION");
        PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,broadcast);

        //Call function that finds and sets quote
        quoteOfTheDay = new Quote("Dáið er alt án drauma; og dapur heimurinn.", "Barn náttúrunnar,", 1919);


        mQuoteText = (TextView) findViewById(R.id.quote_text);
        mQuoteNovel = (TextView) findViewById(R.id.quote_novel);
        mQuoteDate = (TextView) findViewById(R.id.quote_date);

        mQuoteText.setText(quoteOfTheDay.getText());
        mQuoteNovel.setText(quoteOfTheDay.getNovel());
        mQuoteDate.setText(" " + String.valueOf(quoteOfTheDay.getYear()));




    }


}
