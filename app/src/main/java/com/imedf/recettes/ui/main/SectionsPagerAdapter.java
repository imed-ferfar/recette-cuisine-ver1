package com.imedf.recettes.ui.main;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.imedf.recettes.R;
import com.imedf.recettes.modele.Utilisateur;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[] {R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    private Utilisateur user;


    public SectionsPagerAdapter(Context context, FragmentManager fm, Utilisateur user) {
        super(fm);
        mContext = context;
        this.user = user;
    }


    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fr = null;
        switch (position){
            case 0:
                //fr = seConnecter.newInstance("5","6");
                fr = seConnecter.newInstance(user.getCourriel(),user.getMotPasse());
                break;
            case 1:
                fr = sInscrire.newInstance("1","2");
                break;
        }
        //return PlaceholderFragment.newInstance(position + 1);
        return fr;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}