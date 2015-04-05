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
public class CustomAdapterEmail extends ArrayAdapter<Email> {
    LayoutInflater inflater;
    ArrayList<Email> contactArrayList;
    public CustomAdapterEmail(Context context, List<Email> objects) {
        super(context, 0, objects);
        this.contactArrayList = (ArrayList<Email>) objects;
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
        Email email = contactArrayList.get(position);
        holder.ivPerson.setImageResource(R.drawable.person);
        holder.tvPerson.setText(email.getReceivedFrom());
        return convertView;
    }

    private class ViewHolder{
        private ImageView ivPerson;
        private TextView tvPerson;
    }
}
