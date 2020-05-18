package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.jokeandroidlibrary.JokeActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

public class EndpointAsyncTask extends AsyncTask<Context, Void, String> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected String doInBackground(Context... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0];

        try {
            // call the endpoints method here to get the joke
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            // Here we log the exception and return null
            //
            // Returning null is useful to avoid a false positive when testing the AsyncTask,
            // since we check for a non-null and non-empty string to validate the joke
            // This way, if the app is unable to connect to the backend, we return null
            // and so causing the test to fail in this case.
            //
            // Otherwise we would validate an error msg, which is also a non-empty string
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Once got the joke from the backend, pass it to the JokeActivity
        // We can improve this by using an interface to return the result,
        // and place the Intent code in MainActivity, so we apply
        // separation of concerns and so allowing different implementations for the AsyncTask
        Intent intent = new Intent(context, JokeActivity.class);
        intent.putExtra(JokeActivity.KEY_JOKE, result);
        context.startActivity(intent);
    }
}
