package com.musipo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.musipo.R;
import com.musipo.model.User;
import com.musipo.util.ImageUtils;

import java.util.ArrayList;
import java.util.Calendar;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<User> userArrayList;
    private static String today;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message;
        public ImageView profileImg;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            profileImg = (ImageView) view.findViewById(R.id.profile_image);
        }
    }


    public UserListAdapter(Context mContext, ArrayList<User> userArrayList) {
        this.mContext = mContext;
        this.userArrayList = userArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.name.setText(user.getName());
        holder.message.setText(user.getStatusMsg());

        Bitmap bitImage = ImageUtils.getImageBitmap(mContext, user.getId());

        if(bitImage!=null){
            holder.profileImg.setImageBitmap(bitImage);
        }
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private UserListAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final UserListAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
