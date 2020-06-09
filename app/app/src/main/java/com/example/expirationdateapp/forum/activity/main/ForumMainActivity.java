package com.example.expirationdateapp.forum.activity.main;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.forum.activity.editor.EditorActivity;
import com.example.expirationdateapp.forum.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ForumMainActivity extends Fragment implements MainView{

    private static final int INTENT_EDIT = 200;
    private static final int INTENT_ADD = 100;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    MainPresenter presenter;
    MainAdapter adapter;
    MainAdapter.ItemClickListener itemClickListener;
    List<Note> note;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_forum_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Toolbar 세팅
        Toolbar toolbar = view.findViewById(R.id.forumFrag_toobar_top);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        fab = view.findViewById(R.id.add);
        fab.setOnClickListener(click_view -> startActivityForResult(new Intent(requireContext(), EditorActivity.class), INTENT_ADD)
        );

        presenter = new MainPresenter(this);
        presenter.getData();
        swipeRefresh.setOnRefreshListener(() -> presenter.getData()
        );

        itemClickListener = ((click_view, position) -> {
            int id = note.get(position).getId();
            String title = note.get(position).getTitle();
            String notes = note.get(position).getNote();
            int color = note.get(position).getColor();

            Intent intent = new Intent(requireContext(), EditorActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("title", title);
            intent.putExtra("note", notes);
            intent.putExtra("color", color);
            startActivityForResult(intent, INTENT_EDIT);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == INTENT_ADD && resultCode == RESULT_OK){
            presenter.getData();
        }else if (requestCode == INTENT_EDIT && resultCode ==RESULT_OK){
            presenter.getData();
        }
    }

    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onGetResult(List<Note> notes) {
        adapter = new MainAdapter(requireContext(), notes, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        note = notes;
    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
