package com.example.phone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





    }

    /**
     * Used to launch the program
     * @param view
     */
    public void click_display_home_activity(View view)
    {
        Intent intent = new Intent(this, Home_activity.class);
        startActivity(intent);




    }
}
