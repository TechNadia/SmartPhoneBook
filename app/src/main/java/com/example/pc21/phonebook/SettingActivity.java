package com.example.pc21.phonebook;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class SettingActivity extends Activity implements View.OnClickListener{
    Button btnWhite, btnBlack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        btnBlack = (Button) findViewById(R.id.btnBlack);
        btnWhite = (Button) findViewById(R.id.btnWhite);

        btnBlack.setOnClickListener(this);
        btnWhite.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBlack:
                ThemeUtils.changeToTheme(this, ThemeUtils.BLACK);
                break;
            case R.id.btnWhite:
                ThemeUtils.changeToTheme(this, ThemeUtils.BLUE);
                break;
        }
    }
}
