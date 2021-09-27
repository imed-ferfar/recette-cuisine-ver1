package com.imedf.recettes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imedf.recettes.AsyncTask.AsyncChargementImg;
import com.imedf.recettes.AsyncTask.AsyncEnregistremantImg;
import com.imedf.recettes.daoSqlite.ImageDbAdapter;
import com.imedf.recettes.daoSqlite.IngredientDbAdapter;
import com.imedf.recettes.daoSqlite.LoginDbAdapter;
import com.imedf.recettes.daoSqlite.RecetteDbAdapter;
import com.imedf.recettes.firebase.GestionRecette;
import com.imedf.recettes.modele.Recette;
import com.imedf.recettes.modele.Utilisateur;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class EntranceActivity extends AppCompatActivity {
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static DatabaseReference dbR = db.getReference("Recettes");
    private static String userId = auth.getCurrentUser().getUid();

    private Intent recuIntent, monIntent;
    private ListView listingView;
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String,String>> maListe = new ArrayList<>();
    private Utilisateur user;
    private RecetteDbAdapter recetteAdp;
    private ImageDbAdapter imageAdp;
    private IngredientDbAdapter ingredAdp;
    private List<Recette> listLocale, listRecette;
    private String etatListe = "locale";



   /* private final String[] voiture={"Chevrolet", "Ford", "Nissan",
            "Hyundai", "Honda", "Toyota", "Ferrari","Chevrolet1", "Ford1", "Nissan1",
            "Hyundai1", "Honda1", "Toyota1", "Ferrari1", "Chevrolet2", "Ford2", "Nissan2",
            "Hyundai2", "Honda2", "Toyota12", "Ferrari2"};//data du listview

    private final String[] titre ={"Sedon", "Sedon", "Sport",
            "Sedon", "Sorport", "Sport", "Wow","Sedon", "Sedon", "Sport",
            "Sedon", "Sorport", "Sport", "Wow", "Sedon", "Sedon", "Sport",
            "Sedon", "Sorport", "Sport", "Wow"};//data du listview

    private int[] images = {R.drawable.coeur, R.drawable.icon, R.drawable.imc_heart, R.drawable.imc_help,
            R.drawable.imc_prefs, R.drawable.imc_suivi, R.drawable.imc_calcul, R.drawable.coeur,
            R.drawable.icon, R.drawable.imc_heart, R.drawable.imc_help, R.drawable.imc_prefs, R.drawable.imc_suivi,
            R.drawable.imc_calcul,R.drawable.imc_prefs, R.drawable.imc_suivi, R.drawable.imc_calcul,
            R.drawable.coeur, R.drawable.icon, R.drawable.imc_heart, R.drawable.imc_help};*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        listRecette = new ArrayList<>();
        dbR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 1;
                Recette  maRecette;
                for (DataSnapshot tmp : dataSnapshot.getChildren()){
                    String titre = tmp.child("titre").getValue().toString();
                    String preparation = tmp.child("preparation").getValue().toString();
                    maRecette = new Recette(titre, preparation);
                    String image = tmp.child("images").child("image1").getValue().toString();
                    maRecette.ajouterImage(image);
                    Log.v("imed","salut"+image);
               //     Toast.makeText(EntranceActivity.this,"Image "+image+" :)",Toast.LENGTH_LONG).show();

                    listRecette.add(maRecette);
                    // Toast.makeText(EntranceActivity.this,"Titre "+maRecette.getTitre()+" :)",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        setWidgets();
        setListeners();
        Toast.makeText(EntranceActivity.this,"Bienvenue "+user.getPrenom()+" :)",Toast.LENGTH_SHORT).show();

    }

    private void setWidgets() {
        recuIntent = getIntent();
        recetteAdp = new RecetteDbAdapter(EntranceActivity.this);
        imageAdp = new ImageDbAdapter(EntranceActivity.this);
        user = recuIntent.getParcelableExtra("user");
        auth = FirebaseAuth.getInstance();

        listingView = findViewById(R.id.listingView);

        try {
            listLocale = recetteAdp.chercherRecettes();
           // Log.v("imed","taille = "+listRecette.size());
        }catch(Exception e){
            Toast.makeText(EntranceActivity.this,"listLocale: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(EntranceActivity.this,"listLocale: "+listLocale.size(),Toast.LENGTH_LONG).show();
        String[] from = {"nom","image","titre"};
        int [] to = {R.id.txtIItemN, R.id.imgItem, R.id.txtIItemIngred};

        //construire la liste
        HashMap<String, String> map;
      /*  for (int i= 0; i<voiture.length; i++){
            map = new HashMap<>();
            map.put("nom", voiture[i]);
            map.put("image", String.valueOf(images[i]));
            map.put("titre",titre[i]);
            maListe.add(map);
        }*/
        for (Recette tmp : listLocale) {
            try {
                tmp.setImages(imageAdp.chercherImages(tmp.getIdRecette()));
            }catch(Exception e){
                Log.v("imed","ex:"+e.getMessage());
            }
         /*   map = new HashMap<>();
            map.put("nom", tmp.getTitre());
            map.put("image", tmp.getImage());
            map.put("titre",tmp.getPreparation());
            maListe.add(map);*/
        }
       /* simpleAdapter = new SimpleAdapter(EntranceActivity.this, maListe,
                R.layout.itemlist, from,to);*/


        //listingView.setAdapter(simpleAdapter);
        listingView.setAdapter(new CustomListAdapter(this,listLocale));

    }

    private void setListeners() {
        listingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
               // Toast.makeText(EntranceActivity.this,i+" : "+listRecette.get(i).getTitre(), Toast.LENGTH_LONG).show();
             //   Log.v("imed","taille = "+listRecette.size());
                monIntent = new Intent(EntranceActivity.this, AfficheRecetteActivity.class);
                if (etatListe.equals("locale"))
                    monIntent.putExtra("recette", listLocale.get(i));
                else
                    monIntent.putExtra("recette", listRecette.get(i));
                startActivity(monIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mon_menu,menu);
        return true;


    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.decon:
                auth.signOut();
                monIntent = new Intent(EntranceActivity.this, MainActivity.class);
                startActivity(monIntent);
                finish();
                break;
            case R.id.compte:
                monIntent = new Intent(EntranceActivity.this, CompteActivity.class);
                monIntent.putExtra("user", user);
                startActivityIfNeeded(monIntent,501);
                break;
            case R.id.ajoutRoucette:
                monIntent = new Intent(EntranceActivity.this, RecetteActivity.class);
                startActivity(monIntent);
                break;
            case R.id.filtre:
                etatListe = "locale";
                listingView.setAdapter(new CustomListAdapter(EntranceActivity.this,listLocale));
                break;
            case R.id.actualise:
                etatListe = "toute";
                listingView.setAdapter(new CustomListAdapter(EntranceActivity.this,listRecette));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 501 && resultCode == RESULT_OK){
            user = data.getParcelableExtra("user");
            //Toast.makeText(EntranceActivity.this, "ok :" + user.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //=========================================Classes ====================
}