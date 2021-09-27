package com.imedf.recettes.modele;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private String description;
    private String unite;
    private double quantite;

    public Ingredient() {
    }

    public Ingredient(String description, String unite, double quantite) {
        this.description = description;
        this.unite = unite;
        this.quantite = quantite;
    }

    protected Ingredient(Parcel in) {
        description = in.readString();
        unite = in.readString();
        quantite = in.readDouble();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeString(unite);
        parcel.writeDouble(quantite);
    }
/*
    @Override
    public String toString() {
        return "Ingredient{" +
                "description='" + description + '\'' +
                ", unite='" + unite + '\'' +
                ", quantite=" + quantite +
                '}';
    }*/
}
