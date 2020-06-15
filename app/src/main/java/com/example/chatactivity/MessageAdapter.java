package com.example.chatactivity;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


   public static final int MSG_TYPE_LEFT=0;
   public static final int MSG_TYPE_RIGHT=1;
   FirebaseUser fuser;

    private Context mContext;
    private List<Chats> mUsers;
    private String imageUrl;

    public MessageAdapter(Context mContext, List<Chats> mUsers, String imageUrl){
        this.mContext=mContext;
        this.mUsers=mUsers;
        this.imageUrl=imageUrl;

    }



    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT)
        {
            View view=LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }else
        {
            View view=LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }




    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
         Chats uSers=mUsers.get(position);

         holder.show_msg.setText(uSers.getMessage());
         if(imageUrl.equals("default"))
         {
             holder.image.setImageResource(R.mipmap.ic_launcher);
         }




    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView show_msg;
        ImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_msg=itemView.findViewById(R.id.show_msg);
            image=itemView.findViewById(R.id.image);


        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(fuser.getEmail().equals(mUsers.get(position).getSender()))
        {
            return MSG_TYPE_RIGHT;
        }
        else return MSG_TYPE_LEFT;
    }
}
