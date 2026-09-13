package com.example.mynotepad.activity;

import android.content.Intent;
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
import com.example.mynotepad.Utils.AlertDialogUtils;
import com.example.mynotepad.bean.Note;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditActivity";
    private EditText mEtTitle;
    private EditText mEtContent;
    private Button mBtnConfirm;
    private Button mBtnDelete;
    private NoteDBOpenHelper mHelper;
    private Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initData();
        initView();
        initEvent();
    }

    private void initData() {
        mHelper = new NoteDBOpenHelper(EditActivity.this);
    }

    private void initEvent() {
        mBtnConfirm.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
    }

    private void initView() {
        mEtTitle = findViewById(R.id.et_title);
        mEtContent = findViewById(R.id.et_content);
        mBtnConfirm = findViewById(R.id.btn_confirm_edit);
        mBtnDelete = findViewById(R.id.btn_delete);
        //打开收到的包裹
        OpenBundleAndSetText();

    }

    private void OpenBundleAndSetText() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mNote = (Note) bundle.getSerializable("note");
            if (mNote != null) {
                String title = mNote.getTitle();
                String content = mNote.getContent();
                mEtTitle.setText(title);
                mEtContent.setText(content);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_delete) {
            //用AlertDialog对话框来提醒用户是否删除
            AlertDialogUtils.showDeleteAlertDialog(EditActivity.this,
                    "删除记事",
                    "确认删除该记事吗？",
                    mNote,
                    mHelper,
                    new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
        } else {
            editOneNote();
        }
    }

    private void deleteOneNote(Note note) {
        Log.d(TAG, "删除一条记事");
        long result = mHelper.deleteOneNote(note.getId());
        if (result > 0) {
            Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void editOneNote() {
        Log.d(TAG, "编辑一条记事");
        String title = mEtTitle.getText().toString();
        String content = mEtContent.getText().toString();

        //判断标题是否为空和是否为空字符串
        if (TextUtils.isEmpty(title.trim())) {
            mEtTitle.setError("标题不能为空或全是空格！");
            mEtTitle.requestFocus();
            return;
        }

        mNote.setTitle(title);
        mNote.setContent(content);
        long update = mHelper.updateData(mNote);
        if (update > 0) {
            Toast.makeText(this, "编辑成功！", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "编辑失败!", Toast.LENGTH_SHORT).show();
        }
    }
}