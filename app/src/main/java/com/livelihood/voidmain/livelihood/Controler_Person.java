package com.livelihood.voidmain.livelihood;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Controler_Person extends RecyclerView.Adapter {

    List<Person> personList;
    private int person_id;
    private int group_id;

    public Controler_Person(List<Person> personList) {
        this.personList = personList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.person_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mName, mAddress, txt_person_id;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_person_id = (TextView) itemView.findViewById(R.id.textView_group_id);
            mName = (TextView) itemView.findViewById(R.id.textView_name);
            mAddress = (TextView) itemView.findViewById(R.id.textView_address);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp = txt_person_id.getText().toString();
                    String[] arr = temp.split("PERSON ID: ");
                    person_id = Integer.parseInt(arr[1]);
                    Context context = v.getContext();
                    Intent intent = new Intent(context, PersonDetails.class);
                    intent.putExtra("com.klevie.livelihood.PERSON_ID", person_id);
                    context.startActivity(intent);

                }
            });

        }

        public void bindView(int position){
            mName.setText(personList.get(position).getName());
            mAddress.setText(personList.get(position).getAddress());
            txt_person_id.setText("PERSON ID: " + personList.get(position).getPersonID());
            //group_id = personList.get(position).getGroupID();
        }

        //@Override
        /*public void onClick(View v) {
            TextView nMame = v.findViewById(R.id.textView_name);
            /*String name = nMame.getText().toString();
            Toast.makeText(v.getContext(), name, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), PersonDetails.class );
            startActivity(intent);
        }*/
    }
}
