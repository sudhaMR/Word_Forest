package com.google.engedu.wordforest2
        ;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import android.content.Context;
import android.widget.Toast;

import com.google.engedu.ghost.FastDictionary;
import com.google.engedu.ghost.GhostDictionary;
import com.google.engedu.ghost.SimpleDictionary;

public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = true;
    private Random random = new Random();
    private SimpleDictionary S;
    private FastDictionary F;
    private String WORD;
    private String STATUS;
    private String MESSAGE;
    private int Comp_Score =0;
    private int User_Score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        final EditText enteredWord = (EditText) findViewById(R.id.userWord);


        Button reset = (Button)findViewById(R.id.res);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             onStart(v);
            }
        });
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
           // S = new SimpleDictionary(inputStream);
            F= new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        Button go = (Button) findViewById(R.id.goButton);
        final TextView status = (TextView) findViewById(R.id.statusText);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = enteredWord.getText().toString().toLowerCase();
                if(F.isWord(word)){
                    status.setText("Good Work!");
                }
                else{
                    status.setText("Try Again!");
                }
                enteredWord.setText("");
            }
        });

    }


    @Override
    public boolean onKeyUp (int keyCode, KeyEvent event) {
        if(userTurn) {
            TextView label = (TextView) findViewById(R.id.gameStatus);
            if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
                Log.i("char", String.valueOf(event.getKeyCode()));
                TextView ghostText = (TextView) findViewById(R.id.ghostText);
                ghostText.append(KeyEvent.keyCodeToString(keyCode).split("_")[1].toLowerCase());
                String word = ghostText.getText().toString().trim().toLowerCase();
                boolean val = S.isWord(word);
                //boolean val = F.isWord(word);
                Log.w("word", word);
                Log.i("isWordkeyup",Boolean.toString(val));
                if (val == true) {
                    TextView status = (TextView) findViewById(R.id.gameStatus);
                    status.setText("COMPUTER WINS");
                }

            }
            userTurn=false;
            callturn();
        }
        return super.onKeyUp(keyCode,event);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement.
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void callturn()
    { if(userTurn==false) {
        computerTurn();
        userTurn = true;
       }
    }
    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        TextView ghostText = (TextView)findViewById(R.id.ghostText);
        String word = ghostText.getText().toString();
        Log.i("tag",word);
        String good_word;
        boolean val=false;
        if(!(word.equals(""))) {
            val = S.isWord(word);
            //val = F.isWord(word);
            Log.i("isWordcompturn",Boolean.toString(val));
        }

        if(word.length()>=4 && val)
            label.setText("COMPUTER WINS");
        else
        {   good_word=S.getAnyWordStartingWith(word);
            //good_word=F.getAnyWordStartingWith(word);
            //good_word=F.getGoodWordStartingWith(word);
            int n= word.length();
            //Log.isLoggable("length",n);
            if(good_word.equals(word))
            {  label.setText("COMPUTER WINS");
                TextView msg = (TextView)findViewById(R.id.msg);
                msg.setText("No word with given prefix found");
            }
            else
            {  Log.d("append",good_word);
                String x= good_word.substring(n, n + 1);
                ghostText.append(x);
                userTurn = true;
                label.setText(USER_TURN);
            }
        }

    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        //userTurn = random.nextBoolean();
        userTurn = true;
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView msg = (TextView)findViewById(R.id.msg);
        msg.setText("");
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
    public void getvalues(){
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView msg = (TextView)findViewById(R.id.msg);
        WORD=text.getText().toString();
        STATUS = label.getText().toString();
        MESSAGE=msg.getText().toString();

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        getvalues();
        String key ="WORD";
        savedInstanceState.putString(key,WORD);
        key="STATUS";
        savedInstanceState.putString(key,STATUS);
        key="MSG";
        savedInstanceState.putString(key,MESSAGE);
        key="turn";
        savedInstanceState.putString(key, Boolean.toString(userTurn));
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
