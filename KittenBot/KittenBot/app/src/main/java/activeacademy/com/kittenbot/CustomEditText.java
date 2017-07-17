package activeacademy.com.kittenbot;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;


/**
 * Created by Stan on 6/30/2017.
 */
public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    Context context;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    int ok=0;
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ok++;
            // User has pressed Back key. So hide the keyboard
            //InputMethodManager mgr = (InputMethodManager)
             //       context.getSystemService(Context.INPUT_METHOD_SERVICE);
          //  mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
            // TODO: Hide your view as you do it in your activity
            Log.v("back","pressed");
            if(ok==2) {
                ok=0;
                MainChatActivity.messageEditText.clearFocus();
            }

        }
        return super.onKeyPreIme(keyCode, event);
    }
}