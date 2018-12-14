package kr.ac.hansung.thetherfinder;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class DailyBoxOfficeFragment extends Fragment implements MyRecyclerAdapter.MyRecyclerViewClickListener {

    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    private ArrayList<CardItem> list = new ArrayList<>();

    public DailyBoxOfficeFragment() {


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_daily_box_office, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);


        try {
            list = CardItem.createContactsList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recyclerView.setHasFixedSize(true);
        adapter = new MyRecyclerAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onItemClicked(int potition, String title, String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(name);
        builder.setMessage(title);
        builder.setPositiveButton("나가기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}
