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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a class for having a custom adapter to display the requests a user has along with Accept/Decline buttons
 * this class is used in conjunction with the following files activity_view_requests.xml, requests_content.xml, requests_row_content.xml, and RequestsActivity
 * code was adapted from: https://stackoverflow.com/questions/17525886/listview-with-add-and-delete-buttons-in-each-row-in-android
 *
 */
public class RequestsCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    FirebaseFirestore db;


    /**
     * Requests Custom Adapter's constructor
     * @param list list of strings, the data for the adapter
     * @param context the context for this adapter
     */
    public RequestsCustomAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    /**
     * returns the size of the list/number of elements in the list
     * @return  list's size (int)
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     *
     * @param pos, the particular position in the list, can be used to index the list at this spot
     * @return position (int)
     * returns the position in the list as an integer
     */
    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    /**
     *
     * @param position
     * @return 0 (int)
     * returns 0 as the list items in our situation don't have a particular id
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     *
     * @param position position in list (int)
     * @param convertView view object
     * @param parent view object
     * @return View that allows the view, the inflated row (which contains one username and accept/decline buttons)
     */
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
        db = FirebaseFirestore.getInstance();

        CollectionReference collUser = db.collection(mainUser.getName());
        /**
         * @param new View.OnClickListener()
         * on click listener for the decline button that deletes the request from the user's requests
         */
        declineBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String usernameOfNotFriend = list.get(position);
                System.out.println(usernameOfNotFriend);
                collUser.document("friends").collection("requests").document(usernameOfNotFriend).delete();
                mainUser.getRequests().remove(usernameOfNotFriend);
                System.out.println(mainUser.getRequests());
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        /**
         * @param new View.OnClickListener()
         * on click listener for the accept button that adds the accepted user to the current user's friends list
         */
        approveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String usernameOfNewFriend = list.get(position);
                HashMap<String, String> data = new HashMap<>();
                data.put("username", usernameOfNewFriend);
                data.put("name", usernameOfNewFriend);
                collUser.document("friends").collection("requests").document(usernameOfNewFriend).delete();
                collUser.document("friends").collection("current").document(usernameOfNewFriend).set(data);
                mainUser.getRequests().remove(usernameOfNewFriend);
                mainUser.getFriends().add(usernameOfNewFriend);
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return view;

    }
}

