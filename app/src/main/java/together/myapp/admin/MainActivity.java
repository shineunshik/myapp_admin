package together.myapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout menu_a,menu_b,menu_c,menu_d,menu_e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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