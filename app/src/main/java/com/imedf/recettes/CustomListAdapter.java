package com.imedf.recettes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.imedf.recettes.modele.Recette;

import java.util.List;

import utils.GestionImage;

public class CustomListAdapter extends BaseAdapter {
    private List<Recette> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context aContext,  List<Recette> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.itemlist, null);
            holder = new ViewHolder();
            holder.flagView = (ImageView) convertView.findViewById(R.id.imgItem);
            holder.countryNameView = (TextView) convertView.findViewById(R.id.txtIItemN);
            holder.populationView = (TextView) convertView.findViewById(R.id.txtIItemIngred);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Recette recette = this.listData.get(position);
        holder.countryNameView.setText(recette.getTitre() );
        holder.populationView.setText("Preparation : " + recette.getPreparation());
        if (recette.getImage() != null)
            holder.flagView.setImageBitmap(GestionImage.convertirStringAbitmap(recette.getImage()));
        //int imageId = this.getMipmapResIdByName(country.getFlagName());

        //holder.flagView.setImageResource(imageId);
        //holder.flagView.setImageBitmap(GestionImage.convertirStringAbitmap(recette.getImage()));

        return convertView;
    }

    static class ViewHolder {
        ImageView flagView;
        TextView countryNameView;
        TextView populationView;
    }
}
