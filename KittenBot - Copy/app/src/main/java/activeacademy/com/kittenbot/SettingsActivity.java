package activeacademy.com.kittenbot;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import activeacademy.com.kittenbot.adapters.SettingsAdapter;

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
            @Override
            public boolean onDoubleTap(MotionEvent e) {
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
        doubleTapImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });

        getSavedColor(this);
        applyColor();


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


    ImageView[] colorImages = new ImageView[5];
    Button btnSave, btnCancel;

    private void saveColor(String color) {

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("app color",color).apply();

    }
    public static String appColor = "chatBlue";


    public static void getSavedColor(Context context) {
        String restoredText = PreferenceManager.getDefaultSharedPreferences(context).getString("app color",null);

        if (restoredText != null) {
            appColor = restoredText;
        }

    }

    public void applyColor(){
        LinearLayout coloredLayout = (LinearLayout)findViewById(R.id.coloredLayout);
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

    String tempColor;
    @Override
    public void onClick(String option) {
        final Dialog dialog = new Dialog(this);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                getSavedColor(SettingsActivity.this);
                applyColor();
            }
        });
        switch (option){
            case "Change app color":
                dialog.setContentView(R.layout.color_dialog);

                colorImages[0] = (ImageView) dialog.findViewById(R.id.blueIV);
                colorImages[1] = (ImageView) dialog.findViewById(R.id.greenIV);
                colorImages[2] = (ImageView) dialog.findViewById(R.id.orangeIV);
                colorImages[3] = (ImageView) dialog.findViewById(R.id.purpleIV);
                colorImages[4] = (ImageView) dialog.findViewById(R.id.redIV);
                btnSave          = (Button) dialog.findViewById(R.id.save);
                btnCancel        = (Button) dialog.findViewById(R.id.cancel);

                tempColor=null;

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveColor(tempColor);
                        dialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                colorImages[0].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Drawable color = v.getBackground();
                            Log.v("color-select",color.toString());
                            colorImages[1].setBackgroundResource(R.color.chatGreen);
                            colorImages[2].setBackground(getDrawable(R.color.chatOrange));
                            colorImages[3].setBackground(getDrawable(R.color.chatPurple));
                            colorImages[4].setBackground(getDrawable(R.color.chatRed));

                            tempColor = "chatBlue";
                            colorImages[0].setBackgroundResource(R.drawable.selected_color0);
                        }
                    });
                colorImages[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drawable color = v.getBackground();
                        Log.v("color-select",color.toString());
                        colorImages[0].setBackgroundResource(R.color.chatBlue);
                        colorImages[2].setBackground(getDrawable(R.color.chatOrange));
                        colorImages[3].setBackground(getDrawable(R.color.chatPurple));
                        colorImages[4].setBackground(getDrawable(R.color.chatRed));

                        tempColor ="chatGreen";
                        colorImages[1].setBackgroundResource(R.drawable.selected_color1);

                    }
                });
                colorImages[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drawable color = v.getBackground();
                        Log.v("color-select",color.toString());

                        colorImages[0].setBackgroundResource(R.color.chatBlue);
                        colorImages[1].setBackground(getDrawable(R.color.chatGreen));
                        colorImages[3].setBackground(getDrawable(R.color.chatPurple));
                        colorImages[4].setBackground(getDrawable(R.color.chatRed));

                        tempColor ="chatOrange";
                        colorImages[2].setBackgroundResource(R.drawable.selected_color2);


                    }
                });
                colorImages[3].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drawable color = v.getBackground();
                        Log.v("color-select",color.toString());

                        colorImages[0].setBackgroundResource(R.color.chatBlue);
                        colorImages[2].setBackground(getDrawable(R.color.chatOrange));
                        colorImages[1].setBackground(getDrawable(R.color.chatGreen));
                        colorImages[4].setBackground(getDrawable(R.color.chatRed));

                        tempColor ="chatPurple";
                        colorImages[3].setBackgroundResource(R.drawable.selected_color3);

                    }
                });
                colorImages[4].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drawable color = v.getBackground();
                        Log.v("color-select",color.toString());

                        colorImages[0].setBackgroundResource(R.color.chatBlue);
                        colorImages[2].setBackground(getDrawable(R.color.chatOrange));
                        colorImages[3].setBackground(getDrawable(R.color.chatPurple));
                        colorImages[1].setBackground(getDrawable(R.color.chatGreen));

                        tempColor ="chatRed";
                        colorImages[4].setBackgroundResource(R.drawable.selected_color4);

                    }
                });
                break;
        }

        dialog.show();

    }
}
