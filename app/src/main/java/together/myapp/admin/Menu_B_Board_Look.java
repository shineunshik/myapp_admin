package together.myapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import together.myapp.admin.CustomAdapter.CustomAdapter_Fax_Board_Review;
import together.myapp.admin.Object.Ob_Fax_Board_Review;

public class Menu_B_Board_Look extends AppCompatActivity {

    TextView title,time,content;




    /** 리뷰 **/
    RecyclerView recyclerView;
    ArrayList<Ob_Fax_Board_Review> arrayList;
    RecyclerView.Adapter adapter;
    EditText review;
    TextView review_btn;
    String review_m;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference_complete;
    DatabaseReference databaseReference_admin;
    DatabaseReference databaseReference_admin_complete;

    TextView complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bboard_look);

        complete = (TextView)findViewById(R.id.complete);
        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time);
        content = (TextView) findViewById(R.id.content);

        title.setText(getIntent().getStringExtra("title"));
        time.setText(getIntent().getStringExtra("time"));
        content.setText(getIntent().getStringExtra("content"));

        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Menu_B_Board_Look.this);
        layoutManager.setReverseLayout(true); //리사이클러뷰 역순으로 보여짐
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter_Fax_Board_Review(arrayList,Menu_B_Board_Look.this);
        recyclerView.setAdapter(adapter);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("oojung_fax_board").child(getIntent().getStringExtra("user")).child(getIntent().getStringExtra("key")).child("review");
        databaseReference_admin = firebaseDatabase.getReference().child("oojung_fax_board_admin").child(getIntent().getStringExtra("key")).child("review");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    arrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        arrayList.add(snapshot.getValue(Ob_Fax_Board_Review.class));

                    }
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                //입력시 스크롤 맨 아래로
                recyclerView.scrollToPosition(adapter.getItemCount()-1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        review_btn = (TextView)findViewById(R.id.review_btn);
        review = (EditText) findViewById(R.id.review);
        review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review_m = review.getText().toString();

                if (review_m.length()<1){
                    Toast.makeText(Menu_B_Board_Look.this,"1자 이상 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }


                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                //현재시간 저장 객체 생성
                Date date = new Date();
                String time = simpleDateFormat.format(date);

                String key = databaseReference.push().getKey();
                HashMap<String, Object> value = new HashMap<>();
                value.put("key",key);
                value.put("user","관리자");
                value.put("time",time);
                value.put("content",review_m);

                if (getIntent().getStringExtra("key").length()!=0&&getIntent().getStringExtra("key")!=null)
                {
                    databaseReference.child(key).setValue(value);
                    databaseReference_admin.child(key).setValue(value);
                }
                review.setText("");

                /** 글 쓰고 키보드 내리는 방법 **/
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(review.getWindowToken(), 0);

            }
        });
        databaseReference_complete = firebaseDatabase.getReference().child("oojung_fax_board").child(getIntent().getStringExtra("user")).child(getIntent().getStringExtra("key")).child("status");
        databaseReference_admin_complete = firebaseDatabase.getReference().child("oojung_fax_board_admin").child(getIntent().getStringExtra("key")).child("status");
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference_admin_complete.setValue("답변 완료");
                databaseReference_complete.setValue("답변 완료");
            }
        });

    }
}