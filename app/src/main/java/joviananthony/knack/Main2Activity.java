package joviananthony.knack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private ExpandableListView mListView;
    List<String> listDataHeader;
    HashMap<String, ArrayList<Exams>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toast.makeText(getApplicationContext(), "Loading,Please wait...", Toast.LENGTH_LONG ).show();
        Bundle bundle = getIntent().getExtras();
        final String message=bundle.getString("SPINNER");

        mListView = findViewById(R.id.lvExp);




        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef =mFirebaseDatabase.getReference();




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

    private void showData(DataSnapshot dataSnapshot,String msg) {


        ArrayList<Exams> array = new ArrayList<>();
        ArrayList<Exams> array1 = new ArrayList<>();
        ArrayList<Exams> array2 = new ArrayList<>();
        ArrayList<Exams> array3 = new ArrayList<>();



        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Exams sInfo = ds.getValue(Exams.class);
            if(ds.getValue(Exams.class).getStd().contains(msg)) {
                //sInfo.setDescription(ds.getValue(Exams.class).getDescription());
                //sInfo.setLink(ds.getValue(Exams.class).getLink());
                sInfo.setName(ds.getValue(Exams.class).getName());

                //Log.d("TAG", "showData: description:" + sInfo.getDescription());
                //Log.d("TAG", "showData: link:" + sInfo.getLink());
                Log.d("TAG", "showData: name:" + sInfo.getName());


                //array.add(sInfo.getDescription());
                //array.add(sInfo.getLink());
                if(ds.getValue(Exams.class).getTags().contains("academic")) {
                    array.add(sInfo);
                }else if(ds.getValue(Exams.class).getTags().contains("language")){
                    array1.add(sInfo);
                }else if(ds.getValue(Exams.class).getTags().contains("other")){
                    array2.add(sInfo);
                }else if(ds.getValue(Exams.class).getTags().contains("exam")){
                    array3.add(sInfo);
                }

            }
        }
        prepareListData(array,array1,array2,array3);
        final ExamAdapter adapter = new ExamAdapter(this, listDataHeader,listDataChild);
        mListView.setAdapter(adapter);

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                Exams obj = (Exams) adapter.getChild(groupPosition,childPosition);
                String message = obj.getName();
                Intent i=new Intent(Main2Activity.this,ScrollingActivity.class);

                i.putExtra("Exams",message);
                startActivity(i);

                return false;
            }
        });


    }

    private void prepareListData(ArrayList zeroX,ArrayList oneX,ArrayList twoX,ArrayList threeX) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, ArrayList<Exams>>();

        // Adding child data
        listDataHeader.add("Science");
        listDataHeader.add("Language");
        listDataHeader.add("Other");
        listDataHeader.add("Exams");


        listDataChild.put(listDataHeader.get(0), zeroX); // Header, Child data
        listDataChild.put(listDataHeader.get(1), oneX);
        listDataChild.put(listDataHeader.get(2), twoX);
        listDataChild.put(listDataHeader.get(3), threeX);
    }
}
