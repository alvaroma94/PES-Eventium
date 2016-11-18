package com.eventium.eventium;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Abel on 11/11/2016.
 */

public class MiPerfilFragment extends Fragment  {

    private static final int IMAGE_GALLERY_REQUEST = 1;
    String path;

    TextView name;
    TextView mail;
    //TextView city;
    //TextView direction;
    ImageView verified;
    ImageButton fotoMiPerfil;
    RatingBar reputacion;
    String idUsuario;

    public MiPerfilFragment() {
        // Required empty public constructor
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_miperfil, container, false);
        name = (TextView)view.findViewById(R.id.username);
        mail = (TextView)view.findViewById(R.id.emailtext);
        verified = (ImageView)view.findViewById(R.id.verified);
        fotoMiPerfil = (ImageButton)view.findViewById(R.id.fotoMiPerfil);
        fotoMiPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.contexto, "Has pulsado la imagen", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMAGE_GALLERY_REQUEST);
            }
        });
        reputacion = (RatingBar)view.findViewById(R.id.ratingBar);

        HTTPMethods httpMethods = new HTTPMethods(4);
        httpMethods.setToken_user(NavigationDrawerActivity.token);
        httpMethods.ejecutarHttpAsyncTask();
        while (!httpMethods.getFinished());
        String username = httpMethods.getResultado();
        //System.out.println(username);
        username = username.substring(14, username.length() - 2);
        //System.out.println(username);

        httpMethods = new HTTPMethods(1);
        httpMethods.setUsername(username);
        httpMethods.ejecutarHttpAsyncTask();
        while (!httpMethods.getFinished());
            Usuario user = httpMethods.getUser();

            idUsuario = user.getId();

            name.setText(user.getUsername());
            mail.setText("Email: " + user.getMail());
            byte[] decodedString = Base64.decode(user.getPic(), Base64.DEFAULT);
            Bitmap profilePic = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            fotoMiPerfil.setImageBitmap(profilePic);
            if(!user.getIsVerified()) {
                verified.setVisibility(View.INVISIBLE);
            }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                Uri geller = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Context c = NavigationDrawerActivity.contexto;
                Cursor cursor = c.getContentResolver().query(geller, filePathColumn, null, null, null);
                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow(filePathColumn[0]);
                    cursor.moveToFirst();
                    path = cursor.getString(column_index);
                    cursor.close();
                    // PONER LA NUEVA IMAGEN EN EL IMAGEBUTTON
                    Bitmap bm = BitmapFactory.decodeFile(path);
                    fotoMiPerfil.setImageBitmap(bm);
                    // PUT de /users/<id>
                    HTTPMethods httpMethods = new HTTPMethods(16);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                    byte[] bb = bos.toByteArray();
                    String encodedString = Base64.encodeToString(bb, Base64.DEFAULT);
                    httpMethods.setPic(encodedString);
                    httpMethods.setUser_id(Integer.parseInt(idUsuario));
                    httpMethods.ejecutarHttpAsyncTask();
                    while (!httpMethods.getFinished());
                    NavigationDrawerActivity.change_image = true;
                    NavigationDrawerActivity.userimage = bm;
                    Toast.makeText(NavigationDrawerActivity.contexto, "Imagen modificada correctamente", Toast.LENGTH_LONG).show();
                    // No se modifica, manana se arregla
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        //inflater.inflate(R.menu.menu_saldo, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
