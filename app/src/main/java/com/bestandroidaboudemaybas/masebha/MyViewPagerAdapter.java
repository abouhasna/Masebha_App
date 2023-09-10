package com.bestandroidaboudemaybas.masebha;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bestandroidaboudemaybas.masebha.fragments.MainFragment;
import com.bestandroidaboudemaybas.masebha.fragments.TesbihFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MainFragment();
            case 1:
                return new TesbihFragment();
            default:
                return new MainFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
