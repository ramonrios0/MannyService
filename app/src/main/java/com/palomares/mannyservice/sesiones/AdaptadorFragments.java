package com.palomares.mannyservice.sesiones;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdaptadorFragments extends FragmentStateAdapter {

    public AdaptadorFragments(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentInicio();
            case 1:
                return new FragmentRegistro();
            default:
                return new FragmentInicio();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
