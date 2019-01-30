package ru.vegax.xavier.a3test.albums;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.vegax.xavier.a3test.R;
import ru.vegax.xavier.a3test.user_data.UserAlbum;


public abstract class AlbumsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private List<UserAlbum> mUserAlbums;
    private Context mContext;


    AlbumsAdapter(@NonNull Context context, @NonNull List<UserAlbum> userAlbums) {
        mUserAlbums = userAlbums;
        mContext = context;
    }

    @Override
    public int getItemCount() {

        return mUserAlbums.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolderUserAlbums(LayoutInflater.from(mContext).inflate(R.layout.list_item_user, parent, false));


    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderUserAlbums viewHolderUsers = (ViewHolderUserAlbums) holder;
        //Get current item
        UserAlbum currentItem = mUserAlbums.get(position);

        viewHolderUsers.bindTo(currentItem);


        TextView textView = viewHolderUsers.getTextView();
        //disable swipe for the switch, only click on the item will trigger the output

        if (currentItem != null) {
            textView.setTag(currentItem.getId()); // album Id
            textView.setOnClickListener(this);
        }
    }

    public void setAlbumList(@NonNull ArrayList<UserAlbum> userAlbums){
        mUserAlbums = userAlbums;
    }

    class ViewHolderUserAlbums extends RecyclerView.ViewHolder {

        private TextView mTxtVUser;

        TextView getTextView() {
            return mTxtVUser;
        }


        ViewHolderUserAlbums(View itemView) {
            super(itemView);
            //Initialize the views
            mTxtVUser = itemView.findViewById(R.id.txtVUser);
        }

        void bindTo(UserAlbum currentItem) {
            if (currentItem!= null){
            mTxtVUser.setText(currentItem.getTitle());
            }
        }
    }
}
