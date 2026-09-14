package com.example.mynotepad;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotepad.Adapter.MyAdapter;
import com.example.mynotepad.bean.Note;

import java.util.List;

public class MyCallBack extends ItemTouchHelper.SimpleCallback {
    private final MyAdapter mAdapter;


    public MyCallBack(int dragDirs, int swipeDirs,  MyAdapter adapter) {
        super(dragDirs, swipeDirs);
        this.mAdapter = adapter;
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getBindingAdapterPosition();
        if (pos != RecyclerView.NO_POSITION) {
            mAdapter.removeItem(pos);

        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

    }
}

