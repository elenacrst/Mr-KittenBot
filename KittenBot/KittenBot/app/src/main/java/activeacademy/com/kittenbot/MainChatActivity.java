package activeacademy.com.kittenbot;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;
import org.alicebot.ab.Timer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import activeacademy.com.kittenbot.adapters.MessagesAdapter;
import activeacademy.com.kittenbot.adapters.SettingsAdapter;
import activeacademy.com.kittenbot.entities.Message;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainChatActivity extends ActionBarActivity {

    RelativeLayout pisicotRL;
    NestedScrollView ursicotRL;
    public static CustomEditText messageEditText;
    RecyclerView messagesRecyclerView;
    RelativeLayout baseRL;
    CircleImageView sendCIV;
    List<Message> messages = new ArrayList<>();
    MessagesAdapter messagesAdapter;

    static Bot bot;
    static Chat chat;


    private Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_chat);
        messagesRecyclerView = (RecyclerView) getActivity().findViewById(R.id.messagesRV);

        pisicotRL = (RelativeLayout) findViewById(R.id.pisicotRL);
        ursicotRL = (NestedScrollView) findViewById(R.id.ursicotRL);
        messageEditText = (CustomEditText) getActivity().findViewById(R.id.messageET);
        baseRL = (RelativeLayout) getActivity().findViewById(R.id.baseRL);
        sendCIV = (CircleImageView) findViewById(R.id.sendCIV);

        setupChat(MainChatActivity.this);

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
                    RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) pisicotRL.getLayoutParams();
                    relativeParams.setMargins(0, 0, 0, 0);  // left, top, right, bottom
                    pisicotRL.setLayoutParams(relativeParams);
                } else if (hasFocus) {
                   // Log.v("focus", "true");
                    RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) pisicotRL.getLayoutParams();

                   // DisplayMetrics displayMetrics = new DisplayMetrics();
                   // getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                   // int height = displayMetrics.heightPixels;
                    // int width = displayMetrics.widthPixels;
                    relativeParams.topMargin = 365;

                    pisicotRL.setLayoutParams(relativeParams);
                }
            }
        });

        baseRL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
              //  Log.v("click", "true");
                InputMethodManager mgr = (InputMethodManager)
                        baseRL.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                pisicotRL.clearFocus();
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
                //ursicotRL.smoothScrollTo(0,messagesAdapter.getItemCount()-1);
              /*  ursicotRL.post(new Runnable() {

                    @Override
                    public void run() {
                        ursicotRL.scrollTo(0,ScrollView.FOCUS_DOWN);
                    }
                });*/

                // messageEditText.clearFocus();
              /*  InputMethodManager mgr = (InputMethodManager)
                        baseRL.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);*/
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
                           /* ursicotRL.post(new Runnable() {

                                @Override
                                public void run() {
                                    ursicotRL.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });*/

                        }
                    }, 100);

                }
            }
        });
        ursicotRL.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
             //   @Override
              //  public void run() {
                ursicotRL.scrollTo(0,ursicotRL.getScrollY()+ursicotRL.getHeight());
                  //  ursicotRL.fullScroll(ScrollView.FOCUS_DOWN);
              //  }
            }
        });


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

        //receiving the assets from the app directory
        if(a) {
            AssetManager assets = context.getResources().getAssets();
            File jayDir = new File(Environment.getExternalStorageDirectory().toString() + "/hari/bots/Hari");
            boolean b = jayDir.mkdirs();
            if (jayDir.exists()) {
                //Reading the file
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
                            //copy file from assets to the mobile's SD card or any secondary memory
                            copyFile(in, out);
                            in.close();
                            in = null;
                            out.flush();
                            out.close();
                            out = null;
                        }
                    }
                } catch (IOException e) {
                    Log.v("excep1",e.toString());

                 //   e.printStackTrace();
                    AssetManager assets2 = context.getResources().getAssets();
                    File jayDir2 = new File(getFilesDir().toString() + "/hari/bots/Hari");
                    boolean b2 = jayDir.mkdirs();
                    if (jayDir2.exists()) {
                        //Reading the file
                        try {
                            for (String dir : assets2.list("Hari")) {
                                File subdir = new File(jayDir2.getPath() + "/" + dir);
                                boolean subdir_check = subdir.mkdirs();
                                for (String file : assets2.list("Hari/" + dir)) {
                                    File f = new File(jayDir2.getPath() + "/" + dir + "/" + file);
                                    if (f.exists()) {
                                        continue;
                                    }
                                    InputStream in = null;
                                    OutputStream out = null;
                                    in = assets2.open("Hari/" + dir + "/" + file);
                                    out = new FileOutputStream(jayDir2.getPath() + "/" + dir + "/" + file);
                                    //copy file from assets to the mobile's SD card or any secondary memory
                                    copyFile(in, out);
                                    in.close();
                                    in = null;
                                    out.flush();
                                    out.close();
                                    out = null;
                                }
                            }
                        } catch (IOException f) {
                            Log.v("excep2",f.toString());
                            f.printStackTrace();
                        }
                    }
                }

                //get the working directory
                MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/hari";
                System.out.println("Working Directory = " + MagicStrings.root_path);
                AIMLProcessor.extension = new PCAIMLProcessorExtension();
                //Assign the AIML files to bot for processing
                bot = new Bot("Hari", MagicStrings.root_path, "chat");
                chat = new Chat(bot);
            }
        }else{
            AssetManager assets3 = context.getResources().getAssets();
            File jayDir3 = new File(getFilesDir().toString() + "/hari/bots/Hari");
            boolean b3 = jayDir3.mkdirs();
            Log.v("else","executed");
            if (jayDir3.exists()) {
                //Reading the file
                try {
                    for (String dir : assets3.list("Hari")) {
                        File subdir = new File(jayDir3.getPath() + "/" + dir);
                        boolean subdir_check = subdir.mkdirs();
                        for (String file : assets3.list("Hari/" + dir)) {
                            File f = new File(jayDir3.getPath() + "/" + dir + "/" + file);
                            if (f.exists()) {
                                continue;
                            }
                            InputStream in = null;
                            OutputStream out = null;
                            in = assets3.open("Hari/" + dir + "/" + file);
                            out = new FileOutputStream(jayDir3.getPath() + "/" + dir + "/" + file);
                            //copy file from assets to the mobile's SD card or any secondary memory
                            copyFile(in, out);
                            in.close();
                            in = null;
                            out.flush();
                            out.close();
                            out = null;
                        }
                    }
                } catch (IOException e) {
                    Log.v("excep3",e.toString());
                    e.printStackTrace();
                }
            }

            //get the working directory
            MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/hari";
            System.out.println("Working Directory = " + MagicStrings.root_path);
            AIMLProcessor.extension = new PCAIMLProcessorExtension();
            //Assign the AIML files to bot for processing
            bot = new Bot("Hari", MagicStrings.root_path, "chat");
            chat = new Chat(bot);
        }

    }
    //check SD card availability
    public static boolean isSDCARDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    //copying the file
    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}
/*
    private void setupAIML(){
        //checking SD card availablility
        boolean a = isSDCARDAvailable();
//receiving the assets from the app directory
        AssetManager assets = getResources().getAssets();
        File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/hari/bots/Hari");
       // boolean b = jayDir.mkdirs();
        if (myDir.exists()) {
            //Reading the file
            try {
                for (String dir : assets.list("Hari")) {
                  //  File subdir = new File(myDir.getPath() + "/" + dir);
                    //boolean subdir_check = subdir.mkdirs();
                    for (String file : assets.list("Hari/" + dir)) {
                        File f = new File(myDir.getPath() + "/" + dir + "/" + file);
                        if (f.exists()) {
                            continue;
                        }
                        InputStream in ;
                       OutputStream out ;
                        in = assets.open("Hari/" + dir + "/" + file);
                        out = new FileOutputStream(myDir.getPath() + "/" + dir + "/" + file);
                        //copy file from assets to the mobile's SD card or any secondary memory
                        copyFile(in, out);
                        in.close();
                       // in = null;
                        out.flush();
                        out.close();
                      //  out = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//get the working directory
        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/hari";
      //  System.out.println("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension =  new PCAIMLProcessorExtension();
//Assign the AIML files to bot for processing
        bot = new Bot("Hari", MagicStrings.root_path, "chat");
        chat = new Chat(bot);
        String[] args = null;
      //  mainFunction(args);
    }
    //check SD card availability
    public static boolean isSDCARDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)? true :false;
    }
    //copying the file
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}

    //Request and response of user and the bot
   /* public static void mainFunction (String[] args) {
        MagicBooleans.trace_mode = false;
       // System.out.println("trace mode = " + MagicBooleans.trace_mode);
        Graphmaster.enableShortCuts = true;
        //Timer timer = new Timer();
        String request = "Hello.";
        String response = chat.multisentenceRespond(request);

       // System.out.println("Human: "+request);
      //  System.out.println("Robot: " + response);
    }

}*/
