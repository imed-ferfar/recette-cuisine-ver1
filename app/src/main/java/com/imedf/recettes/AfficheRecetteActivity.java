package com.imedf.recettes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imedf.recettes.modele.Recette;

public class AfficheRecetteActivity extends AppCompatActivity {
    private Intent recuIntent;
    private Recette recette;

    private ImageView imgRetour;
    private TextView txtTitreRec;
    private EditText editPrepara;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiche_recette);

        setWidgets();
        setListeners();
        afficherInfos();
    }

    private void afficherInfos() {
        txtTitreRec.setText(recette.getTitre());
        editPrepara.setText(recette.getPreparation());
    }

    private void setListeners() {
        imgRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setWidgets() {

        recuIntent = getIntent();
        recette = recuIntent.getParcelableExtra("recette");
        Toast.makeText(AfficheRecetteActivity.this, "titre :" + recette.getTitre(), Toast.LENGTH_SHORT).show();

        txtTitreRec = findViewById(R.id.txtTitreRec);
        editPrepara = findViewById(R.id.editPrepara);
        imgRetour = findViewById(R.id.imgRetour);
    }
}