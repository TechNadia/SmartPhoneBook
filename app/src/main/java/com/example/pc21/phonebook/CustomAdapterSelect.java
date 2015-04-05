package com.example.pc21.phonebook;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Nadia Akter on 3/31/2015.
 */
public class CustomAdapterSelect extends ArrayAdapter<Contact> {

    LayoutInflater inflater;
    ArrayList<Contact> contactArrayList;
    public static Map<Integer, Integer> idList = new ConcurrentHashMap<>();
    int id;
    String number;
    public static SparseBooleanArray itemChecked = new SparseBooleanArray();
    public static int pos;

    public CustomAdapterSelect(Context context, List<Contact> objects) {
        super(context, 0, objects);
        this.contactArrayList = (ArrayList<Contact>) objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null){
            convertView = inflater.inflate(R.layout.custom_view_select, null);
            holder.ivPerson = (ImageView) convertView.findViewById(R.id.ivPerson);
            holder.tvPerson = (TextView) convertView.findViewById(R.id.tvPerson);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Contact contact = contactArrayList.get(position);
        holder.ivPerson.setImageResource(R.drawable.person);
        holder.tvPerson.setText(contact.getName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckBox box = (CheckBox) buttonView.findViewById(R.id.checkbox);
                id = contact.getId();
                if (box.isChecked()){
                    itemChecked.delete(position);
                    itemChecked.put(position, true);
                    pos = position;
                    idList.put(position, id);
                    Log.v("===================================Checked: ======================================", "position: " + position);
                    Log.v("===================================Checked: ======================================", "id: " + id );
                }
                if (!box.isChecked()){
                    if (itemChecked.get(position)){
                        itemChecked.delete(position);
                        itemChecked.put(position, false);
                        idList.remove(position);
                    }
                    Log.v("=================================== Not Checked: ======================================", "position: " + position );
                    Log.v("===================================Not Checked: ======================================", "id: " + id );
                }
            }
        });
        boolean bool = itemChecked.get(position);
        holder.checkBox.setChecked(bool);
        return convertView;
    }

    private class ViewHolder{
        private ImageView ivPerson;
        private TextView tvPerson;
        private CheckBox checkBox;
    }

}
