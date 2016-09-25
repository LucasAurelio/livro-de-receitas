package com.projetoes.livrodereceitas.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.projetoes.livrodereceitas.MainActivity;
import com.projetoes.livrodereceitas.R;

public class TypeSearchFragment extends Fragment {

    private static TypeSearchFragment fragment;
    public static final String TAG = "TYPE_SEARCH_FRAGMENT";
    private final String SEARCH_COMPATIBILIDADE = "COMPATIBILIDADE";
    private final String SEARCH_SIMILARIDADE = "SIMILARIDADE";

    public TypeSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static TypeSearchFragment getInstance() {
        if (fragment == null) {
            fragment = new TypeSearchFragment();
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
        final View view = inflater.inflate(R.layout.fragment_type_search, container, false);
        final Button srcBtnCompativel = (Button) view.findViewById(R.id.compatibilidade_btn);
        final Button srcBtnSimilar = (Button) view.findViewById(R.id.similaridade_btn);

        srcBtnCompativel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).setTypeSearch(SEARCH_COMPATIBILIDADE);
                ((MainActivity) getActivity()).onTypeButtonPressed(getView());
            }
        });

        srcBtnSimilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).setTypeSearch(SEARCH_SIMILARIDADE);
                ((MainActivity) getActivity()).onTypeButtonPressed(getView());
            }
        });

        return view;
    }
    
}
