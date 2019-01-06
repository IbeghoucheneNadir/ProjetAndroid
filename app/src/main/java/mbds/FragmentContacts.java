package mbds;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import mbdse.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentContacts extends Fragment implements TextAdapterListener{

    private iCallable mCallback;
    private FloatingActionButton btn;
    private TextAdapter mAdapter;
    private RecyclerView recyclerView;
    private Database db;
    private List<String> nom;
    private List<Person> personne = new ArrayList<>();
    private long userID;

    public FragmentContacts() {  }

    public void transferData(String s) { mCallback.transferData(s); }

    public void setUserID(long userID){
        this.userID = userID;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        btn = v.findViewById(R.id.btnSend);
        btn.setOnClickListener(v1 -> {
            final Intent ma = new Intent(getActivity(), AddContact.class);
            ma.putExtra("UserID", userID);
            ma.putExtra("token", mCallback.getToken());
            startActivity(ma);
        });
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof iCallable) {
            mCallback = (iCallable) context;
        }else{
            throw new ClassCastException(context.toString()+ " must implement iCallable");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        nom = new ArrayList<>();
        db=Database.getIstance(this.getContext());
        personne=db.readPerson(userID);
        for(Person p : personne){
            nom.add(p.getNom());
        }
        recyclerView.setAdapter(new TextAdapter(this.getContext(),nom, this));
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void textClicked(String str, TextAdapter.MyViewHolder holder) {
        Log.i("POSITION", ""+holder.getAdapterPosition());
        holder.itemView.setBackgroundColor(Color.BLUE);
        transferData(str);
        Timer timer = new Timer(str, true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }, 1000);

    }

    @Override
    public boolean longtextClicked(String str, TextAdapter.MyViewHolder holder) {
        holder.itemView.setBackgroundColor(Color.RED);
        new AlertDialog.Builder(getContext())
                .setTitle("Are you sure?")
                .setMessage("Do you really want to delete contact " + str + " with all it's messages ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.removePerson(str, userID);
                        onResume();
                        Toast.makeText(getContext(), "contact " + str + " deleted!", Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
        Timer timer = new Timer(str, true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }, 1000);
        return true;
    }
}





