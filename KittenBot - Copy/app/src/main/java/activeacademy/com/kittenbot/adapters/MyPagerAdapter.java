package activeacademy.com.kittenbot.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import activeacademy.com.kittenbot.fragments_instructions.Fragment1;
import activeacademy.com.kittenbot.fragments_instructions.Fragment2;
import activeacademy.com.kittenbot.fragments_instructions.Fragment3;

public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new Fragment1();

            case 1:
                return new Fragment2();

            case 2:
                return new Fragment3();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
