package com.example.navbotdialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter_Team extends RecyclerView.Adapter<ItemAdapter_Team.ItemViewHolder> {

    private List<DataModel> mList;
    private List<String> list = new ArrayList<>();
    private ArrayList<String> Tlist = new ArrayList<>();

    public ItemAdapter_Team(List<DataModel> mList,ArrayList<String> Tlist){
        this.mList  = mList;
        this.Tlist  = Tlist;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item_team , parent , false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {

        DataModel model = mList.get(position);
        holder.mTextView.setText(model.getItemText());
        String TeamName=model.getItemText();

        boolean isExpandable = model.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (isExpandable){
            holder.mArrowImage.setImageResource(R.drawable.arrow_up);
        }else{
            holder.mArrowImage.setImageResource(R.drawable.arrow_down);
        }
//pass Teamname to the NestedAdapter class to delete particular item from database also pass 2 for team class and 1 for editTeam
        NestedAdapter_Team adapter = new NestedAdapter_Team(list,TeamName,Tlist);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(adapter);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setExpandable(!model.isExpandable());
                list = model.getNestedList();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout linearLayout;
        private RelativeLayout expandableLayout;
        private TextView mTextView;
        private ImageView mArrowImage;
        private RecyclerView nestedRecyclerView;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout_team);
            expandableLayout = itemView.findViewById(R.id.expandable_layout_team);
            mTextView = itemView.findViewById(R.id.itemTv_team);
            mArrowImage = itemView.findViewById(R.id.arro_imageview_team);
            nestedRecyclerView = itemView.findViewById(R.id.child_rv_team);
        }
    }
}