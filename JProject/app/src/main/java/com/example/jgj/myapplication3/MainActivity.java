package com.example.jgj.myapplication3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity { //
    EditText ed;
    Button bt;
    ListView parent_listview;
    private ArrayList<TestModel> testModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 100; i++) {
            TestModel t = new TestModel();
            t.tv = i+"===========================";
            testModels.add(t);
        }
        setContentView(R.layout.activity_main);
        ed = (EditText) findViewById(R.id.ed);
        bt = (Button) findViewById(R.id.bt);
        parent_listview = (ListView) findViewById(R.id.parent_listview);
        parent_listview.setAdapter(new ParentAdpter(this,parent_listview,testModels));

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("ed".equals(ed.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, "你SB呀", Toast.LENGTH_SHORT).show();
                    return;
                }
                float f = px2dip(MainActivity.this, Float.parseFloat(ed.getText().toString()));
                Toast.makeText(MainActivity.this, "" + f + "dp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public  int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
