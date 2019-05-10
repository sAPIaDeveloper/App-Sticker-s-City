package com.desarrolladorgmail.a.jose.stickerscity.API;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.desarrolladorgmail.a.jose.stickerscity.API.QueryUtils;
import com.desarrolladorgmail.a.jose.stickerscity.Base_de_Datos.DataBaseRoom;
import com.desarrolladorgmail.a.jose.stickerscity.POJOS.Guardado;
import com.desarrolladorgmail.a.jose.stickerscity.POJOS.Sticker;
import com.desarrolladorgmail.a.jose.stickerscity.R;

import java.util.List;

public class StickerViewModel extends AndroidViewModel {
    public  MutableLiveData<List<Sticker>> stickerList;
    public  LiveData<List<Sticker>> stickersFavoritos;
    public  LiveData<List<Sticker>> stickersDescargados;
    public Sticker stickers;
    private Application application;
    Context context;
    String palabra;
    private static  DataBaseRoom dbr;
    public StickerViewModel(@NonNull Application application) {//constructor donde inicializo las variables
        super(application);
        this.application=application;
        dbr=DataBaseRoom.getInstance(application);
        stickersFavoritos=dbr.stickerDAO().getProducts();//Metodo  para obtener todos los sticker que estan en fav
        stickersDescargados=dbr.stickerDAO().getGifGuardado();//Metodo para obtener el historial los ultimos sticker que se han decargado

    }
    public void addSticker(Sticker sticker){new AddAsyncStick().execute(sticker);}// Metodo que llama a la clase asincrona encargada de guardar el sticker favorito
    public void addStickerSave(Guardado guardado){new AddAsyncStickGuardado().execute(guardado);}// Metodo que llama a la clase asincrona encargada de guardar el sticker descargado
    public void deleteSticker(Sticker sticker){new DeleteAsyncSticker().execute(sticker);}// Metodo que llama a la clase asincrona encargada de borrar el sticker favorito

    public LiveData<List<Sticker>> getFavoritSticker(){
       return stickersFavoritos;// metodo que devuelve los stickers favoritos
    }
    public LiveData<List<Sticker>> getGifGuardado(){return stickersDescargados;}
    public void borrarListaFavoritos(){ new DeleteAllAsyncSticker().execute();}// metodo para borrar todos los fav
    public void borrarListaGuardado(){ new DeleteAllAsyncGuardado().execute();}//metodo para borrar el historial de descargas


    public LiveData<List<Sticker>> getStickers(String busqueda){
        if(busqueda!=null){
            if(stickerList==null || !palabra.equals(busqueda)){// entra si la lista de sticker es nula o han cambiado la palabra a buscar
                palabra=busqueda;
                stickerList= new MutableLiveData<>();//LiveData es un tipo de lista donde puedes almacenar datos en el que puedes observar si hay cambios.
                loadStickers();
            }
        }
        return  stickerList;
    }




    public void loadStickers(){
        /*Obtengo la api_key para añadirla al enlace de la api*/
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(application);
        String apiKey = sharedPrefs.getString(
                application.getApplicationContext().getString(R.string.keyapikey),
                application.getApplicationContext().getString(R.string.apikey));
        String limite = sharedPrefs.getString(
                application.getApplicationContext().getString(R.string.limit),
                application.getApplicationContext().getString(R.string.limite));
        String STRING_URL_STICKER="http://api.giphy.com/v1/stickers/search?q=";
        STRING_URL_STICKER+=palabra;//añado la busqueda al enlace
        final Uri baseUri = Uri.parse(STRING_URL_STICKER);
        //Creo la uri con el enlace y le añado el limite y la api key
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api_key", apiKey);
        uriBuilder.appendQueryParameter("limit",limite);
        Log.i("La uri:",baseUri.getPath());
        // Metodo que se encarga de obtener el JSON de la api
        final RequestQueue queue = Volley.newRequestQueue(application);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, uriBuilder.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Llamo al metodo del QueryUtils para extraer los dato y lo guardo en la lista
                        List<Sticker>stickerList1=QueryUtils.extractStickers(response);
                        stickerList.setValue(stickerList1);// le doy al mutabllivedata el valor de la lista
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.getMessage()!=null){
                    Log.i("Error Volley",error.getMessage());
                }else Log.i("Error Volley","Error nulo");

            }
        });
        queue.add(stringRequest);

    }
    // Metodo para insertar el sticker fav
    private class AddAsyncStick extends AsyncTask<Sticker,Void,Void> {
        Sticker s;
        @Override
        protected Void doInBackground(Sticker... stickers) {
            if(stickers.length!=0){
                s = stickers[0];
                dbr.stickerDAO().insert(stickers[0]);
            }
           return null;
        }

    }
    // Metodo para insertar el sticker descargado
    private class AddAsyncStickGuardado extends AsyncTask<Guardado,Void,Void> {
        Guardado g;
        @Override
        protected Void doInBackground(Guardado... guardados) {
            if(guardados.length!=0){
                g = guardados[0];
                dbr.stickerDAO().insert(guardados[0]);
            }
            return null;
        }

    }
    private class DeleteAsyncSticker  extends AsyncTask<Sticker,Void,Void>{
        @Override
        protected Void doInBackground(Sticker... stickers) {
            dbr.stickerDAO().delete(stickers[0]);
            return null;
        }
    }

    private class DeleteAllAsyncSticker  extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            dbr.stickerDAO().deleteFav();
            return null;
        }
    }

    private class DeleteAllAsyncGuardado  extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            dbr.stickerDAO().deleteGuardar();
            return null;
        }
    }







}
