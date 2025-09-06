package com.example.uilizacionia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HobbyAdapter extends RecyclerView.Adapter<HobbyAdapter.HobbyViewHolder> {

    private List<Hobby> hobbies;
    private Context context;
    private OnHobbyActionListener listener;

    public interface OnHobbyActionListener {
        void onEditHobby(Hobby hobby);
        void onDeleteHobby(Hobby hobby);
    }

    public HobbyAdapter(Context context, List<Hobby> hobbies, OnHobbyActionListener listener) {
        this.context = context;
        this.hobbies = hobbies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hobby, parent, false);
        return new HobbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HobbyViewHolder holder, int position) {
        Hobby hobby = hobbies.get(position);
        holder.textViewName.setText(hobby.getNombre());
        holder.textViewDifficulty.setText(hobby.getDificultad());

        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditHobby(hobby);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteHobby(hobby);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hobbies.size();
    }

    public void updateHobbies(List<Hobby> newHobbies) {
        this.hobbies = newHobbies;
        notifyDataSetChanged();
    }

    public static class HobbyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDifficulty;
        ImageButton buttonEdit;
        ImageButton buttonDelete;

        public HobbyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewHobbyName);
            textViewDifficulty = itemView.findViewById(R.id.textViewHobbyDifficulty);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
