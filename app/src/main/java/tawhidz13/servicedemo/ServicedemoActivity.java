package tawhidz13.servicedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ServicedemoActivity extends Activity {
	Button btnStart,btnStop;
	Intent in;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnStart=(Button)findViewById(R.id.button1);
        btnStop=(Button)findViewById(R.id.button2);
        
        in=new Intent(this,MyService.class);
    }
    
    public void startClicked(View v)
    {
    	startService(in);
    }
    public void stopClicked(View v)
    {
    	stopService(in);
    }
}