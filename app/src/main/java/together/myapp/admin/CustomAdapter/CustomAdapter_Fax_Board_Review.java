package together.myapp.admin.CustomAdapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import together.myapp.admin.Object.Ob_Fax_Board_Review;
import together.myapp.admin.R;

public class CustomAdapter_Fax_Board_Review extends RecyclerView.Adapter<CustomAdapter_Fax_Board_Review.CustomViewHolder> {
    ArrayList<Ob_Fax_Board_Review> arrayList;
    Context context;
    /** 현재 선택된 항목의 위치를 추적하는 변수 **/
    int selecrposition = RecyclerView.NO_POSITION;
    public CustomAdapter_Fax_Board_Review(ArrayList<Ob_Fax_Board_Review> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomAdapter_Fax_Board_Review.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fax_board_review_layout,parent,false);
        CustomAdapter_Fax_Board_Review.CustomViewHolder customViewHolder = new CustomAdapter_Fax_Board_Review.CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter_Fax_Board_Review.CustomViewHolder holder, int position) {


        holder.content.setText(arrayList.get(position).getContent());
        holder.time.setText(arrayList.get(position).getTime());
        holder.user.setText(arrayList.get(position).getUser());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView content,time,user;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.content = itemView.findViewById(R.id.content);
            this.time = itemView.findViewById(R.id.time);
            this.user = itemView.findViewById(R.id.user);
            view = itemView;

        }
    }

}