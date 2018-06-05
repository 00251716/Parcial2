package com.example.kevin.parcial2.Fragments;

import android.support.v4.app.Fragment;

public class ShowNewsFragment extends Fragment {

    /*private RecyclerView mRecyclerView;
    private ArrayList<News> mNews;
    private NewsListAdapter mNewsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Mostrar registros");

        // Aquí se infla el layout para el fragmento
        final View view = inflater.inflate(R.layout.news_layout, container, false);

        mRecyclerView =  (RecyclerView) view.findViewById(R.id.recyclerView);
        mNewsListAdapter = new NewsListAdapter(getContext(), mNews);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mNewsListAdapter);

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRegistros = DBHelper.ourInstance.getCurrentList();
        recyclerAdapter = new RecyclerViewAdapter(getContext(), mRegistros);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search,menu);

        final MenuItem searchItem = menu.findItem(R.id.item_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint("Ingrese un carnet");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerAdapter.filter(query);
                //se oculta el EditText
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mRegistros.clear();
        mRegistros.addAll(recyclerAdapter.getOriginal());

    }*/

}
