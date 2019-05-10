package com.desarrolladorgmail.a.jose.stickerscity.Activities;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.desarrolladorgmail.a.jose.stickerscity.API.StickerViewModel;
import com.desarrolladorgmail.a.jose.stickerscity.Adapter.CategoryAdapter;
import com.desarrolladorgmail.a.jose.stickerscity.Fragments.BusquedaMostrarHorizontal;
import com.desarrolladorgmail.a.jose.stickerscity.R;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE_ASK_PERMISSIONS =7;
    private Fragment fragmentBusqueda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager= (ViewPager) findViewById(R.id.viewpager);
        fragmentBusqueda=new BusquedaMostrarHorizontal();//Creo una instancia de la clase BusquedaMostrarHorizontal
        Resources resources=getResources();
        // Creo instancia del category adapter que es quien gestiona el cambio de pestañas
        CategoryAdapter categoryAdapter= new CategoryAdapter(getSupportFragmentManager(),resources,fragmentBusqueda);
        viewPager.setAdapter(categoryAdapter);
        TabLayout tb= findViewById(R.id.viewlayout);
        tb.setupWithViewPager(viewPager);
        comprobarPermisos();

    }

    /*
    * Metodo que se encarga de comprobar si existen los permisos de lectura y escritura que se necesitan para poder descargar las imagenes.
    * Si no existiese tal permiso se lo pediria al usuario en tiempo de ejecución ya que estos permisos son de tipo peligroso ya que requiere
    * recursos que incluyen información privada del usuario, o bien que podrían afectar los datos almacenados del usuario o el funcionamiento de otras aplicaciones
    * */
    public void comprobarPermisos(){
        int permiso= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);// este metodo devuelve el codigo del permiso
        if(permiso!=PackageManager.PERMISSION_GRANTED){//Si el codigo coincide con el codigo establecido para un permiso concedido si no se le pide
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permisos, int[] resultado) {//Metodo que comprueba si el usuario a denegado o no el permiso
        if(REQUEST_CODE_ASK_PERMISSIONS == requestCode) {
            if (resultado[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Permiso denegado ! ", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permisos, resultado);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Metodo para inflar el menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();//obtengo el id del item que ha pulsado
        StickerViewModel model=ViewModelProviders.of(this).get(StickerViewModel.class);
        //noinspection SimplifiableIfStatement
        if (id == R.id.borraFav) {//Si ha pulsado el item de borrar favoritos llamo al metodo que me borra de la bbdd todos los favoritos
            model.borrarListaFavoritos();
            return true;
        }else{//si no borro el historial de las ultimas descargas
            model.borrarListaGuardado();
            return true;
        }

        //return super.onOptionsItemSelected(item);
    }

}
