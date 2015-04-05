package com.example.pc21.phonebook;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity{
    private DrawerLayout drawerLayout;
    private ListView listView;
    ViewPager pager;
    ContactDBHandler contactDBHandler;
    List<Contact> contactList;
    String[] tools;
    ActionBarDrawerToggle drawerToggle;
    Intent intent;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayAdapter adapter;
    private int recent_id, previous_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        tools = getResources().getStringArray(R.array.Tools);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close ){
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        /*if (savedInstanceState == null) {
            selectItem(0);
        }*/

        listView = (ListView) findViewById(R.id.drawerList);
        adapter = new ArrayAdapter(this, R.layout.drawer_list_item, tools);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), tools[position] + " Clicked", Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        Toast.makeText(getApplicationContext(), tools[0] + " Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, AddActivity.class);
                        intent.putExtra("from", "add");
                        startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), tools[1] + " Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, SelectActivity.class);
                        intent.putExtra("from", "select");
                        startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), tools[2] + " Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, SelectActivity.class);
                        intent.putExtra("from", "delete");
                        startActivity(intent);
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), tools[3] + " Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, ReminderActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(), tools[4] + " Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, ExportToCsv.class);
                        startActivity(intent);
                        break;
                    case 5:
                        Toast.makeText(getApplicationContext(), tools[5] + " Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });
        init();

    }



    private void init() {
        contactDBHandler = new ContactDBHandler(this);
        Intent intent = new Intent(MainActivity.this, ListenerService.class);
        startService(intent);

        pager= (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SwipeAdapter(getSupportFragmentManager()));
        pager.setOnPageChangeListener(onPageChangeListener);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().addTab(actionBar.newTab().setIcon(R.drawable.contact).setTabListener(tabListener));
        getSupportActionBar().addTab(actionBar.newTab().setIcon(R.drawable.facebook).setTabListener(tabListener));
        getSupportActionBar().addTab(actionBar.newTab().setIcon(R.drawable.gmail).setTabListener(tabListener));


    }

    private ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            getSupportActionBar().setSelectedNavigationItem(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private android.support.v7.app.ActionBar.TabListener tabListener=new android.support.v7.app.ActionBar.TabListener() {
        @Override
        public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            pager.setCurrentItem(tab.getPosition());
            recent_id = tab.getPosition();
            switch (recent_id){
                case 0:
                    Toast.makeText(getApplicationContext(), "Phone Contact Fragment", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "Gmail Contact Fragment", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }

        @Override
        public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }

        @Override
        public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }
    };

   /* @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, tools[position] + " is clicked",Toast.LENGTH_SHORT).show();
        selectItem(position);
    }*/

    private void selectItem(int position) {
        listView.setItemChecked(position, true);
        setTitle(tools[position]);
        drawerLayout.closeDrawer(listView);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private class SwipeAdapter extends FragmentPagerAdapter {

        public SwipeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment=null;
            switch (i){
                case 0:
                    contactList = contactDBHandler.getAllContact();
                    if (contactList.isEmpty()){
                        fragment=new PhoneContactFragment();
                    }
                    else {
                        fragment=new AppContactFragment();
                    }
                    break;

                case 1:
                    fragment=new GmailContactFragment();
                    break;
                case 2:
                    fragment=new FacebookFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public void Call(View view){
        //Call Clicked
        Toast.makeText(getApplicationContext(), "Calling...", Toast.LENGTH_SHORT).show();
    }
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("MyPage", recent_id);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        previous_id = savedInstanceState.getInt("MyInt");
    }
}
