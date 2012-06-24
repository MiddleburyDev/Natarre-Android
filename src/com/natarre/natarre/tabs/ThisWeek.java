package com.natarre.natarre.tabs;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ThisWeek extends NListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.this_week, container, false);
        return rootView;
    }

@Override
public void refresh() {
	
}

@Override
public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onLoaderReset(Loader<Cursor> arg0) {
	// TODO Auto-generated method stub
	
}	
}
