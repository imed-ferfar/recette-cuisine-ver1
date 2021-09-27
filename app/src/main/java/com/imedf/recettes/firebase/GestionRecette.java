package com.imedf.recettes.firebase;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.imedf.recettes.EntranceActivity;
import com.imedf.recettes.modele.Ingredient;
import com.imedf.recettes.modele.Recette;
import com.imedf.recettes.modele.Utilisateur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GestionRecette {
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static DatabaseReference dbR = db.getReference("Recettes");
    private static String userId = auth.getCurrentUser().getUid();


    public static void saveRecette(Recette recette){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbR;
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();

        Gson gson = new Gson();
        String json = gson.toJson(recette);
        dbR = db.getReference().child("Users").child(userId).child("recettes");
        dbR.setValue(json);
       // dbR = db.getReference().child("Users").child(userId).child("recettes").child("ingredients");
       /* for (Ingredient tmp : recette.getIngredients())
            dbR.setValue(tmp);
        dbR = db.getReference().child("Users").child(userId).child("recettes").child("images");
        for (String tmp : recette.getImages())
            dbR.setValue(tmp);*/
    }

    public static void ajouterRecette(){
        int i =1;
        Recette recette =  new Recette("recette2","Preparation1") ;
        recette.ajouterImage("image11111111111111");
        recette.ajouterImage("image22222222222222");
        recette.ajouterImage("image33333333333333");
        String key = dbR.push().getKey(), keyImg;
       // HashMap<String, String> mapImages = new HashMap<>();
        Set<String> mapImages = new HashSet<>();
        HashMap<String, Ingredient> mapIngredient = new HashMap<>();
        //Recette mapRecette = new Recette("recette1","preparation");
        dbR.child(key).child("userId").setValue(userId);
        dbR.child(key).child("titre").setValue(recette.getTitre());
        dbR.child(key).child("preparation").setValue(recette.getPreparation());

        //mapImages.put("image","image1");mapImages.put("image","image2");mapImages.put("image","image3");
        mapImages.add("image1");
        mapImages.add("image2");
        mapImages.add("image3");

        for (String tmp : recette.getImages() ) {
            keyImg = dbR.push().getKey();
            dbR.child(key).child("images").child("image"+i).setValue(tmp);
            i++;
        }
    }
    public static void ajouterRecette(Recette recette){
        String key = dbR.push().getKey();
        int i =1;

        dbR.child(key).child("userId").setValue(userId);
        dbR.child(key).child("titre").setValue(recette.getTitre());
        dbR.child(key).child("preparation").setValue(recette.getPreparation());

        for (String tmp : recette.getImages() ) {
            dbR.child(key).child("images").child("image" + i).setValue(tmp);
        }
      /*  i =1;
        for (Ingredient tmp : recette.getIngredients() ) {
                dbR.child(key).child("ingredients").child("ingredient"+i).setValue(tmp);
        }*/
    }

  /*  public static List<Recette> getRecettes(){
        List<Recette> listRecette = new ArrayList<>();
        dbR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // listRecette = new ArrayList<>();
                int i = 1;
                Recette maRecette;
                for (DataSnapshot tmp : dataSnapshot.getChildren()) {
                    String titre = tmp.child("titre").getValue().toString();
                    String preparation = tmp.child("preparation").getValue().toString();
                    maRecette = new Recette(titre, preparation);
                   // String image = tmp.child("images").child("image" + i).getValue().toString();
                  //  maRecette.ajouterImage(image);
                   // Toast.makeText(EntranceActivity.this, "Image " + maRecette.getImage() + " :)", Toast.LENGTH_LONG).show();

                    listRecette.add(maRecette);
                   // Toast.makeText(EntranceActivity.this, "Titre " + maRecette.getTitre() + " :)", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return listRecette;
    }*/
}
