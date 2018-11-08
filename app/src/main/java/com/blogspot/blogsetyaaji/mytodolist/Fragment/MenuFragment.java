package com.blogspot.blogsetyaaji.mytodolist.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.blogsetyaaji.mytodolist.R;
import com.blogspot.blogsetyaaji.mytodolist.utils.ClickListener;
import com.blogspot.blogsetyaaji.mytodolist.utils.RecyclerClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private RecyclerView lvmenu;

    private int[] menuGambar = {
            R.drawable.todo,
            R.drawable.progress,
            R.drawable.done
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        lvmenu = (RecyclerView) view.findViewById(R.id.lvmenu);
        // pengaturan tampilan susunan data
        lvmenu.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        String[] menuApp = {
                getActivity().getString(R.string.mn_todo),
                getActivity().getString(R.string.mn_progres),
                getActivity().getString(R.string.mn_done)};
        // masukkan data ke adapter
        MyMenuAdapter myMenuAdapter = new MyMenuAdapter(getActivity(), menuApp, menuGambar);
        // pasang adapter dengan recyclerview
        lvmenu.setAdapter(myMenuAdapter);

        lvmenu.addOnItemTouchListener(new RecyclerClickListener(getActivity(),
                lvmenu, new ClickListener() {
            @Override
            public void OnClick(View view, int posisi) {

            }

            @Override
            public void onLongClick(View view, int posisi) {
                switch (posisi) {
                    case 0 :
                        TodoFragment todoFragment = new TodoFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, todoFragment).commit();
                        break;
                    case 1 :
                        ProgressFragment progressFragment = new ProgressFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, progressFragment).commit();
                        break;
                    case 2 :
                        DoneFragment doneFragment = new DoneFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, doneFragment).commit();
                        break;
                }
            }
        }));

        return view;
    }

}
