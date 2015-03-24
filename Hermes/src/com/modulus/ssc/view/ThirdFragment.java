package com.modulus.ssc.view;

import com.modulus.ssc.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ThirdFragment extends Fragment {
	
	//Pantalla Info
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View v = inflater.inflate(R.layout.third_frag, container, false);

	    return v;
	}

	public static ThirdFragment newInstance() {

	    ThirdFragment f = new ThirdFragment();
	    Bundle b = new Bundle();

	    return f;
	}

}
