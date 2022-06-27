package com.example.todotasklist;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListActivity<myCustomArray> extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Task> taskList;
    private List<Task> listItems;
    private DatabaseHandler db;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private EditText taskItem;
    private Button saveButton,sync;
    final static String URL = "http://192.168.1.69:80/simple/myfile.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        FloatingActionButton fab = findViewById(R.id.fab);
        sync = findViewById(R.id.sync);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopUpDialog();
            }
        });

        db = new DatabaseHandler(this);
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        listItems = new ArrayList<>();
        taskList = db.getAllTask();
        for (Task g : taskList) {
            Task task = new Task();
            task.setName(g.getName());
            task.setId(g.getId());
            task.setDateAdded("Added on : " + g.getDateAdded());
            listItems.add(task);
        }
        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync.setBackgroundColor(Color.RED);
                RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response +" and data sent to mysql", Toast.LENGTH_LONG).show();
                        Log.i("data",response);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        }){


                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        for (int i = 0; i < taskList.size(); i++) {
                            params.put("ID", String.valueOf(taskList.get(i).getId()));
                            params.put("TASK", String.valueOf(taskList.get(i).getName()));
                            params.put("DATE", String.valueOf(taskList.get(i).getDateAdded()));
                        }
                        return params;
                    }
                };
                myrequest.add(stringRequest);
            }
        });
    }

    private void createPopUpDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.pop_up, null);
        taskItem = view.findViewById(R.id.item);
        saveButton = view.findViewById(R.id.saveButton);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!taskItem.getText().toString().isEmpty()) {
                    saveTaskToDB(v);
                }
            }
        });

    }

    private void saveTaskToDB(View v) {
        Task task = new Task();
        String newTask = taskItem.getText().toString();
        Toast.makeText(getApplicationContext(), "Name" + newTask , Toast.LENGTH_LONG).show();
        task.setName(newTask);
        db.addTask(task);
        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                startActivity(new Intent(ListActivity.this, ListActivity.class));
            }
        }, 1000);

    }

    public void byPassActivity() {
        if (db.getTaskCount() > 0) {
            startActivity(new Intent(ListActivity.this, ListActivity.class));
            finish();
        }
    }
}