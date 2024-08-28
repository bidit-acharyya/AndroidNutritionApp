package com.example.afinal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class SavedFragment extends Fragment {
    TextView foodText, URLText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_saved, null);
        foodText = fragmentView.findViewById(R.id.foodTextView);
        URLText = fragmentView.findViewById((R.id.URLTextView));

        MainActivity mainActivity = new MainActivity();
        foodText.setText(mainActivity.getSavedFood());

        //foodText.setText(getArguments().getString("FAVS"));

        return fragmentView;
    }
    // lifecycle methods are available
    @Override
    public void onStart() {
        super.onStart();
    }

}