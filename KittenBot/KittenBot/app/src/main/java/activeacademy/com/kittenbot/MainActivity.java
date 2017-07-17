package activeacademy.com.kittenbot;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import activeacademy.com.kittenbot.adapters.MessagesAdapter;
import activeacademy.com.kittenbot.adapters.MyPagerAdapter;
import activeacademy.com.kittenbot.fragments_instructions.Fragment1;
import activeacademy.com.kittenbot.fragments_instructions.Fragment2;
import activeacademy.com.kittenbot.fragments_instructions.Fragment3;


public class MainActivity extends FragmentActivity {

    private MyPagerAdapter mFragmentAdapter;
    private ViewPager mViewPager;

    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Apply the Adapter
        //tabs
        mFragmentAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.photos_viewpager);
        mViewPager.setAdapter(mFragmentAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        startButton = (Button)findViewById(R.id.startBtn);

       // MainChatActivity.setupChat(MainActivity.this);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettings();
            }
        });



    }

    private void startSettings() {
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }


}
