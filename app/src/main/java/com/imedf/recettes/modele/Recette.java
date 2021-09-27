package com.imedf.recettes.modele;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Recette implements Parcelable {
    private int idRecette;
    private String titre;
    private String preparation;
    private Set<Ingredient> ingredients;
    private Set<String> images;



    public Recette(String titre, String preparation) {
        this.titre = titre;
        this.preparation = preparation;
        images = new LinkedHashSet<>();
        ingredients = new LinkedHashSet<>();
    }
    public Recette() {
        ingredients = new LinkedHashSet<>();
        images = new LinkedHashSet<>();
    }


    protected Recette(Parcel in) {
        idRecette = in.readInt();
        titre = in.readString();
        preparation = in.readString();
    }

    public static final Creator<Recette> CREATOR = new Creator<Recette>() {
        @Override
        public Recette createFromParcel(Parcel in) {
            return new Recette(in);
        }

        @Override
        public Recette[] newArray(int size) {
            return new Recette[size];
        }
    };

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public Set<String> getImages() {
        return images;
    }

    public String getImage() {
        for (String tmp : images)
            return tmp;
        return null;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public void ajouterIngredient(Ingredient ingred){
        ingredients.add(ingred);
    }

    public void ajouterImage(String image){
        images.add(image);
    }

    public int getIdRecette() {
        return idRecette;
    }

    public void setIdRecette(int idRecette) {
        this.idRecette = idRecette;
    }

    public int nombreIngredients(){
        if (ingredients == null)
            return 0;
        return ingredients.size();
    }

   /* @Override
    public String toString() {
        return "Recette{" +
                "titre='" + titre + '\'' +
                ", preparation='" + preparation + '\'' +
                ", ingredients=" + listerIngredients() +
                ", images=" + listerImages() +
                '}';
    }*/

    public String listerImages() {
        String message="";
        for (String tmp : images)
            message += "\n"+tmp;
        return message;
    }

    private String listerIngredients(){
        String message="";
        for (Ingredient tmp : ingredients)
            message += "\n"+tmp.toString();
        return message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idRecette);
        parcel.writeString(titre);
        parcel.writeString(preparation);
    }
   /*@Override
   public String toString() {
       return new GsonBuilder().create().toJson(this, Recette.class);
   }*/
}
