package com.modulus.ssc.view;

import com.modulus.ssc.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SecondFragment extends Fragment {

	//Pantalla Paradas
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View v = inflater.inflate(R.layout.second_frag, container, false);

	    return v;
	}

	public static SecondFragment newInstance() {

	    SecondFragment f = new SecondFragment();
	    Bundle b = new Bundle();

	    return f;
	}
}
