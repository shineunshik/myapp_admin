package together.myapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import together.myapp.admin.CustomAdapter.CustomAdapter_Fax_Status_List;
import together.myapp.admin.Object.Ob_Fax_Status;

public class Menu_A_Fax_Payment_Status extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Ob_Fax_Status> arrayList;
    RecyclerView recyclerview;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_a_fax_payment_status);


        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        arrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Menu_A_Fax_Payment_Status.this);
        linearLayoutManager.setReverseLayout(true); //리사이클러뷰 역순으로 보여짐
        linearLayoutManager.setStackFromEnd(true); //역순으로 보여지고 맨위로 올리기
        recyclerview.setLayoutManager(linearLayoutManager);
        adapter = new CustomAdapter_Fax_Status_List(arrayList,Menu_A_Fax_Payment_Status.this);
        recyclerview.setAdapter(adapter);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("oojung_fax_admin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    arrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        arrayList.add(dataSnapshot.getValue(Ob_Fax_Status.class));
                    }
                    adapter.notifyDataSetChanged();


                    recyclerview.scrollToPosition(adapter.getItemCount()-1);

                    /**최신날짜로 오름차순 정리 **/
                    Collections.sort(arrayList, new Comparator<Ob_Fax_Status>() {
                        @Override
                        public int compare(Ob_Fax_Status o1, Ob_Fax_Status o2) {

                            return o1.getSendDT().compareTo(o2.getSendDT());

                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                    System.out.println("실팰ㄴㄹㄴㅇㄹㄴㅇㄹ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}