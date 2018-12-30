package mbds;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import mbdse.R;
import java.util.List;

class TextAdapter extends RecyclerView.Adapter<TextAdapter.MyViewHolder> {
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtv;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtv = (TextView) itemView.findViewById(R.id.cellulite);
        }
    }

    private List<String> strs;

    public TextAdapter(List<String> strs) {
        this.strs = strs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cellule, parent, false);
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

