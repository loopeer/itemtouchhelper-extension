package com.loopeer.test.itemtouchhelperextension;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MainRecyclerAdapter mAdapter;
    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.updateData(createTestDatas());
        mCallback = new ItemTouchHelperCallback();
        mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private List<Integer> createTestDatas() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            result.add(i);
        }
        return result;
    }
}
