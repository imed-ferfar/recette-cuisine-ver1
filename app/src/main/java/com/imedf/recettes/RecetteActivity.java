package com.imedf.recettes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.imedf.recettes.AsyncTask.AsyncChargementImg;
import com.imedf.recettes.AsyncTask.AsyncEnregistremantImg;
import com.imedf.recettes.daoSqlite.ImageDbAdapter;
import com.imedf.recettes.daoSqlite.IngredientDbAdapter;
import com.imedf.recettes.daoSqlite.RecetteDbAdapter;
import com.imedf.recettes.firebase.GestionRecette;
import com.imedf.recettes.modele.Ingredient;
import com.imedf.recettes.modele.Recette;

import java.util.ArrayList;
import java.util.HashMap;

public class RecetteActivity extends AppCompatActivity {
    static final String[] UNITE_INGREDIENT = new String[]{"gr","ml","verre","c/s","c/c","pc","pincée"};
    private ImageView imgRetour, imageAdd, imageTest;
    private EditText editTitre, editIngred, editQte, editPreparation;
    private TextView txtErreurSaisi, textViewEnreg;
    private Spinner spinnerUnite;
    private LinearLayout mainLayoutvh3;
    private CheckBox checkPartage;
    private Button btnAjoutImage;

    private ListView listingView, listingImages;
    private SimpleAdapter simpleAdapter;
    private ArrayAdapter simpleAdpImg;
    private String titre, modePreparation, ingredient, quantiteStr, unite;
    private double quantite;

    private ArrayList<HashMap<String,String>> maListe;
    private ArrayList<Bitmap> maListeImg;
    private HashMap <String, String> map, mapImg;

    private Recette maRecette;
    private Ingredient ingred;
    private int compteur = 0,cptImg =0;


    private Intent galleryInt, recuIntent, monIntent, retourIntent;
    private AsyncChargementImg asyncCharImg;
    private AsyncEnregistremantImg asyncEnreImg;

    private RecetteDbAdapter recetteAdp;
    private ImageDbAdapter imageAdp;
    private IngredientDbAdapter ingredAdp;

    String[] from = {"n","ingred","unite","qte"};
    int [] to = {R.id.txtIItemN, R.id.txtIItemIngred, R.id.txtIItemU, R.id.txtIItemQte};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recette);
        getSupportActionBar().hide();

        recetteAdp = new RecetteDbAdapter(RecetteActivity.this);
        imageAdp = new ImageDbAdapter(RecetteActivity.this);
        ingredAdp = new IngredientDbAdapter(RecetteActivity.this);
        setWidgets();
        setListeners();
    }
    private void setWidgets() {
        imgRetour = findViewById(R.id.imgRetour);
        imageAdd = findViewById(R.id.imageAdd);
        editIngred = findViewById(R.id.editIngred);
        editTitre = findViewById(R.id.editTitre);
        editQte = findViewById(R.id.editQte);
        editPreparation = findViewById(R.id.editPrepara);
        spinnerUnite = findViewById(R.id.spinnerUnite);
        listingView = findViewById(R.id.listingView);
        listingImages = findViewById(R.id.listingImages);
        txtErreurSaisi = findViewById(R.id.txtErreurSaisi);
        textViewEnreg = findViewById(R.id.textViewEnreg);
        btnAjoutImage = findViewById(R.id.btnAjoutImage);

        imageTest = findViewById(R.id.imageTest);
        checkPartage = findViewById(R.id.checkPartage);

        mainLayoutvh3 = findViewById(R.id.mainLayoutvh3);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, UNITE_INGREDIENT);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerUnite.setAdapter(adp);
        maRecette = new Recette();

        maListe = new ArrayList<>();
        maListeImg  = new ArrayList<>();

        simpleAdapter = new SimpleAdapter(RecetteActivity.this, maListe,
                R.layout.itemingredient, from,to);
        listingView.setAdapter(simpleAdapter);

        simpleAdpImg = new ArrayAdapter<Bitmap>(this, R.layout.itemimages,maListeImg);
        listingImages.setAdapter(simpleAdpImg);
    }

    private void setListeners() {
        //========================= Retour =========================//
        txtErreurSaisi.setVisibility(TextView.GONE);
        imgRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //========================= Ajouter un ingrédient =========================//
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredient = editIngred.getText().toString();
                quantiteStr = editQte.getText().toString();
                if (validerDataIngredient()){
                    unite = spinnerUnite.getSelectedItem().toString();
                    ingred = new Ingredient(ingredient,unite, quantite);
                    maRecette.ajouterIngredient(ingred);
                    try {
                        ajouterIngedientItem();
                    } catch (Exception ex){
                        Toast.makeText(RecetteActivity.this, "ok :" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //========================= Enregistre image =========================//
        textViewEnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncEnreImg = new AsyncEnregistremantImg(RecetteActivity.this);
                asyncEnreImg.execute(asyncCharImg.getBitmapReslt());
               // maRecette.ajouterImage(asyncEnreImg.getImgStrReslt());
                textViewEnreg.setVisibility(TextView.GONE);
            }
        });
    }
    //========================= Ajouter Recette =========================//
    public void OnAjouterRecette(View view) {
        txtErreurSaisi.setVisibility(TextView.GONE);
        int id;
        titre = editTitre.getText().toString();
       // Toast.makeText(RecetteActivity.this, "image :" + asyncEnreImg.getImgStrReslt(), Toast.LENGTH_SHORT).show();
        modePreparation = editPreparation.getText().toString();
        if (validerData()){
            maRecette.ajouterImage(asyncEnreImg.getImgStrReslt());
            maRecette.setTitre(titre);
            maRecette.setPreparation(modePreparation);
           try {
                recetteAdp.ajouterRecette(maRecette);
                id = recetteAdp.chercherDernierId();
                maRecette.setIdRecette(id);
                imageAdp.ajouterImages(maRecette);
                ingredAdp.ajouterIngredients(maRecette);
            } catch(Exception e){
                Toast.makeText(RecetteActivity.this, "ex" +e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (checkPartage.isChecked())
               // GestionRecette.ajouterRecette(maRecette);
                Toast.makeText(RecetteActivity.this, "image" +maRecette.getImage(), Toast.LENGTH_SHORT).show();
        }

     //  GestionRecette.ajouterRecette();
    }

    private boolean validerData(){

        if (titre.equals("")){
            txtErreurSaisi.setText("Tapez le titre de la recette!");
            txtErreurSaisi.setVisibility(TextView.VISIBLE);
            return false;
        }
        if (maRecette.nombreIngredients() < 1){
            txtErreurSaisi.setText("Ajouter au moins un seul ingredient!");
            txtErreurSaisi.setVisibility(TextView.VISIBLE);
            return false;
        }
        if (modePreparation.equals("")){
            txtErreurSaisi.setText("Tapez les instructions de la préparation de la recette!");
            txtErreurSaisi.setVisibility(TextView.VISIBLE);
            return false;
        }
        return true;
    }
    private boolean validerDataIngredient(){

        if (ingredient.equals("")){
            txtErreurSaisi.setText("Tapez le nom de l'ingrédient à ajouter!");
            txtErreurSaisi.setVisibility(TextView.VISIBLE);
            return false;
        }
        if (quantiteStr.equals("")){
            txtErreurSaisi.setText("Tapez la quantité de l'ingrédient à ajouter!");
            txtErreurSaisi.setVisibility(TextView.VISIBLE);
            return false;
        }
        try{
            quantite = Double.parseDouble(editQte.getText().toString());
        } catch (NumberFormatException ex){
            quantite = -1;
        }
        if (quantite <=0 ){
            txtErreurSaisi.setText("Tapez une quantité valide & positive!");
            txtErreurSaisi.setVisibility(TextView.VISIBLE);
            return false;
        }
        return true;
    }


    private void ajouterIngedientItem(){
        map = new HashMap<>();
        map.put("n", String.valueOf(++compteur));
        map.put("ingred", ingredient);
        map.put("unite",unite);
        map.put("qte", String.valueOf(quantite));
        maListe.add(map);
        if (compteur ==1){
            mainLayoutvh3.setVisibility(LinearLayout.VISIBLE);
            listingView.setVisibility(ListView.VISIBLE);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    public void OnAjoutPhoto(View view) {
        galleryInt = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryInt,101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            asyncCharImg = new AsyncChargementImg(RecetteActivity.this, imageTest);
            asyncCharImg.execute(data.getData());
            textViewEnreg.setVisibility(TextView.VISIBLE);
            btnAjoutImage.setVisibility(Button.GONE);
        }else {
            Toast.makeText(getApplicationContext(),"Vous n'avez pas choisi d'image", Toast.LENGTH_LONG).show();
        }
    }

    ////////////////////////====================== CLASSES ======================////////////////////////


}

