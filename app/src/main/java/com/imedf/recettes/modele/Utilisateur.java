package com.imedf.recettes.modele;

import android.os.Parcel;
import android.os.Parcelable;

public class Utilisateur implements Parcelable {
    private String id;
    private String nom;
    private String prenom;
    private String courriel;
    private String motPasse;
    private String imgProfil;

    public Utilisateur() {
    }

    public Utilisateur(String nom, String prenom, String courriel, String motPasse, String imgProfil) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.courriel = courriel;
        this.motPasse = motPasse;
        this.imgProfil = imgProfil;
    }

    public Utilisateur(String nom, String prenom, String courriel, String motPasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.courriel = courriel;
        this.motPasse = motPasse;
    }

    public Utilisateur(String courriel, String motPasse) {
        this.courriel = courriel;
        this.motPasse = motPasse;
    }

    public Utilisateur(String courriel) {
        this.courriel = courriel;
    }

    protected Utilisateur(Parcel in) {
        id = in.readString();
        nom = in.readString();
        prenom = in.readString();
        courriel = in.readString();
        motPasse = in.readString();
        imgProfil = in.readString();
    }

    public static final Creator<Utilisateur> CREATOR = new Creator<Utilisateur>() {
        @Override
        public Utilisateur createFromParcel(Parcel in) {
            return new Utilisateur(in);
        }

        @Override
        public Utilisateur[] newArray(int size) {
            return new Utilisateur[size];
        }
    };

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getCourriel() {
        return courriel;
    }

    public String getMotPasse() {
        return motPasse;
    }

    public String getImgProfil() {
        return imgProfil;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }

    public void setImgProfil(String imgProfil) {
        this.imgProfil = imgProfil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", courriel='" + courriel + '\'' +
                ", motPasse='" + motPasse + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nom);
        parcel.writeString(prenom);
        parcel.writeString(courriel);
        parcel.writeString(motPasse);
        parcel.writeString(imgProfil);
    }
}
