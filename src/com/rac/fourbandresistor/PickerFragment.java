package com.rac.fourbandresistor;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class PickerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater lf, ViewGroup container, Bundle savedInstance) {
        return lf.inflate(R.layout.fragment_pickers, container, false);
    }
}
