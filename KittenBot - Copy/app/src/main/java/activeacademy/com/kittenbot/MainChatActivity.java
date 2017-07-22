package activeacademy.com.kittenbot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import activeacademy.com.kittenbot.adapters.MessagesAdapter;
import activeacademy.com.kittenbot.entities.Message;
import de.hdodenhof.circleimageview.CircleImageView;

import static activeacademy.com.kittenbot.SettingsActivity.appColor;

public class MainChatActivity extends ActionBarActivity {

    RelativeLayout headerRL;
    NestedScrollView scroll;
    public static CustomEditText messageEditText;
    RecyclerView messagesRecyclerView;
    RelativeLayout baseRL;
    CircleImageView sendCIV;
    List<Message> messages = new ArrayList<>();
    MessagesAdapter messagesAdapter;

    ImageView backImage;

    static Bot bot;
    static Chat chat;

    private Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_chat);
        messagesRecyclerView = (RecyclerView) getActivity().findViewById(R.id.messagesRV);

        headerRL = (RelativeLayout) findViewById(R.id.headerRL);
        scroll = (NestedScrollView) findViewById(R.id.scroll);
        messageEditText = (CustomEditText) getActivity().findViewById(R.id.messageET);
        baseRL = (RelativeLayout) getActivity().findViewById(R.id.baseRL);
        sendCIV = (CircleImageView) findViewById(R.id.sendCIV);
        backImage = (ImageView)findViewById(R.id.backArrowIV);

        setupChat(MainChatActivity.this);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class destination = SettingsActivity.class;
                Context context = MainChatActivity.this;
                Intent intent = new Intent(context, destination);
                startActivity(intent);
            }
        });

        messageEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                InputMethodManager mgr = (InputMethodManager)
                        baseRL.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(baseRL, 0);
                return true;
            }
        });

       messageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.v("focus", "false");
                    RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) headerRL.getLayoutParams();
                    relativeParams.setMargins(0, 0, 0, 0);  // left, top, right, bottom
                    headerRL.setLayoutParams(relativeParams);
                } else if (hasFocus) {
                    RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) headerRL.getLayoutParams();

                    relativeParams.topMargin = 250;

                    headerRL.setLayoutParams(relativeParams);
                }
            }
        });

        baseRL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager mgr = (InputMethodManager)
                        baseRL.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                headerRL.clearFocus();
                messageEditText.clearFocus();

                return true;
            }
        });

        sendCIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageEditText.getText() != null && messageEditText.getText().toString().length() > 0) {
                    messages.add(new Message(messageEditText.getText().toString(), true));
                    String response = chat.multisentenceRespond(messageEditText.getText().toString());
                    if(response.contains("<")){
                        messages.add(new Message("I don't know what to answer", false));
                    }else
                    messages.add(new Message(response, false));
                }
                messageEditText.setText("");
                messagesAdapter.setData(messages);
                messagesRecyclerView.setAdapter(messagesAdapter);
                messagesRecyclerView.smoothScrollToPosition(
                        messagesRecyclerView.getAdapter().getItemCount() - 1);
                         }

        });

        prepareRecycler();
        messagesRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    messagesRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messagesRecyclerView.smoothScrollToPosition(
                                    messagesRecyclerView.getAdapter().getItemCount() - 1);

                        }
                    }, 100);

                }
            }
        });
        scroll.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                scroll.scrollTo(0,scroll.getScrollY()+scroll.getHeight());

            }
        });

        SettingsActivity.getSavedColor(this);
        setColor();


    }

    private void setColor(){
        switch (appColor){
            case "chatBlue":
                sendCIV.setBackgroundResource(R.drawable.sendimg);
                headerRL.setBackgroundResource(R.color.chatBlue);
                baseRL.setBackgroundResource(R.color.chatBlue);
                break;
            case "chatGreen":
                baseRL.setBackgroundResource(R.color.chatGreen);
                sendCIV.setBackgroundResource(R.drawable.sendgreen);
                headerRL.setBackgroundResource(R.color.chatGreen);
                break;
            case "chatOrange":
                baseRL.setBackgroundResource(R.color.chatOrange);
                sendCIV.setBackgroundResource(R.drawable.sendorange);
                headerRL.setBackgroundResource(R.color.chatOrange);
                break;
            case "chatPurple":
                baseRL.setBackgroundResource(R.color.chatPurple);
                sendCIV.setBackgroundResource(R.drawable.sendpurple);
                headerRL.setBackgroundResource(R.color.chatPurple);
                break;
            case "chatRed":
                baseRL.setBackgroundResource(R.color.chatRed);
                sendCIV.setBackgroundResource(R.drawable.sendred);
                headerRL.setBackgroundResource(R.color.chatRed);
                break;

        }
    }

    private void prepareRecycler() {
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setHasFixedSize(true);
        messagesAdapter = new MessagesAdapter();
        messagesAdapter.setData(messages);
        messagesRecyclerView.setAdapter(messagesAdapter);
    }

    public void setupChat(Context context) {
        boolean a = isSDCARDAvailable();

        if(a) {
            AssetManager assets = context.getResources().getAssets();
            File jayDir = new File(Environment.getExternalStorageDirectory().toString() + "/hari/bots/Hari");
            boolean b = jayDir.mkdirs();
            if (jayDir.exists()) {
                try {
                    for (String dir : assets.list("Hari")) {
                        File subdir = new File(jayDir.getPath() + "/" + dir);
                        boolean subdir_check = subdir.mkdirs();
                        for (String file : assets.list("Hari/" + dir)) {
                            File f = new File(jayDir.getPath() + "/" + dir + "/" + file);
                            if (f.exists()) {
                                continue;
                            }
                            InputStream in = null;
                            OutputStream out = null;
                            in = assets.open("Hari/" + dir + "/" + file);
                            out = new FileOutputStream(jayDir.getPath() + "/" + dir + "/" + file);
                            copyFile(in, out);
                            in.close();

                            out.flush();
                            out.close();
                        }
                    }
                } catch (IOException e) {
                    Log.v("excep1",e.toString());

                    AssetManager assets2 = context.getResources().getAssets();
                    File jayDir2 = new File(getFilesDir().toString() + "/hari/bots/Hari");

                    if (jayDir2.exists()) {
                        try {
                            for (String dir : assets2.list("Hari")) {
                                for (String file : assets2.list("Hari/" + dir)) {
                                    File f = new File(jayDir2.getPath() + "/" + dir + "/" + file);
                                    if (f.exists()) {
                                        continue;
                                    }
                                    InputStream in ;
                                    OutputStream out ;
                                    in = assets2.open("Hari/" + dir + "/" + file);
                                    out = new FileOutputStream(jayDir2.getPath() + "/" + dir + "/" + file);
                                    copyFile(in, out);
                                    in.close();
                                    out.flush();
                                    out.close();
                                }
                            }
                        } catch (IOException f) {
                            Log.v("excep2",f.toString());
                            f.printStackTrace();
                        }
                    }
                }

                MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/hari";
                System.out.println("Working Directory = " + MagicStrings.root_path);
                AIMLProcessor.extension = new PCAIMLProcessorExtension();
                bot = new Bot("Hari", MagicStrings.root_path, "chat");
                chat = new Chat(bot);
            }
        }else{
            AssetManager assets3 = context.getResources().getAssets();
            File jayDir3 = new File(getFilesDir().toString() + "/hari/bots/Hari");
            Log.v("else","executed");
            if (jayDir3.exists()) {
                try {
                    for (String dir : assets3.list("Hari")) {
                        for (String file : assets3.list("Hari/" + dir)) {
                            File f = new File(jayDir3.getPath() + "/" + dir + "/" + file);
                            if (f.exists()) {
                                continue;
                            }
                            InputStream in ;
                            OutputStream out ;
                            in = assets3.open("Hari/" + dir + "/" + file);
                            out = new FileOutputStream(jayDir3.getPath() + "/" + dir + "/" + file);
                            copyFile(in, out);
                            in.close();
                            out.flush();
                            out.close();
                        }
                    }
                } catch (IOException e) {
                    Log.v("excep3",e.toString());
                    e.printStackTrace();
                }
            }

            MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/hari";
            System.out.println("Working Directory = " + MagicStrings.root_path);
            AIMLProcessor.extension = new PCAIMLProcessorExtension();
            bot = new Bot("Hari", MagicStrings.root_path, "chat");
            chat = new Chat(bot);
        }

    }
    public static boolean isSDCARDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}

