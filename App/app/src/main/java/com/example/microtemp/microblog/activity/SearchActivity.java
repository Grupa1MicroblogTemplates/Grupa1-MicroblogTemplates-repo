package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.RetrofitClient;
import com.example.microtemp.microblog.ui.GetMicroblogResponse;
import com.example.microtemp.microblog.ui.GetPostResponse;
import com.example.microtemp.microblog.ui.MicroblogRecyclerViewAdapter;
import com.example.microtemp.microblog.ui.PostRecyclerViewAdapter;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private static Spinner spinner;
    private static RecyclerView recyclerView;
    private static Button button;
    private static RecyclerView.Adapter adapter;
    private static EditText editText;
    List<GetPostResponse> postList = null;
    List<GetMicroblogResponse> blogList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.post_recyclerview);

        spinner = findViewById(R.id.spinner8);
        ArrayList<String> options = new ArrayList();
        options.add("post");
        options.add("blog");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        recyclerView = findViewById(R.id.search_recyclerview);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String choice = spinner.getSelectedItem().toString();

                JsonObject jsonPost = new JsonObject();
                jsonPost.addProperty("tbl", choice);
                jsonPost.addProperty("key", editText.getText().toString());

                Log.d("JSON", jsonPost.toString());

                if(choice.equals("post")) {
                    loadPost(jsonPost);
                }
                else if(choice.equals("blog")) {
                    loadMicroblogs(jsonPost);
                }
            }
        });

    }

    public void loadPost(JsonObject jsonPost) {

        retrofit2.Call<List<GetPostResponse>> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .searchPost(jsonPost);

        call.enqueue(new Callback<List<GetPostResponse>>() {
            @Override
            public void onResponse(Call<List<GetPostResponse>> call, Response<List<GetPostResponse>> response) {
                if (response.isSuccessful()) {
                    postList = response.body();
                    recyclerViewInitPost(postList);
                    Log.d("JSON", response.body().toString());

                } else {
                    Log.d("POST", Integer.toString(response.code()));
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<GetPostResponse>> call, Throwable t) {
                Log.d("ERROR", t.toString());
            }
        });
    }

    public void recyclerViewInitPost(final List<GetPostResponse> postList) {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new PostRecyclerViewAdapter(postList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new PostRecyclerViewAdapter.
                        RecyclerItemClickListener(this, recyclerView, new PostRecyclerViewAdapter.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }

                    @Override
                    public void onShowPress(View view, int position) {

                    }

                }));



    }
    public void recyclerViewInitBlog(final List<GetMicroblogResponse> blogList) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new MicroblogRecyclerViewAdapter(blogList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new MicroblogRecyclerViewAdapter.
                        RecyclerItemClickListener(this, recyclerView, new MicroblogRecyclerViewAdapter.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }

                    @Override
                    public void onShowPress(View view, int position) {
                    }

                }));

    }

    public void loadMicroblogs(JsonObject jsonMicroblog) {

        retrofit2.Call<List<GetMicroblogResponse>> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .searchBlog(jsonMicroblog);

        call.enqueue(new Callback<List<GetMicroblogResponse>>() {
            @Override
            public void onResponse(Call<List<GetMicroblogResponse>> call, Response<List<GetMicroblogResponse>> response) {
                if (response.isSuccessful()) {
                    blogList = response.body();
                    for (GetMicroblogResponse mess : blogList
                            ) {
                        Log.d("LIST TIEM",mess.getName());
                    }
                    recyclerViewInitBlog(blogList);
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetMicroblogResponse>> call, Throwable t) {
                Log.d("ERROR", t.toString());
            }
        });
    }
}