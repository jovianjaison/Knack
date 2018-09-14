package joviananthony.knack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Iterator;

public class Favourites extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private ListView mListView;
    String text;
    ArrayList<String> arrayX = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Favourites.this, MainActivity.class);


                startActivity(i);
            }
        });

        Button button=findViewById(R.id.ButtonB);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Cleared!", Toast.LENGTH_LONG).show();
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("favourite.txt", MODE_PRIVATE));
                    outputStreamWriter.write("");
                    outputStreamWriter.close();
                } catch(FileNotFoundException e){

                    try {
                        File file=new File("favourite.txt");
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("favourite.txt", MODE_PRIVATE));
                        outputStreamWriter.write("");
                        outputStreamWriter.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }

                Intent i = new Intent(Favourites.this, Favourites.class);


                startActivity(i);
            }
        });

        readFromFile();





        mListView = findViewById(R.id.listviewF);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef =mFirebaseDatabase.getReference();




        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("FDB","Listener called");

                showData(dataSnapshot,text);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }




    private void showData (DataSnapshot dataSnapshot,String txt) {


        ArrayList<Exams> array = new ArrayList<>();


        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Iterator<String> itr = arrayX.iterator();
            while(itr.hasNext()) {

                Exams sInfo = ds.getValue(Exams.class);
                if (ds.getValue(Exams.class).getName().equals(itr.next())) {


                    Log.d("TAG", "showData: name:" + sInfo.getName());


                    array.add(sInfo);


                }
            }
        }

        final ExamAdapterArr adapter = new ExamAdapterArr(this, array);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Exams obj = adapter.getItem(position);
                String message = obj.getName();
                Intent i = new Intent(Favourites.this, ScrollingActivity.class);

                i.putExtra("Exams", message);
                startActivity(i);

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
}
