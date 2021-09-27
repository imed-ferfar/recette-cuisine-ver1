package com.imedf.recettes.ui.main;

import com.imedf.recettes.modele.Ingredient;
import com.imedf.recettes.modele.Recette;

import java.util.List;

public class ProviderRecette {

    public static List<Recette> listing;
    private Recette maRecette;

    public static List<Recette> getListing(){
        Recette maRecette = new Recette("Quiche au thon","Placer la pâte dans un moule et la piquer avec une fourchette\n" +
                "Badigeonner le fond avec de la crème fraiche\n" +
                "Couper les tomates en petits cubes et les mélanger avec le thon, les œufs, le reste de la crème fraiche, sel et poivre noir \n" +
                "Recouvrir le tout de gruyère\n" +
                "Cuire au four à 200°C pendant 20 à 25 min.");
        maRecette.ajouterIngredient(new Ingredient("","",55));
        listing.add(maRecette);

        return listing;
    }



}
