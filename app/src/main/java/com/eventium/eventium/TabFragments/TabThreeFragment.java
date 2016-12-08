package com.eventium.eventium.TabFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventium.eventium.Evento;
import com.eventium.eventium.HTTPMethods;
import com.eventium.eventium.MostrarEventoFragment;
import com.eventium.eventium.NavigationDrawerActivity;

import com.eventium.eventium.R;
import com.eventium.eventium.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class TabThreeFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerview;
    private List<EventModel> mEventModel;
    private RVAdapter adapter;
    ArrayList<Evento> eventos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventos = new ArrayList<Evento>();

        HTTPMethods httpMethods = new HTTPMethods(3);
        httpMethods.ejecutarHttpAsyncTask();
        while (!httpMethods.getFinished()) ;
        List<Evento> list_events = httpMethods.getEvents();
        if (list_events != null) {
            for (int i = 0; i < list_events.size(); ++i) {
                eventos.add(list_events.get(i));
            }
        }
    }

    public void mostrarEventosFiltrados()
    {
        //System.out.println("Entro en mostrarEventosFiltrados");
        String direccionFiltraje = NavigationDrawerActivity.direccionFiltraje;
        HTTPMethods httpMethods = new HTTPMethods(9);
        httpMethods.setDireccionFiltraje(direccionFiltraje);
        httpMethods.ejecutarHttpAsyncTask();
        while (!httpMethods.getFinished());
        //System.out.println(httpMethods.getResultado());
        List<Evento> list_events = httpMethods.getEvents();
        ArrayList<Evento> eventosFiltrados = new ArrayList<Evento>();
        if (list_events != null) {
            for (int i = 0; i < list_events.size(); ++i) {
                eventosFiltrados.add(list_events.get(i));
            }
        }
        mEventModel = new ArrayList<>();
        for (int i = 0; i < eventosFiltrados.size(); ++i) {
            String fechas = eventosFiltrados.get(i).getFecha_ini() + " - " + eventosFiltrados.get(i).getFecha_fin();
            String horas = eventosFiltrados.get(i).getHora_ini() + " - " + eventosFiltrados.get(i).getHora_fin();
            String precio = eventosFiltrados.get(i).getPrecio() + " €";
            String encodedImage = eventosFiltrados.get(i).getPic();
            byte[] decodedImage = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap base64BitmapImage = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
            mEventModel.add(new EventModel(base64BitmapImage, eventosFiltrados.get(i).getTitle(), eventosFiltrados.get(i).getCiudad(), fechas, horas, precio, eventosFiltrados.get(i).getId()));
        }
        adapter = new RVAdapter(true);
        adapter.setRVE(mEventModel);
        recyclerview.setAdapter(adapter);
    }

    public void mostrarEventosTodos()
    {
        //System.out.println("Entro en mostrarEventosTodos");
        mEventModel = new ArrayList<>();
        for (int i = 0; i < eventos.size(); ++i) {
            String fechas = eventos.get(i).getFecha_ini() + " - " + eventos.get(i).getFecha_fin();
            String horas = eventos.get(i).getHora_ini() + " - " + eventos.get(i).getHora_fin();
            String precio = eventos.get(i).getPrecio() + " €";
            String encodedImage = eventos.get(i).getPic();
            byte[] decodedImage = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap base64BitmapImage = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
            mEventModel.add(new EventModel(base64BitmapImage, eventos.get(i).getTitle(), eventos.get(i).getCiudad(), fechas, horas, precio, eventos.get(i).getId()));
        }

        adapter = new RVAdapter(true);
        adapter.setRVE(mEventModel);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_three_fragment, container, false);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        mEventModel = new ArrayList<>();
        for (int i = 0; i < eventos.size(); ++i) {
            String fechas = eventos.get(i).getFecha_ini() + " - " + eventos.get(i).getFecha_fin();
            String horas = eventos.get(i).getHora_ini() + " - " + eventos.get(i).getHora_fin();
            String precio = eventos.get(i).getPrecio() + " €";
            String encodedImage = eventos.get(i).getPic();
            byte[] decodedImage = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap base64BitmapImage = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
            mEventModel.add(new EventModel(base64BitmapImage, eventos.get(i).getTitle(), eventos.get(i).getCiudad(), fechas, horas, precio, eventos.get(i).getId()));
        }

        adapter = new RVAdapter(true);
        adapter.setRVE(mEventModel);
        recyclerview.setAdapter(adapter);
        recyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(NavigationDrawerActivity.contexto, recyclerview, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String item = adapter.getItemRVE(position);
                        Toast.makeText(NavigationDrawerActivity.contexto, item, Toast.LENGTH_LONG).show();
                        Fragment fragment = new MostrarEventoFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("event", item);
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.contenedor_principal, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.menu_eventos, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Busca un evento");

        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter.setFilterRVE(mEventModel);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<EventModel> filteredModelList = filter(mEventModel, newText);
        adapter.setFilterRVE(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<EventModel> filter(List<EventModel> models, String query) {
        query = query.toLowerCase();

        final List<EventModel> filteredModelList = new ArrayList<>();
        for (EventModel model : models) {
            final String text = model.getTitulo().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}