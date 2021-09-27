package com.imedf.recettes;

import android.content.Intent;
import android.os.Bundle;


import com.imedf.recettes.databinding.ActivityMainBinding;
import com.imedf.recettes.modele.Utilisateur;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.imedf.recettes.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Utilisateur user = new Utilisateur("","");//pour le test
    private String courriel, motPasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent recuIntent = getIntent();
        courriel = recuIntent.getStringExtra("courriel");
        motPasse = recuIntent.getStringExtra("motPasse");
        if(courriel != null) {
            user = new Utilisateur(courriel, motPasse);
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), user);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        /*FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        } catch(Exception ex){
            Toast.makeText(MainActivity.this, "ex : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}