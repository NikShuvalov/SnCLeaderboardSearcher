package com.example.nikita.sncleaderboardsearcher;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Figure out how to keep app always running in background to keep track of time and issue
notifications.
1. Check for average points needed to reach goal based on previous weeks standings.
2. Allow user to set goal. a. Initially just have them set a point goal.
                           b. A more advanced way of doing it is by setting a prize goal.
3. Allow player to pick their schedule for when they'd be reminded to play.
4. Remind player to play with different prompts. If falling behind goal notify them, otherwise
remind them of schedule but let them know they are ahead of schedule.
Optional additions: Quiz player during session to see if they are tilting and should take a
break.
 */

public class MainActivity extends AppCompatActivity {
    String username;
    ArrayList<String> entries= new ArrayList<>();
    TextView resultView;
    EditText editText;
    DownloadTask downloadTask;
    String result;
    Boolean searching=false;
    String selfUser;
    Button selfButton;
    String relevantHTML = "";
    Boolean found;
    ArrayList<String> tempArray = new ArrayList<>();

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            try {
                url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String s = "";
                while ((s=bufferedReader.readLine())!=null){
                    result+=s;
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void searchUser(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (!searching) {
            searching=true;
            found=false;
            Button b = (Button)view;
            int id= b.getId();
            username = editText.getText().toString();
            if (id== selfButton.getId()){
                final SharedPreferences sharedPreference = this.getSharedPreferences
                        ("com.example.nikita.sncleaderboardsearcher",MODE_PRIVATE);
                selfUser= sharedPreference.getString("selfUser", "");
                if (selfUser.equals("")){
                    sharedPreference.edit().putString("selfUser",editText.getText().toString()).apply();
                    selfButton.setText("Search for self");
                }else{
                    username=selfUser;
                }
            }
            Pattern p = Pattern.compile(">(.*?)</td>");

            for (int i = 0; i < entries.size(); i++) {
                if (i != 0) {
                    String t = entries.get(i);
                    tempArray.clear();
                    Matcher m = p.matcher(t);
                    while (m.find()) {
                        tempArray.add(m.group(1));
                    }
                    String user1 = tempArray.get(1).toUpperCase();
                    String user2 = username.toUpperCase();
                    if (user1.equals(user2)) {
                        resultView.setText("Position:"+tempArray.get(0)+
                                "\nName:"+tempArray.get(1)+
                                "\nPoints:"+tempArray.get(2)+
                                "\nCurrent Cash:"+tempArray.get(3)+
                                "\nProjected Cash:"+tempArray.get(4));
                        searching=false;
                        found= true;
                        break;
                    }
                }
            }
            searching=false;
            if (!found){
                Toast.makeText(getApplicationContext(),"No match in top 200",Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getApplicationContext(),"Still searching",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        downloadTask = new DownloadTask();

        resultView=(TextView) findViewById(R.id.resultView);
        editText =(EditText) findViewById(R.id.editText);
        selfButton= (Button)findViewById(R.id.selfButton);


        try {
            result = downloadTask.execute("http://sitncrush.wpnetwork.eu/Race/SnC?results=200&site=0").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Pattern p = Pattern.compile("class=\"leaderboard_table\">(.*?)</table>");
        Matcher m = p.matcher(result);
        while (m.find()) {
            relevantHTML = m.group(1);
        }

        p = Pattern.compile("<tr>(.*?)</tr>");
        m = p.matcher(relevantHTML);
        while (m.find()) {
            entries.add(m.group(1));
        }

        final SharedPreferences sharedPreference = this.getSharedPreferences
                ("com.example.nikita.sncleaderboardsearcher",MODE_PRIVATE);
        selfUser= sharedPreference.getString("selfUser", "");
        if (selfUser.equals("")){
            selfButton.setText("Set Self Username");
        }else{
            selfButton.setText("Search for self");
        }
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
            final SharedPreferences sharedPreference = this.getSharedPreferences
                    ("com.example.nikita.sncleaderboardsearcher",MODE_PRIVATE);
            selfUser="";
            sharedPreference.edit().remove("selfUser").commit();
            selfButton.setText("Set Self Username");
        }

        return super.onOptionsItemSelected(item);
    }
}
