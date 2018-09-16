package com.musipo.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.musipo.model.PlayingStatus;
import com.musipo.model.User;
import com.musipo.ui.activity.MainActivity;
import com.musipo.ui.activity.PlayerActivity;
import com.musipo.ui.activity.UserContactListActivity;
import com.musipo.util.ImageUtils;
import com.musipo.util.UiUtils;

import java.util.ArrayList;
import java.util.Calendar;


public class StatusListAdapter extends RecyclerView.Adapter<StatusListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<PlayingStatus> dataArrayList;
    private static String today;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message;
        public ImageView profileImg, playImag;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            profileImg = (ImageView) view.findViewById(R.id.profile_image);
            playImag = (ImageView) view.findViewById(R.id.playSongImg);
        }
    }


    public StatusListAdapter(Context mContext, ArrayList<PlayingStatus> dataArrayList) {
        this.mContext = mContext;
        this.dataArrayList = dataArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PlayingStatus playingStatus = dataArrayList.get(position);

        User user = playingStatus.getUser();

        if (user != null) {
            holder.name.setText(user.getName());
            holder.message.setText(playingStatus.getStatus());
            Bitmap bitImage = ImageUtils.getImageBitmap(mContext, user.getId());

            if (bitImage != null) {
                holder.profileImg.setImageBitmap(bitImage);
            }
        }

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.showMessage(v.getContext(), "himanshu........");
            }
        });

        holder.playImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // UiUtils.showMessage(v.getContext(), ".." + playingStatus.toString());
                openPlayer();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private StatusListAdapter.ClickListener clickListener;


        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final StatusListAdapter.ClickListener clickListener) {
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

            this.clickListener = new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    UiUtils.showMessage(view.getContext(), "himanshu");
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            };
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

    private final void openPlayer() {
        Intent intent = new Intent(this.mContext, PlayerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.mContext.startActivity(intent);
    }

}
