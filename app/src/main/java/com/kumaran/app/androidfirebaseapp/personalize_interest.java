package com.kumaran.app.androidfirebaseapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class personalize_interest extends AppCompatActivity implements AdapterView.OnItemClickListener {

    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalize_interest);

        Toolbar my_toolbar = (Toolbar)findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("");
//        String toolbar_title = getString(R.string.toolbar_title);
        final TextView toolbarText = (TextView) findViewById(R.id.toolbar_title);
        toolbarText.setText("Personalize");
        my_toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
        ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.ExpandableGridview);
        gridView.setAdapter(new ImageAdapter(this));
        gridView.setNumColumns(3);
        gridView.setExpanded(true);
        System.out.println("inside onclick ");
        gridView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("inside onclick ");
        if (parent.getId() ==R.id.ExpandableGridview)
        {
            System.out.println("inside onclick ExpandableGridview");
            switch(position)
            {
                case 0: {
                    System.out.println("position 0");
                    break;
                }
                case 1: {
                    System.out.println("position 1");
                    break;
                }
                default:{
                    System.out.println("position not clicked");
                    break;
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.navigate_next:
                startActivity(new Intent(this,logout.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
