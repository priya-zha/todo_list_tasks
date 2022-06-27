package com.example.todotasklist;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView itemName, dateAdded;
    private int taskId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        itemName = findViewById(R.id.itemNameDetail);
        dateAdded = findViewById(R.id.itemDateAddedDetail);
        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            itemName.setText(bundle.getString("name"));
            dateAdded.setText(bundle.getString("date"));
            taskId = bundle.getInt("id");
        }
    }
}