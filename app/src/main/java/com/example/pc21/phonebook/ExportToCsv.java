package com.example.pc21.phonebook;

import android.app.Activity;
import android.database.Cursor;
import android.os.Environment;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class ExportToCsv extends Activity {
    EditText etName;
    Button btnExport;
    String name;
    ContactDBHandler dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_to_csv);
        init();
    }

    private void init() {
        dbAdapter = new ContactDBHandler(this);
        etName = (EditText) findViewById(R.id.etName);
        btnExport = (Button) findViewById(R.id.btnExport);


        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = String.valueOf(etName.getText());
                Log.d("================", "=================File Name: " + name);
                Boolean flag = backupDatabaseCSV(name);
                if (flag){
                    showMessage("Successfully Exported");
                }else{
                    showMessage("Couldn't Export! Try again later");
                }
            }
        });
    }

    private void showMessage(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    private Boolean backupDatabaseCSV(String outFileName) {
        Log.d("================", "=================File Name: " + outFileName);
        Boolean returnCode = false;
        int i = 0;
        String csvHeader = "";
        String csvValues = "";
        for (i = 0; i < ContactDBHandler.COLUMNS.length; i++) {
            if (csvHeader.length() > 0) {
                csvHeader += ",";
            }
            csvHeader += "\"" + ContactDBHandler.COLUMNS[i] + "\"";
        }

        csvHeader += "\n";
        Log.d("================", "header=" + csvHeader);
try {
        String extr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/" + outFileName + ".csv").toString();
        Log.d("================", "External: " +extr);
        File outFile = new File(extr);
        if (!outFile.exists()){
            outFile.createNewFile();
        }
    //outFile.getParentFile().mkdirs();
        FileWriter fileWriter = new FileWriter(outFile);
        BufferedWriter out = new BufferedWriter(fileWriter);
        Cursor cursor = dbAdapter.getAllCursorContact();
        if (cursor != null) {
            out.write(csvHeader);
            while (cursor.moveToNext()) {
                csvValues = Long.toString(cursor.getLong(0)) + ",";
                csvValues += cursor.getString(1)
                        + ",";
                csvValues += cursor.getString(2)
                        + ",";
                csvValues += cursor.getString(3)
                        + ",";
                csvValues += cursor.getString(4)
                        + "\n";
                out.write(csvValues);
            }
            cursor.close();
        }
        out.close();
        returnCode = true;
    } catch (IOException e) {
        returnCode = false;
        Log.d("================", "IOException: " + e.getMessage());
    }
    dbAdapter.close();

        return returnCode;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        name = etName.getText().toString();

        savedInstanceState.putString("export name", name);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        name = savedInstanceState.getString("export name");
        etName.setText(name);
    }
}

