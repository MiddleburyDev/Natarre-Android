package com.natarre.natarre;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

import com.natarre.natarre.tabs.Favorites;
import com.natarre.natarre.tabs.Popular;
import com.natarre.natarre.tabs.Prompts;
import com.natarre.natarre.tabs.ReadingList;
import com.natarre.natarre.tabs.ThisWeek;

public class Home extends FragmentActivity implements OnSharedPreferenceChangeListener {

	private ViewPager nPager;
	private NPagerAdapter nPagerAdapter;

	@SuppressWarnings("unchecked")
	private static final Class<? extends Fragment>[] tabs = new Class[] {
			ThisWeek.class, Popular.class, Prompts.class, Favorites.class,
			ReadingList.class };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		// Pager

		nPagerAdapter = new NPagerAdapter(getSupportFragmentManager());
		nPager = (ViewPager) findViewById(R.id.screen_pager);
		nPager.setAdapter(nPagerAdapter);

		nPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				getActionBar().setSelectedNavigationItem(position);
			}
		});

		// Tabs

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {

			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				nPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {

			}
		};

		actionBar.addTab(actionBar.newTab().setText("This Week")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Popular")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("All Prompts")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Favorites")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Reading List")
				.setTabListener(tabListener));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home, menu);
		MenuItem recordMenuItem = (MenuItem) menu.findItem(R.id.record_menu_item);
		recordMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (isLoggedIn()) {
				Intent recordIntent = new Intent(Home.this, Record.class);
				startActivity(recordIntent);
				} else promptLogin();
				return false;
			}
		});
		
		MenuItem loginMenuItem = (MenuItem) menu.findItem(R.id.login_menu_item);
		loginMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent recordIntent = new Intent(Home.this, Login.class);
				startActivity(recordIntent);
				return false;
			}
		});
		
		MenuItem refreshMenuItem = (MenuItem) menu.findItem(R.id.refresh_menu_item);
		refreshMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				refreshAllData();
				return false;
			}
		});
		
		return true;
	}

	public class NPagerAdapter extends FragmentPagerAdapter {

		public NPagerAdapter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public Fragment getItem(int tabNum) {
			try {
				return tabs[tabNum].newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return new Fragment(); // hack? BAD
		}

		@Override
		public int getCount() {
			return tabs.length;
		}

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.i("NATARRE", key);
	}
	
	private boolean isLoggedIn() {
		SharedPreferences settings =  getSharedPreferences("natarre_prefs", MODE_PRIVATE);
		String user_id = settings.getString("user_id", null);
		String token =settings.getString("token", null);
		if(user_id != null && token != null) {
			return true;
		} else return false;
	}
	
	private void refreshPersonalData() {
		
	}
	
	private void refreshAllData() {
		for (int i = 0; i < nPagerAdapter.getCount(); i++) {
			nPagerAdapter.getItem(i);
		}
			
	}
	
	private void promptLogin() {
		
	}
	
	public interface Refreshable {
		public void refresh();
	}
}