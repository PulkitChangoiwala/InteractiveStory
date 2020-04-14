package com.pulkit.interactivestory.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulkit.interactivestory.R;
import com.pulkit.interactivestory.model.Page;
import com.pulkit.interactivestory.model.Story;

import java.util.Stack;

public class StoryActivity extends AppCompatActivity {
    public static final String  TAG = StoryActivity.class.getSimpleName();

//Adding Objects
    private Story story;
    private ImageView storyImageView;
    private TextView storyTextView;
    private Button choice1Button;
    private Button choice2Button;
    private String name;
    private Stack<Integer> pageStack = new Stack<Integer>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);


        storyImageView = findViewById(R.id.storyImageView);
        storyTextView = findViewById(R.id.storyTextView);
        choice1Button = findViewById(R.id.choice1Button);
        choice2Button = findViewById(R.id.choice2Button);








        Intent intent = getIntent();
        //name is declared as class object because it gets used in loadpage() method
        name = intent.getStringExtra(getString(R.string.key_name));
        if(name == null || name.isEmpty())
        {
            name = "Default";
        }
        Log.d(TAG, name);



        story = new Story();
        loadPage(0);




    }






    private void loadPage(int pageNumber) {
        pageStack.push(pageNumber);
        final Page page = story.getPage(pageNumber);
        Drawable image = ContextCompat.getDrawable(this, page.getImageId());
        storyImageView.setImageDrawable(image);

        String pageText = getString(page.getTextId());
         //Add name if placeholder included. Won't add if not
        pageText = String.format(pageText,name);
        storyTextView.setText(pageText);
        if(page.isFinalPage())
        {
            choice1Button.setVisibility(View.INVISIBLE);
            choice2Button.setText(R.string.play_again);
            pageStack.clear();
            choice2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //finish();
                    // To go to main activity ... as StoryActivity is called from MainActivity finish() will
                    //take back to mainActivity
                    loadPage(0);


                }
            });
        }
        else
        {
            loadButtons(page);

        }}

    private void loadButtons(final Page page) {
        choice1Button.setVisibility(View.VISIBLE);
        choice1Button.setText(page.getChoice1().getTextId());
        choice2Button.setVisibility(View.VISIBLE);
        choice1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                int nextPage = page.getChoice1().getNextPage();
                loadPage(nextPage);

            }
        });


        choice2Button.setText(page.getChoice2().getTextId());
        choice2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                int nextPage = page.getChoice2().getNextPage();
                loadPage(nextPage);

            }
        });
    }


    @Override
    public void onBackPressed() {
        pageStack.pop();
        if(pageStack.isEmpty())
        { super.onBackPressed();}
        else
        {
         loadPage(pageStack.pop());

        }
    }
}
