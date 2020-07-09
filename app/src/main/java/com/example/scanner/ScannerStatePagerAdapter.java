package com.example.scanner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.ArrayList;
import java.util.List;

public class ScannerStatePagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments = new ArrayList<>();



    public ScannerStatePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
