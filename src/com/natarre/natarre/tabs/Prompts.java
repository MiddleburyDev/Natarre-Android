package com.natarre.natarre.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Prompts extends NFragment {
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.prompts, container, false);
        return rootView;
    }

@Override
public void refresh() {
	// TODO Auto-generated method stub
	
}	
}
