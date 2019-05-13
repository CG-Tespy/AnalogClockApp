package cop4656.gcabrera.analogclockapp_solo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
    AnalogClockView clockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //clockView =                     (AnalogClockView) findViewById(R.id.analogClockView);
    }
}
