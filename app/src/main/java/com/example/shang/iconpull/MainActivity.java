package com.example.shang.iconpull;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends Activity {

    private MyListView listView;
    private ImageView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        listView = (MyListView) findViewById(R.id.list_view);

        View view = View.inflate(MainActivity.this, R.layout.header, null);
        header = (ImageView) view.findViewById(R.id.header);
        listView.addHeaderView(view);

        header.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                listView.setParallaxImage(header);
                header.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
    }
}
