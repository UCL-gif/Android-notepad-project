package com.example.mynotepad.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mynotepad.NoteDBOpenHelper;
import com.example.mynotepad.R;
import com.example.mynotepad.Utils.TimeUtils;
import com.example.mynotepad.bean.Note;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = "AddActivity";
    private EditText mEtTitle;
    private EditText mEtContent;
    private Button mBtnSave;
    NoteDBOpenHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        mHelper = new NoteDBOpenHelper(this);
    }

    private void initEvent() {
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOneNote();
            }
        });
    }

    private void addOneNote() {
        Log.d(TAG, "添加一条记事");
        String currentTime = TimeUtils.getCurrentTime();
        //获得用户输入的数据
        String title = mEtTitle.getText().toString();
        String content = mEtContent.getText().toString();

        //判断标题是否为空和是否为空字符串
        if (TextUtils.isEmpty(title.trim())) {
            mEtTitle.setError("标题不能为空或全是空格！");
            mEtTitle.requestFocus();
            return;
        }
        //来一个Note对象，用于保存
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setCreateTime(currentTime);

        long rowId = mHelper.insertData(note);
        if (rowId > 0) {
            Toast.makeText(this, "成功添加一条记事！", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "添加失败！", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        mEtTitle = findViewById(R.id.add_title);
        mEtContent = findViewById(R.id.add_content);
        mBtnSave = findViewById(R.id.btn_save);
    }
}