package joviananthony.knack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class ScrollingActivity extends AppCompatActivity {
    String message;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    String text;

    ArrayList<String> arrayX = new ArrayList<>();
    boolean clicked=true;

    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle bundle = getIntent().getExtras();
        message=bundle.getString("Exams");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScrollingActivity.this, MainActivity.class);


                startActivity(i);
            }
        });


        MobileAds.initialize(this, "ca-app-pub-2092728373538093/4801989485");
        AdView adView= findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        adView.loadAd(adRequest);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef =mFirebaseDatabase.getReference(message);




        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("FDB","Listener called");
                showData(dataSnapshot,message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void showData(DataSnapshot dataSnapshot, String msg) {


        final String message = dataSnapshot.getValue(Exams.class).getDescription();
        final String message2 = dataSnapshot.getValue(Exams.class).getLink();
        final String message3 = dataSnapshot.getValue(Exams.class).getName();

        Exams sInfo = dataSnapshot.getValue(Exams.class);
        if (dataSnapshot.getValue(Exams.class).getStd().contains(msg)) {

            if (sInfo != null) {
                sInfo.setName(dataSnapshot.getValue(Exams.class).getName());

            }

        }




        TextView tv=findViewById(R.id.textViewScroll);
        tv.setText(message);

        TextView tv2=findViewById(R.id.textViewScroll1);
        tv2.setText(message3);

        TextView tv1=findViewById(R.id.textViewScroll2);
        tv1.setClickable(true);
        tv1.setMovementMethod(LinkMovementMethod.getInstance());
        tv1.setText(Html.fromHtml(message2));
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ScrollingActivity.this,Web.class);
                i.putExtra("WEBSITE",message2);
                startActivity(i);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {







                if (clicked) {
                    //fab.setBackgroundColor(RED);
                    Snackbar.make(view, "Saved to favourites!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                        saveToFile();

                }




            }
        });
    }

    public void readFromFile(){
        FileInputStream fis=null;
        try {
            fis = openFileInput("favourite.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();


            while((text = br.readLine())!=null){
                sb.append(text);
                arrayX.add(text);
                Log.e("FDB","++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" +
                        ""+arrayX);

            }



            text=sb.toString();
        } catch(IOException e){

            e.printStackTrace();
        }finally {
            if(fis!=null){
                try{
                    fis.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }


    }

    public void saveToFile() {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("favourite.txt", MODE_APPEND));
            outputStreamWriter.write(message+"\n");
            outputStreamWriter.close();
        } catch(FileNotFoundException e){

            try {
                File file=new File("favourite.txt");
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("favourite.txt", MODE_APPEND));
                outputStreamWriter.write(message+"\n");
                outputStreamWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}

