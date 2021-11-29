package com.example.habitup;

import static com.example.habitup.HabitActivity.mainUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RequestsCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;



    public RequestsCustomAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.requests_row_content, null);
        }

        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        Button declineBtn = (Button)view.findViewById(R.id.decline_btn);
        Button approveBtn = (Button)view.findViewById(R.id.approve_btn);

        declineBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String usernameOfNotFriend = list.get(position);
                list.remove(position);
                notifyDataSetChanged();
                ArrayList<String> requests = mainUser.getRequests();
                requests.remove(usernameOfNotFriend);
                notifyDataSetChanged();
            }
        });
        approveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String usernameOfNewFriend = list.get(position);
                mainUser.addFriend(usernameOfNewFriend);
                ArrayList<String> requests = mainUser.getRequests();
                requests.remove(usernameOfNewFriend);
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return view;

    }
}

