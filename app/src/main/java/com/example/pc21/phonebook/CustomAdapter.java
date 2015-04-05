package com.example.pc21.phonebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nadia Akter on 3/31/2015.
 */
public class CustomAdapter extends ArrayAdapter<Contact> {
    LayoutInflater inflater;
    ArrayList<Contact> contactArrayList;
    public CustomAdapter(Context context, List<Contact> objects) {
        super(context, 0, objects);
        this.contactArrayList = (ArrayList<Contact>) objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null){
            convertView = inflater.inflate(R.layout.custom_view, null);
            holder.ivPerson = (ImageView) convertView.findViewById(R.id.ivPerson);
            holder.tvPerson = (TextView) convertView.findViewById(R.id.tvPerson);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contact contact = contactArrayList.get(position);
        holder.ivPerson.setImageResource(R.drawable.person);
        holder.tvPerson.setText(contact.getName());
        return convertView;
    }

    private class ViewHolder{
        private ImageView ivPerson;
        private TextView tvPerson;
    }
}
