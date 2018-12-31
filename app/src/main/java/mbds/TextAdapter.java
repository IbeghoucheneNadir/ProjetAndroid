package mbds;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import mbdse.R;
import java.util.List;

class TextAdapter extends RecyclerView.Adapter<TextAdapter.MyViewHolder> {
    public class MyViewHolder extends RecyclerView.ViewHolder{

        public final TextView txtv;
        final TextAdapter txta;

        public MyViewHolder(View itemView, TextAdapter txtA) {
            super(itemView);
            txtv = itemView.findViewById(R.id.cellulite);
            txta = txtA;
        }
    }

    private final List<String> strs;
    private LayoutInflater mInflater;
    private TextAdapterListener tal;

    public TextAdapter(Context context, List<String> strs, TextAdapterListener tal) {
        mInflater = LayoutInflater.from(context);
        this.strs = strs;
        this.tal = tal;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.cellule, parent, false);
        return new MyViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtv.setText(strs.get(position));
        holder.txtv.setOnClickListener((view -> tal.textClicked(strs.get(position), holder)));
    }

    @Override
    public int getItemCount() { return strs.size(); }
}

