package activeacademy.com.kittenbot.fragments_instructions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import activeacademy.com.kittenbot.R;


public class Fragment1 extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_one, container, false);

        return rootView;

    }
}
