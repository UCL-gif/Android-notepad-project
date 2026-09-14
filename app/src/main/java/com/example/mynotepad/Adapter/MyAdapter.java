package com.example.mynotepad.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotepad.NoteDBOpenHelper;
import com.example.mynotepad.R;
import com.example.mynotepad.Utils.AlertDialogUtils;
import com.example.mynotepad.activity.EditActivity;
import com.example.mynotepad.bean.Note;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {
    private final Context mContext;
    private List<Note> mNotes;
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
    public int getItemCount() {
        return mNotes == null ? 0 : mNotes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mLayoutType;
    }

    @NonNull
    @Override

    public MyAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);    //布局膨胀器初始化需要获得context

        int layoutId;
        if (viewType == TYPE_LINEAR) {
            layoutId = R.layout.item_layout;
        } else {
            layoutId = R.layout.item_grid_layout;
        }

        View view = layoutInflater.inflate(layoutId, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.myViewHolder holder, int position) {
        Note note = mNotes.get(position);
        holder.mTitle.setText(note.getTitle());
        holder.mContent.setText(note.getContent());
        holder.mCreateTime.setText(note.getCreateTime());
        //设置置顶状态
        if (note.getIsTop()) {
            holder.mPinnedIcon.setVisibility(View.VISIBLE);
            int color = ContextCompat.getColor(mContext, R.color.pinned);
            holder.mMaterialCardView.setCardBackgroundColor(color);
        } else {
            holder.mPinnedIcon.setVisibility(View.GONE);
            int color = ContextCompat.getColor(mContext, R.color.normal);
            holder.mMaterialCardView.setCardBackgroundColor(color);
        }
        holder.mMaterialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("note", note);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        holder.mMaterialCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //先读取note是否为置顶状态，再显示文本操作
                String[] items = {"置顶该记事", "删除该记事"};
                items[0] = note.getIsTop() ? "取消置顶" : "置顶该记事";

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("请选择操作");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteDBOpenHelper helper = new NoteDBOpenHelper(mContext);

                        if (which == 0) {
                            //置顶或取消置顶某个记事
                            //如果在顶上
                            if (note.getIsTop()) {
                                note.setIsTop(false);
                                helper.updateTop(note.getId(), 0);
                                mNotes = helper.queryAllFromDb();
                                refresh(mNotes);
                            } else {
                                note.setIsTop(true);
                                helper.updateTop(note.getId(), 1);
                                mNotes = helper.queryAllFromDb();
                                refresh(mNotes);
                            }
                        } else if (which == 1) {
                            //删除

                            AlertDialogUtils.showDeleteAlertDialog(mContext, "确认删除？", null, note, helper, new Runnable() {
                                //删除成功后执行的代码
                                @Override
                                public void run() {
                                    mNotes = helper.queryAllFromDb();
                                    refresh(mNotes);
                                }
                            });
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
        holder.mIsPinned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了置顶按钮", Toast.LENGTH_SHORT).show();
            }
        });
        holder.mTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了待办按钮", Toast.LENGTH_SHORT).show();
            }
        });
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了删除按钮", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refresh(List<Note> notes) {
        this.mNotes = notes;
        notifyDataSetChanged();
    }

    public void removeItem(int positon) {
        NoteDBOpenHelper helper = new NoteDBOpenHelper(mContext);
        Note note = mNotes.get(positon);
        helper.deleteOneNote(note.getId());
        mNotes.remove(positon);
        notifyItemRemoved(positon);
        Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT).show();
    }
    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mContent;
        TextView mCreateTime;
        ImageView mPinnedIcon;
        MaterialCardView mMaterialCardView;
        TextView mIsPinned;
        TextView mTodo;
        TextView mDelete;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTitle = itemView.findViewById(R.id.title);
            this.mContent = itemView.findViewById(R.id.content);
            this.mCreateTime= itemView.findViewById(R.id.create_time);
            this.mMaterialCardView = itemView.findViewById(R.id.foreground_card);
            this.mPinnedIcon = itemView.findViewById(R.id.iv_pinned);
            this.mIsPinned = itemView.findViewById(R.id.fl_tv_pinned);
            this.mTodo = itemView.findViewById(R.id.fl_tv_todo);
            this.mDelete = itemView.findViewById(R.id.fl_tv_delete);
        }
    }
}
