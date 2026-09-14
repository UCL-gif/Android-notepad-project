package com.example.mynotepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mynotepad.Utils.TimeUtils;
import com.example.mynotepad.bean.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDBOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDatabaseForNote.db";
    private static final int DB_VERSION = 2;
    private static final String TABLE_NAME = "myNote";
    private static final String CREATE_TABLE_FOR_NOTE = "create table " + TABLE_NAME + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "title TEXT," +
            "content TEXT," +
            "create_time TEXT," +
            "is_top INTEGER DEFAULT 0" +
            ")";





    public NoteDBOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FOR_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table " + TABLE_NAME + " add column is_top integer default 0");

    }

    /**
     * 此方法用于从数据库的myNote表中查询所有数据
     * @return  返回装有Note对象的 List列表
     */
    public List<Note> queryAllFromDb() {
        SQLiteDatabase db = getWritableDatabase();
        List<Note> noteList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "is_top DESC, create_time DESC");
        while (cursor.moveToNext()) {
            //定义变量来存放cursor找到的数据
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
            String createTime = cursor.getString(cursor.getColumnIndexOrThrow("create_time"));
            int isTop = cursor.getInt(cursor.getColumnIndexOrThrow("is_top"));

            //设置好一个Note对象的数据
            Note note = new Note();
            note.setId(id);
            note.setTitle(title);
            note.setContent(content);
            note.setCreateTime(createTime);
            note.setIsTop(isTop == 1);
            //放到List列表中
            noteList.add(note);

        }
        cursor.close();
        return noteList;
    }

    /**
     * 此方法用于插入一条数据
     * @param note  是包含了数据项的一个note对象
     * @return      返回插入结果
     */
    public long insertData(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("create_time", note.getCreateTime());

        return db.insert(TABLE_NAME, null, values);
    }

    public long deleteOneNote(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ? ", new String[]{String.valueOf(id)});
    }

    public int updateData(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("create_time", TimeUtils.getCurrentTime());
        return db.update(TABLE_NAME, values,
                "id = ?",
                new String[]{String.valueOf(note.getId())});
    }

    /**
     * 该方法用来设置某条记事是否置顶
     * @param id    就是当前记事的id
     * @param isTop 0为取消置顶，1为置顶
     * @return      返回执行结果
     */
    public void updateTop(int id, int isTop) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_top", isTop);
        db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Note> queryByTitle(String keyword) {
        SQLiteDatabase db = getWritableDatabase();
        List<Note> notes = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME,
                null,
                "title like ?",
                new String[]{"%" + keyword + "%"},
                null,
                null,
                "is_top DESC, create_time DESC");

        while (cursor.moveToNext()) {
            //定义变量来存放cursor找到的数据
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
            String createTime = cursor.getString(cursor.getColumnIndexOrThrow("create_time"));
            int isTop = cursor.getInt(cursor.getColumnIndexOrThrow("is_top"));

            //设置好一个Note对象的数据
            Note note = new Note();
            note.setId(id);
            note.setTitle(title);
            note.setContent(content);
            note.setCreateTime(createTime);
            note.setIsTop(isTop == 1);
            //放到List列表中
            notes.add(note);
        }
        cursor.close();
        return notes;
    }
}
