package com.example.todotasklist;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Task> taskList;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.taskItemName.setText(task.getName());
        holder.dateAdded.setText(task.getDateAdded());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView taskItemName;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            taskItemName = itemView.findViewById(R.id.name);
            dateAdded = itemView.findViewById(R.id.dataAdded);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show();
                    int position = getAdapterPosition();
                    Task task = taskList.get(position);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", task.getId());
                    intent.putExtra("name", task.getName());
                    intent.putExtra("date", task.getDateAdded());
                    context.startActivity(intent);
                }
            });
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editButton:
                    int position = getAdapterPosition();
                    Task task = taskList.get(position);
                    editItem(task);
                    break;
                case R.id.deleteButton:
                    position = getAdapterPosition();
                    task = taskList.get(position);
                    deleteItem(task.getId());
                    break;
            }
        }

        public void deleteItem(final int id) {
            dialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);
            Button noBtn = view.findViewById(R.id.noButton);
            Button yesBtn = view.findViewById(R.id.yesButton);
            dialogBuilder.setView(view);
            dialog = dialogBuilder.create();
            dialog.show();

            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteTask(id);
                    taskList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                }
            });
        }

        public void editItem(final Task task) {
            dialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.pop_up, null);
            final EditText taskItem = view.findViewById(R.id.item);
            final TextView title = view.findViewById(R.id.title);

            title.setText("Edit Task");
            taskItem.setText(task.getName());
            Button saveButton = (Button) view.findViewById(R.id.saveButton);
            dialogBuilder.setView(view);
            dialog = dialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    task.setName(taskItem.getText().toString());
                    if (!taskItem.getText().toString().isEmpty()) {
                        db.updateTask(task);
                        notifyItemChanged(getAdapterPosition(), task);
                    } else {
                        Snackbar.make(view, "Add Grocery and quantity", Snackbar.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            });
        }
    }


}
