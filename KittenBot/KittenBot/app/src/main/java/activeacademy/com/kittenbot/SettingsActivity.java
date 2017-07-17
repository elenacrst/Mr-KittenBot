package activeacademy.com.kittenbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import activeacademy.com.kittenbot.adapters.SettingsAdapter;

import static android.R.attr.y;

public class SettingsActivity extends AppCompatActivity implements SettingsAdapter.SettingsAdapterOnClickHandler {

    RecyclerView mRecyclerView;
    public static SettingsAdapter settingsAdapter;
    String[] settingsList={"Change app color", "Privacy and security", "Data and storage",
            "Notifications"};
    ImageView doubleTapImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings);

        mRecyclerView = (RecyclerView) findViewById(R.id.settingsRV);
        setUpRecycler();

        doubleTapImg= (ImageView)findViewById(R.id.doubleTapImage);
        final GestureDetector gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            //here is the method for double tap
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //your action here for double tap e.g.
                //Log.d("OnDoubleTapListener", "onDoubleTap");
                startChat();
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return true;
            }
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });
//here yourView is the View on which you want to set the double tap action
        doubleTapImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });


    }

    private void startChat() {
        Intent intent = new Intent(this, MainChatActivity.class);
        startActivity(intent);
    }

    private void setUpRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        settingsAdapter = new SettingsAdapter(this);
        settingsAdapter.setData(settingsList);
        mRecyclerView.setAdapter(settingsAdapter);
    }


    @Override
    public void onClick(String option) {

    }
}
