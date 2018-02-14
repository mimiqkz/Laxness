package teymi17.laxness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
