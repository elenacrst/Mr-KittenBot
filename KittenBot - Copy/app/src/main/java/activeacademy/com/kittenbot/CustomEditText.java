package activeacademy.com.kittenbot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

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
            if(ok==2) {
                ok=0;
                MainChatActivity.messageEditText.clearFocus();
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }
}