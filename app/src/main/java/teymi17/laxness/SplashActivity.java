package teymi17.laxness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by meatyminx on 14/02/2018.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
