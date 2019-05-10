package com.desarrolladorgmail.a.jose.stickerscity.API;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.desarrolladorgmail.a.jose.stickerscity.Base_de_Datos.DataBaseRoom;
import com.desarrolladorgmail.a.jose.stickerscity.POJOS.Sticker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class QueryUtils {
    public static Context context;
    public static List<Sticker> listaFavoritos;
    private static DataBaseRoom dbr;
    public QueryUtils() {
    }


    // Metodo que se encarga de extraer los datos de la API y de devolverselo al ViewModel
    public static List<Sticker> extractStickers(String SAMPLE_JSON) {
        dbr=DataBaseRoom.getInstance(context);// Creo una instancia de la base de datos
        List<Sticker> stickers = new ArrayList<Sticker>();


        try {
            //JSONArray para guardar arrays
            //JSONObject para guardar objetos
            JSONObject root=new JSONObject(SAMPLE_JSON);
            JSONArray datas= root.getJSONArray("data");
            for (int i = 0; i <datas.length() ; i++) {
                JSONObject data= datas.getJSONObject(i);
                JSONObject images=data.getJSONObject("images");
                JSONObject url=images.getJSONObject("original");
                String id=data.getString("id");
                String urlImage=url.getString("url");
                //Consulto a la base de datos y paro el proceso principal hasta que me devuelva algún valor
                Sticker sticker=new ConsultaFavorito().execute(id).get();              
                if(sticker!=null){// si no es nulo existe en la bbdd
                   stickers.add(sticker);
                }else{// si no se crea el objeto
                    stickers.add(new Sticker(urlImage,id,false));
                }               
            }

        } catch (JSONException e) {// Capturo si hay algun error al extraer la informacion del json

            Log.e("QueryUtils", "Problem parsing the stickers JSON results", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return stickers;
    }
    //Clase asincrona para comprobar si existe el sticker en la bbdd
    private static class ConsultaFavorito extends AsyncTask<String,Void,Sticker> {
        @Override
        protected Sticker doInBackground(String...ids) {
            Sticker sticker=dbr.stickerDAO().getProduct(ids[0]);         
            return sticker;
        }


    }

}
