package together.myapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    LinearLayout menu_a,menu_b,menu_c,menu_d,menu_e;

    /**팩스 온오프 **/
    TextView power,status;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        power = (TextView)findViewById(R.id.power);
        status = (TextView)findViewById(R.id.status);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("oojung_fax_use").child("use");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String value = snapshot.getValue().toString();

                    if (value.equals("x")){
                        status.setText("OFF");
                    }
                    else if (value.equals("o")){
                        status.setText("ON");
                    }

                    power.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (value.equals("x")){
                                databaseReference.setValue("o");
                            }
                            else if (value.equals("o")){
                                databaseReference.setValue("x");
                            }

                        }
                    });

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        menu_a = (LinearLayout)findViewById(R.id.menu_a);
        menu_b = (LinearLayout)findViewById(R.id.menu_b);
        menu_c = (LinearLayout)findViewById(R.id.menu_c);
        menu_d = (LinearLayout)findViewById(R.id.menu_d);
        menu_e = (LinearLayout)findViewById(R.id.menu_e);


        menu_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Menu_A_Fax_Payment_Status.class);
                startActivity(intent);
            }
        });

        menu_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Menu_B_Board_Status.class);
                startActivity(intent);
            }
        });

    }
}