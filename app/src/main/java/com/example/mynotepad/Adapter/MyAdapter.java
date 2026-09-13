package com.example.mynotepad.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotepad.R;
import com.example.mynotepad.activity.EditActivity;
import com.example.mynotepad.bean.Note;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {
    private Context mContext;
    private List<Note> mNotes;
    private LayoutInflater mLayoutInflater;
    public final int TYPE_LINEAR = 0;
    public final int TYPE_GRID = 1;
    private int mLayoutType = TYPE_LINEAR;

    public MyAdapter(Context context, List<Note> noteList) {
        this.mContext = context;
        this.mNotes = noteList;
    }

    public void setLayoutType(int typeLinear) {
        mLayoutType = typeLinear;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mLayoutType;
    }

    @NonNull
    @Override

    public MyAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mLayoutInflater = LayoutInflater.from(mContext);    //布局膨胀器初始化需要获得context

        int layoutId;
        if (viewType == TYPE_LINEAR) {
            layoutId = R.layout.item_layout;
        } else {
            layoutId = R.layout.item_grid_layout;
        }

        View view = mLayoutInflater.inflate(layoutId, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.myViewHolder holder, int position) {
        Note note = mNotes.get(position);
        holder.mTitle.setText(note.getTitle());
        holder.mContent.setText(note.getContent());
        holder.mCreateTime.setText(note.getCreateTime());
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("note", note);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotes == null ? 0 : mNotes.size();
    }

    public void refresh(List<Note> notes) {
        this.mNotes = notes;
        notifyDataSetChanged();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mContent;
        TextView mCreateTime;
        ViewGroup mContainer;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTitle = itemView.findViewById(R.id.title);
            this.mContent = itemView.findViewById(R.id.content);
            this.mCreateTime= itemView.findViewById(R.id.create_time);
            this.mContainer= itemView.findViewById(R.id.container);
        }
    }
}
