package together.myapp.admin.CustomAdapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import together.myapp.admin.Menu_B_Board_Look;
import together.myapp.admin.Object.Ob_Fax_Board;
import together.myapp.admin.R;

public class CustomAdapter_Fax_Board_List extends RecyclerView.Adapter<CustomAdapter_Fax_Board_List.CustomViewHolder> {
    ArrayList<Ob_Fax_Board> arrayList;
    Context context;
    /**
     * 현재 선택된 항목의 위치를 추적하는 변수
     **/
    int selecrposition = RecyclerView.NO_POSITION;

    public CustomAdapter_Fax_Board_List(ArrayList<Ob_Fax_Board> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomAdapter_Fax_Board_List.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fax_board_list_layout, parent, false);
        CustomAdapter_Fax_Board_List.CustomViewHolder customViewHolder = new CustomAdapter_Fax_Board_List.CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter_Fax_Board_List.CustomViewHolder holder, int position) {


        holder.title.setText(arrayList.get(position).getTitle());
        holder.time.setText(arrayList.get(position).getTime());
        holder.status.setText(arrayList.get(position).getStatus());
        if (arrayList.get(position).getStatus().equals("답변 완료")){
            holder.status.setTextColor(Color.parseColor("#336185"));
        }
        else if (arrayList.get(position).getStatus().equals("답변 대기")){
            holder.status.setTextColor(Color.parseColor("#FFE4E1"));
        }
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView title, time,status;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title);
            this.time = itemView.findViewById(R.id.time);
            this.status = itemView.findViewById(R.id.status);
            view = itemView;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    Intent intent = new Intent(context, Menu_B_Board_Look.class);
                    intent.putExtra("key", arrayList.get(position).getKey());
                    intent.putExtra("time", arrayList.get(position).getTime());
                    intent.putExtra("title", arrayList.get(position).getTitle());
                    intent.putExtra("content", arrayList.get(position).getContent());
                    intent.putExtra("user", arrayList.get(position).getUser());
                    context.startActivity(intent);
                }
            });

        }
    }

}