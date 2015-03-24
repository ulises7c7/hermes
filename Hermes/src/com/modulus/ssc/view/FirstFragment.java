package com.modulus.ssc.view;

import com.modulus.ssc.R;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FirstFragment extends Fragment {
	
	//Pantalla Lineas y Paradas
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View v = inflater.inflate(R.layout.first_frag, container, false);
        
        return v;
	}
        
        public static FirstFragment newInstance() {
        	
        FirstFragment f = new FirstFragment();
        Bundle b = new Bundle();
        return f;
    }
        
      //boton verde
        public void goToLineasYparadas(View view) {
        	
        Intent intent = new Intent(getActivity().getApplicationContext(), ActivityMain1.class);
        startActivity(intent);
        	
        }

}
