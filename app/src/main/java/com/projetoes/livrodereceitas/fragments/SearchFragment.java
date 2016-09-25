package com.projetoes.livrodereceitas.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.SparseBooleanArray;

import com.projetoes.livrodereceitas.CheckboxListViewAdapter;
import com.projetoes.livrodereceitas.DatabaseHelper;
import com.projetoes.livrodereceitas.MainActivity;
import com.projetoes.livrodereceitas.R;
import com.projetoes.livrodereceitas.SelectedIngredientsListViewAdapter;
import com.roughike.bottombar.BottomBar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#getInstance} factory method to
 * create or get a instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private static SearchFragment fragment;
    public static final String TAG = "SEARCH_FRAGMENT";



    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment getInstance() {
        if (fragment == null ){
            fragment = new SearchFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        final HashSet ingredientsArrayList = new HashSet();

        final Button addBtn = (Button) view.findViewById(R.id.add);
        addBtn.setEnabled(false);

        final ArrayList<String> myItems = new ArrayList<String>();
        final ListView selectIngListView = (ListView) view.findViewById(R.id.selected_ingredients_list);

        final Button srcBtn = (Button) view.findViewById(R.id.search_btn);


        // Ingredientes disponíveis
        List ingredientList = ((MainActivity)getActivity()).populateCompleteText();

        final Object[] itemSelected = new Object[1];
        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getContext(),android.R.layout.select_dialog_item,ingredientList);
        //Getting the instance of AutoCompleteTextView
        final AutoCompleteTextView actv= (AutoCompleteTextView)view.findViewById(R.id.auto_complete_ingredient);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // recupera o valor do text
                itemSelected[0] = parent.getItemAtPosition(position);
                addBtn.setEnabled(true);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);

            }
        });

        Button clearBtn = (Button)view.findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actv.setText("");
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientsArrayList.add(itemSelected[0]);
                // Utulização de ArrayList para o adapter

                if(!myItems.contains(itemSelected[0].toString())){
                    myItems.add(itemSelected[0].toString());
                }
                // Povoar a ListView de ingredientes selecionados com botão de remover
                selectIngListView.setAdapter(new SelectedIngredientsListViewAdapter(getActivity(), myItems));
                ListUtils.setDynamicHeight(selectIngListView);

                //Toast.makeText(getContext(),ingredientsArrayList.toString(), Toast.LENGTH_LONG).show();
                addBtn.setEnabled(false);
                Toast.makeText(getContext(),  myItems.toString() , Toast.LENGTH_SHORT).show();
            }
        });

        // Filtros disponíveis
        final List filterList = ((MainActivity)getActivity()).populateFilterList();
        final List selectedFilterList = ((MainActivity)getActivity()).populateFilterList();

        final ListView checkboxListView = (ListView) view.findViewById(R.id.filter_list);
        checkboxListView.setAdapter(new CheckboxListViewAdapter(getActivity(),filterList, selectedFilterList));
        ListUtils.setDynamicHeight(checkboxListView);

        srcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(myItems.isEmpty() || selectedFilterList.isEmpty()){
                    Toast.makeText(getContext(),  "Selecione ao menos um item e filtro" , Toast.LENGTH_SHORT).show();

                }
                else {
                    ((MainActivity) getActivity()).viewReceitasCompativeis(myItems, (ArrayList<String>) selectedFilterList);
                    ((MainActivity) getActivity()).onSearchButtonPressed(getView());
                }
            }
        });

        return view;

    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    public void clearFrag(){
        AutoCompleteTextView actv= (AutoCompleteTextView)getView().findViewById(R.id.auto_complete_ingredient);
    }

}
