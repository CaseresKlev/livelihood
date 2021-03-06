package com.livelihood.voidmain.livelihood;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class Controller_Group extends RecyclerView.Adapter {


    private List<Group> group_list;
    int group_id;

    public Controller_Group(List<Group> group_list) {
        this.group_list = group_list;
        //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return group_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_groupID, textView_groupName, textView_brgy, textView_city, textView_groupCount;

        public ViewHolder(final View itemView) {
            super(itemView);


            textView_groupID = (TextView) itemView.findViewById(R.id.group_id);
            textView_groupName = (TextView) itemView.findViewById(R.id.group_name);
            textView_brgy = (TextView) itemView.findViewById(R.id.group_brgy);
            textView_city = (TextView) itemView.findViewById(R.id.group_city);
            textView_groupCount = (TextView) itemView.findViewById(R.id.group_member_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //group_id =
                    Context context = itemView.getContext();
                    String temp = textView_groupID.getText().toString();
                    String[] arr = temp.split("ID: ");
                    group_id = Integer.parseInt(arr[1]);
                    //Toast.makeText(context, group_id + "", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AddNew.class);
                    intent.putExtra("com.klevie.livelihood.GROUP_ID", group_id);
                    intent.putExtra("com.klevie.livelihood.USER_ID", 5001);
                    intent.putExtra("com.klevie.livelihood.ACTION", "add");
                    context.startActivity(intent);
                }
            });


        }

        public void bindView(int i) {
            group_id = group_list.get(i).getId();
            textView_groupID.setText("ID: " + group_list.get(i).getId());
            textView_groupName.setText(group_list.get(i).getGroup_name());
            textView_brgy.setText(group_list.get(i).getBrgy());
            textView_city.setText(group_list.get(i).getCity());
            textView_groupCount.setText("Current Members: " +group_id);
        }
    }
}
