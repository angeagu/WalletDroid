package com.kursea.walletdroid.listener;

import com.kursea.walletdroid.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar;
import android.util.Log;

public class WalletDroidTabListener implements ActionBar.TabListener {
 
    private Fragment fragment;
 
    public WalletDroidTabListener(Fragment fg)
    {
        this.fragment = fg;
    }
 
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        Log.i("WalletDroid", tab.getText() + " reseleccionada.");
    }
 
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        Log.i("WalletDroid", tab.getText() + " seleccionada.");
        ft.replace(R.id.fragment_container, fragment);
    }
 
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        Log.i("WalletDroid", tab.getText() + " deseleccionada.");
        ft.remove(fragment);
    }

}
