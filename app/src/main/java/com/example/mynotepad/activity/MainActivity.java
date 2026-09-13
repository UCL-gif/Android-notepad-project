package com.example.mynotepad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotepad.Adapter.MyAdapter;
import com.example.mynotepad.NoteDBOpenHelper;
import com.example.mynotepad.R;
import com.example.mynotepad.Utils.SpfUtils;
import com.example.mynotepad.bean.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<Note> mNotes;
    NoteDBOpenHelper mHelper;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHelper != null && mAdapter != null) {
            if (mSearchView != null && !mSearchView.getQuery().toString().isEmpty()) {
                refreshFromDbByTitle();
            } else {
                refreshFromDb();
            }
        }
    }

    private void refreshFromDbByTitle() {
        mNotes = mHelper.queryByTitle(mSearchView.getQuery().toString());
        mAdapter.refresh(mNotes);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //加载保存的偏好布局
        int layoutId = SpfUtils.getInt(MainActivity.this, "layout_type", 0);
        if (layoutId == mAdapter.TYPE_LINEAR) {
            menu.findItem(R.id.linear_layout).setChecked(true);
        } else {
            menu.findItem(R.id.grid_layout).setChecked(true);
        }
        //搜索控件初始化并设置点击逻辑
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                mNotes = mHelper.queryByTitle(newText);
                mAdapter.refresh(mNotes);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.linear_layout) {
            LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
            mRecyclerView.setLayoutManager(llm);
            mAdapter.setLayoutType(mAdapter.TYPE_LINEAR);
            item.setChecked(true);
            //保存偏好
            SpfUtils.saveInt(MainActivity.this, "layout_type", mAdapter.TYPE_LINEAR);
            return true;
        } else if (itemId == R.id.grid_layout) {
            GridLayoutManager glm = new GridLayoutManager(MainActivity.this, 2);
            mRecyclerView.setLayoutManager(glm);
            mAdapter.setLayoutType(mAdapter.TYPE_GRID);
            item.setChecked(true);
            //保存偏好
            SpfUtils.saveInt(MainActivity.this, "layout_type", mAdapter.TYPE_GRID);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshFromDb() {
        mNotes = mHelper.queryAllFromDb();
        mAdapter.refresh(mNotes);
    }

    private void initData() {
        mNotes = new ArrayList<>();
        mHelper = new NoteDBOpenHelper(this);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycleView);
        mAdapter = new MyAdapter(MainActivity.this, mNotes);
        mRecyclerView.setAdapter(mAdapter);
        //加载布局偏好
        int layoutType = SpfUtils.getInt(MainActivity.this, "layout_type", 0);
        if (layoutType == mAdapter.TYPE_LINEAR) {
            LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
            mRecyclerView.setLayoutManager(llm);
            mAdapter.setLayoutType(layoutType);
        } else {
            GridLayoutManager glm = new GridLayoutManager(MainActivity.this, 2);
            mRecyclerView.setLayoutManager(glm);
            mAdapter.setLayoutType(layoutType);
        }

    }

    /**
     * 这个add方法是点击加号实现的，实现添加记事功能
     * @param view
     */
    public void add(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }
}