package mbds;

import android.content.Context;
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
import mbdse.R;
import java.util.ArrayList;
import java.util.List;

public class FragmentContacts extends Fragment implements TextAdapterListener{

    private iCallable mCallback;
    private FloatingActionButton btn;
    private TextAdapter mAdapter;
    private RecyclerView recyclerView;
    private Database db;
    private List<String> nom;
    private List<Person> personne = new ArrayList<>();

    public FragmentContacts() {  }

    public void transferData(String s) { mCallback.transferData(s); }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        btn = v.findViewById(R.id.btnSend);
        btn.setOnClickListener(v1 -> {
            final Intent ma = new Intent(getActivity(), AddContact.class);
            startActivity(ma);
            transferData("hello coucou ");
        });
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
//               View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
//               int pos = recyclerView.getChildAdapterPosition(child);
//
//                return ( child != null );
//                }
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean b) {
//
//            }
//        });
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
        personne=db.readPerson();
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
    }
}





