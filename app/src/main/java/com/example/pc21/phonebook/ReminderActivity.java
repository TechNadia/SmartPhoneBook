package com.example.pc21.phonebook;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class ReminderActivity extends ActionBarActivity{


    EditText etToNumber,etMessage;
    int day,month,year;
    int hour,minute,m24hour;
    Calendar cal,currentCal,reminderCal;






    Button btnSave,btnReminder,btnSetDate,btnSetTime;
    TextView tvSetDate,tvSetTime;

    //tuli
    int remDay,remMonth,remYear,remHour,remMin;
    int curDay,curMonth,curYear,curHour,curMin;
    TextView tvDate,tvTime,tvTimeDif;

    //tuli


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        cal = Calendar.getInstance();
        remDay = cal.get(Calendar.DAY_OF_MONTH);
        remMonth = cal.get(Calendar.MONTH);
        remYear = cal.get(Calendar.YEAR);

        remHour = cal.get(Calendar.HOUR_OF_DAY);
        remMin = cal.get(Calendar.MINUTE);


        //Tuli
        btnReminder= (Button) this.findViewById(R.id.btnReminder);
        btnSave= (Button) this.findViewById(R.id.btnSave);
        etToNumber= (EditText) this.findViewById(R.id.etToNumber);
        etMessage= (EditText) this.findViewById(R.id.etMessage);
        tvSetDate= (TextView) this.findViewById(R.id.tvSetDate);
        btnSetDate= (Button) this.findViewById(R.id.btnSetDate);
        btnSetTime= (Button) this.findViewById(R.id.btnSetTime);
        tvSetTime= (TextView) this.findViewById(R.id.tvSetTime);

        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSetDate.setVisibility(View.VISIBLE);
                tvSetDate.setVisibility(View.VISIBLE);
                btnSetTime.setVisibility(View.VISIBLE);
                tvSetTime.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.VISIBLE);
                etToNumber.setVisibility(View.VISIBLE);
                etMessage.setVisibility(View.VISIBLE);



            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(),"Reminder Saved",Toast.LENGTH_LONG).show();


                String toNumber=etToNumber.getText().toString();
                String message=etMessage.getText().toString();


                etToNumber.setVisibility(View.INVISIBLE);
                etMessage.setVisibility(View.INVISIBLE);
                tvSetDate.setVisibility(View.INVISIBLE);
                tvSetTime.setVisibility(View.INVISIBLE);
                btnSetDate.setVisibility(View.INVISIBLE);
                btnSetTime.setVisibility(View.INVISIBLE);
                btnSave.setVisibility(View.INVISIBLE);


                Intent intent=new Intent(ReminderActivity.this,MessageActivity.class);
                intent.putExtra("toNumber",toNumber);
                intent.putExtra("message",message);


                etMessage.setText(null);
                etToNumber.setText(null);


                reminderCal = Calendar.getInstance();
                reminderCal.set(remYear, remMonth, remDay, remHour, remMin, 0);

                currentCal = Calendar.getInstance();

                curDay = currentCal.get(Calendar.DAY_OF_MONTH);
                curMonth = currentCal.get(Calendar.MONTH);
                curYear = currentCal.get(Calendar.YEAR);
                curHour = currentCal.get(Calendar.HOUR_OF_DAY);
                curMin = currentCal.get(Calendar.MINUTE);

                tvSetDate.setText(String.valueOf(curDay) + "/" +
                        String.valueOf(curMonth + 1) + "/" +
                        String.valueOf(curYear));
                if (curHour > 12)
                    tvSetTime.setText("Time: " + String.valueOf(curHour - 12) + ":" +
                            String.valueOf(curMin) + "PM");
                else
                    tvSetTime.setText("Time: " + String.valueOf(curHour) + ":" +
                            String.valueOf(curMin) + "Am");

                long dif = reminderCal.getTimeInMillis() - currentCal.getTimeInMillis()+20000;
                //tvTimeDif.setText(String.valueOf(dif));
                /*cal= Calendar.getInstance();
                cal.set(year,month,day,hour,minute);
                String h=String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + String.valueOf(cal.get(Calendar.MINUTE));
                tvSetDate.setText(h);
*/






                PendingIntent pendingIntent=PendingIntent.getActivity(ReminderActivity.this,0,intent,0);
                AlarmManager manager= (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                manager.set(AlarmManager.RTC,System.currentTimeMillis()+dif,pendingIntent);



            }
        });

        //Date Set

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatePicker dp=new MyDatePicker();
                dp.show(getSupportFragmentManager(),"datePicker");
            }
        });

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTimePicker tp=new MyTimePicker();
                tp.show(getSupportFragmentManager(),"timePicker");
            }
        });
    }



    @SuppressLint("ValidFragment")
    public class MyDatePicker extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {


            DatePickerDialog.OnDateSetListener listener=
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int Myyear, int monthOfYear, int dayOfMonth) {

                            remDay=dayOfMonth;
                            remMonth = monthOfYear;
                            remYear=Myyear;
                            tvSetDate.setText(String.valueOf(dayOfMonth)+"/"+
                                    String.valueOf(monthOfYear+1)+"/"+
                                    String.valueOf(Myyear));
                        }
                    };

            return new DatePickerDialog(ReminderActivity.this,listener,
                    remYear, remMonth, remDay);
        }

    }

    //Time SET

    @SuppressLint("ValidFragment")
    public class MyTimePicker extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            TimePickerDialog.OnTimeSetListener listener=
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            remHour=hourOfDay;
                            remMin=minute;
                            if(hourOfDay>12)
                                tvSetTime.setText(String.valueOf(hourOfDay-12)+":"+
                                        String.valueOf(minute)+"PM");
                            else
                                tvSetTime.setText(String.valueOf(hourOfDay)+":"+
                                        String.valueOf(minute)+"Am");
                        }
                    };

            return new TimePickerDialog(ReminderActivity.this,listener, remHour, remMin, true);
        }
    }
//
//
//    public void reminder(View view)
//    {
//        btnSave.setVisibility(View.VISIBLE);
//        etToNumber.setVisibility(View.VISIBLE);
//        etMessage.setVisibility(View.VISIBLE);
//
//
//    }

//    public void save(View view){
//        String toNumber=etToNumber.getText().toString();
//        String message=etMessage.getText().toString();
//
//        etToNumber.setVisibility(View.INVISIBLE);
//        etMessage.setVisibility(View.INVISIBLE);
//        btnSave.setVisibility(View.INVISIBLE);
//
//
//        Intent intent=new Intent(this,MessageActivity.class);
//        intent.putExtra("toNumber",toNumber);
//        intent.putExtra("message",message);
//
//        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
//        AlarmManager manager= (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
//        manager.set(AlarmManager.RTC,System.currentTimeMillis()+5000,pendingIntent);
//
//    }
//
//

}

