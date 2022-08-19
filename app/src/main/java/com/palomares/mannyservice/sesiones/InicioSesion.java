package com.palomares.mannyservice.sesiones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.palomares.mannyservice.Principal;
import com.palomares.mannyservice.R;

public class InicioSesion extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 vista;
    AdaptadorFragments adaptadorFragments;
    Button iniciarSesion, registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion2);

        //Manejo de los fragments

        tabLayout = findViewById(R.id.tabSesion);
        vista = findViewById(R.id.view_pager);
        adaptadorFragments = new AdaptadorFragments(this);
        vista.setAdapter(adaptadorFragments);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vista.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vista.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }
    @Override
    public void onBackPressed() {
        return;
    }
}