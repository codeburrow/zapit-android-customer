package com.example.android.fintech_hackathon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // List View
    private ListView mListView;
    // ArrayAdapter
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get ListView from xml
        mListView = (ListView) findViewById(R.id.list_view);


        /**
         *  Create an ArrayAdapter<String>
         *
         *  1. context
         *  2. layout for the list item
         *  3. id of the text view
         *  4. array list - data to be used
         */
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                createDummyDataArray(15));

        // Set mAdapter to mListView
        mListView.setAdapter(mAdapter);
    }

    /**
     *
     * @param numberOfRows to be generated
     * @return ArrayList<String> of dummy data
     */
    private ArrayList<String> createDummyDataArray(int numberOfRows) {
        ArrayList<String> dummyDataArray = new ArrayList<>();

        for (int i = 0; i <= numberOfRows; i ++){
            dummyDataArray.add("CodeBurrow " + i);
            Log.e("CodeBurrow", "" + i);
        }

        return dummyDataArray;
    }



}
