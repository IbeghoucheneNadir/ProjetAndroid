package com.example.mbdse.tp1android;

        import android.os.Bundle;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.TextView;

        import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.MyViewHolder> {
    private List<String> strs;

    public TextAdapter(List<String> strs) {
        this.strs = strs;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtv;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtv = (TextView) itemView.findViewById(R.id.cellulite);
        }
    }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cellule
                            , parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.txtv.setText(strs.get(position));
        }

        @Override
        public int getItemCount() {
            return strs.size();
        }
    }

