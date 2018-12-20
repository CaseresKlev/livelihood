package com.livelihood.voidmain.livelihood;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentCall_GroupList extends Fragment {

    List<Group> groupList;

    public FragmentCall_GroupList() {
        groupList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);


        for(int i=0; i<20; i++){
            Group group = new Group(i, "Group 1", "Purok 1, Barangay " + i, "City " + i, (i+1));
            groupList.add(group);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.groupListRecycleView);

        Controller_Group groupController = new Controller_Group(groupList);
        recyclerView.setAdapter(groupController);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        return view;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }
}
