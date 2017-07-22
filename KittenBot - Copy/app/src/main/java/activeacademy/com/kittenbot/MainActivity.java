package activeacademy.com.kittenbot;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import activeacademy.com.kittenbot.adapters.MyPagerAdapter;

import static activeacademy.com.kittenbot.SettingsActivity.appColor;
import static activeacademy.com.kittenbot.SettingsActivity.getSavedColor;


public class MainActivity extends FragmentActivity {

    LinearLayout coloredLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        MyPagerAdapter mFragmentAdapter = new MyPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.photos_viewpager);
        mViewPager.setAdapter(mFragmentAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Button startButton = (Button) findViewById(R.id.startBtn);
        coloredLayout = (LinearLayout) findViewById(R.id.mainColoredLL);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettings();
            }
        });

        getSavedColor(this);
        setColor();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getSavedColor(this);
        setColor();
    }

    private void setColor(){
        switch (appColor){
            case "chatBlue":
                coloredLayout.setBackgroundResource(R.color.chatBlue);
                break;
            case "chatGreen":
                coloredLayout.setBackgroundResource(R.color.chatGreen);
                break;
            case "chatOrange":
                coloredLayout.setBackgroundResource(R.color.chatOrange);
                break;
            case "chatPurple":
                coloredLayout.setBackgroundResource(R.color.chatPurple);
                break;
            case "chatRed":
                coloredLayout.setBackgroundResource(R.color.chatRed);
                break;

        }
    }

    private void startSettings() {
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }

}
