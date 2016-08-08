package com.projetoes.livrodereceitas.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.projetoes.livrodereceitas.R;

/**
 * A simple {@link Fragment} subclass.
 *
 * Use the {@link InitialFragment#getInstance} factory method to
 * create or get an instance of this fragment.
 */
public class InitialFragment extends Fragment {

    static InitialFragment fragment;
    public static final String TAG = "INITIAL_FRAGMENT";



    public InitialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment InitialFragment.
     */
    public static InitialFragment getInstance() {
        if (fragment == null ){
            fragment = new InitialFragment();
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
        return inflater.inflate(R.layout.fragment_initial, container, false);
    }




}
