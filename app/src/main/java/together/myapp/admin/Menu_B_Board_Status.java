package together.myapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import together.myapp.admin.CustomAdapter.CustomAdapter_Fax_Board_List;
import together.myapp.admin.Object.Ob_Fax_Board;
import together.myapp.admin.Object.Ob_Fax_Status;

public class Menu_B_Board_Status extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Ob_Fax_Board> arrayList;
    RecyclerView recyclerview;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_b_board_status);

        arrayList = new ArrayList<>();
        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Menu_B_Board_Status.this);
        layoutManager.setReverseLayout(true); //리사이클러뷰 역순으로 보여짐
        layoutManager.setStackFromEnd(true);
        recyclerview.setLayoutManager(layoutManager);
        adapter = new CustomAdapter_Fax_Board_List(arrayList,Menu_B_Board_Status.this);
        recyclerview.setAdapter(adapter);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("oojung_fax_board_admin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    arrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        arrayList.add(snapshot.getValue(Ob_Fax_Board.class));

                    }
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}