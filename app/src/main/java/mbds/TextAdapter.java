package mbds;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import mbdse.R;
import java.util.List;

class TextAdapter extends RecyclerView.Adapter<TextAdapter.MyViewHolder> {
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView txtv;
        final TextAdapter txta;

        public MyViewHolder(View itemView, TextAdapter txtA) {
            super(itemView);
            txtv = itemView.findViewById(R.id.cellulite);
            txta = txtA;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int mPosition = getLayoutPosition();
// Use that to access the affected item in mWordList.
            String element = strs.get(mPosition);
// Change the word in the mWordList.
            strs.set(mPosition, "Clicked! " + element);
// Notify the adapter, that the data has changed so it can
// update the RecyclerView to display the data.
            txta.notifyDataSetChanged();
            Log.i("USELESSTAG", "Clicked on " + mPosition );
        }
    }

    private final List<String> strs;
    private LayoutInflater mInflater;

    public TextAdapter(Context context, List<String> strs) {
        mInflater = LayoutInflater.from(context);
        this.strs = strs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.cellule, parent, false);
        return new MyViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtv.setText(strs.get(position));
    }

    @Override
    public int getItemCount() { return strs.size(); }
}

