package com.example.jokeandroidlibrary;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    //Declares a constant for the Intent key used to send/receive the joke.
    // We can declare it as public, and thus reuse it when we are launching this Activity from the app and passing the joke via Intent there.
    public static final String KEY_JOKE = "joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        TextView jokeTextView = findViewById(R.id.joke);
        Intent intent = getIntent();
        // check if we are getting the joke via Intent.
        if (intent != null && intent.hasExtra(KEY_JOKE)) {
            //if so, we display the joke on the screen using the TextView
            jokeTextView.setText(intent.getStringExtra(KEY_JOKE));
        } else {
            // otherwise we can display an error message
            jokeTextView.setText(R.string.error_getting_joke);
        }
    }
}
