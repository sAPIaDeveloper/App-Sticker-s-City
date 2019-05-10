package com.desarrolladorgmail.a.jose.stickerscity.Adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.desarrolladorgmail.a.jose.stickerscity.Fragments.FavoritosFragment;

public class CategoryAdapter extends FragmentPagerAdapter {
    Resources resources;
    Fragment fragment;
    public CategoryAdapter(FragmentManager fm, Resources resources,Fragment fragment) {//Constructor donde recibo los datos necesarios
        super(fm);
        this.resources=resources;
        this.fragment=fragment;
    }

    // Metodo que devuelve el fragment que vamos a mostrar segun en la pestaña en la que estemos
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return   fragment;
            case 1:
                return  new FavoritosFragment();
            default:
                return fragment;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }// Numero de pestañas que tengo
    //Devuelvo los nombres de cada pestaña para que se muestren
    public String getPageTitle(int position) {
        switch (position){
            case 0: return  "Busqueda";
            case 1: return  "Favoritos";
            default: return "Busqueda";
        }
    }
}