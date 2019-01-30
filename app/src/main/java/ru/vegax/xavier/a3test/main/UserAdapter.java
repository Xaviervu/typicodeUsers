package ru.vegax.xavier.a3test.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Objects;

import ru.vegax.xavier.a3test.R;
import ru.vegax.xavier.a3test.user_data.User;

public abstract class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    //Member variables
    private LinkedHashMap<Integer, User> mUsers;
    private Context mContext;


    UserAdapter(@NonNull Context context,  @NonNull LinkedHashMap<Integer, User> users) {
        mUsers = users;
        mContext = context;
    }

    @Override
    public int getItemCount() {

        return mUsers.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolderUsers(LayoutInflater.from(mContext).inflate(R.layout.list_item_user, parent, false));


    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderUsers viewHolderUsers = (ViewHolderUsers) holder;
        //Get current item
        User currentItem = getElementByIndex(mUsers,position);

        viewHolderUsers.bindTo(currentItem);


        TextView textView = viewHolderUsers.getTextView();
        //disable swipe for the switch, only click on the item will trigger the output

        if (currentItem != null) {
            textView.setTag(currentItem.getId());
            textView.setOnClickListener(this);
        }
    }
    private User getElementByIndex(LinkedHashMap map, int index){
        return (User) map.get( (Objects.requireNonNull(map.keySet().toArray()))[ index ] );
    }
    class ViewHolderUsers extends RecyclerView.ViewHolder {

        private TextView mTxtVUser;

        TextView getTextView() {
            return mTxtVUser;
        }


        ViewHolderUsers(View itemView) {
            super(itemView);
            //Initialize the views
            mTxtVUser = itemView.findViewById(R.id.txtVUser);
        }

        void bindTo(User currentItem) {
            if (currentItem!= null){
            mTxtVUser.setText(currentItem.getName());
            }
        }
    }
}
