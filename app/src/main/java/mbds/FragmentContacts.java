package mbds;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import mbdse.R;
import java.util.ArrayList;
import java.util.List;

public class FragmentContacts extends Fragment {

    private iCallable mCallback;
    private FloatingActionButton btn;
    private TextAdapter mAdapter;
    private RecyclerView recyclerView;
    private Database db;
    private List<String> nom = new ArrayList<>();
    private List<Person> personne = new ArrayList<>();

    public FragmentContacts() { mAdapter = new TextAdapter(nom); }

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
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
               View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
               int pos = recyclerView.getChildAdapterPosition(child);

                return ( child != null );
                }
            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });
        return v;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        db=Database.getIstance(context);
        personne=db.readPerson();
        for(Person p : personne){
            nom.add(p.getNom());
        }
        mAdapter.notifyDataSetChanged();

        if(context instanceof iCallable)
        {
            mCallback = (iCallable) context;
        }
        else
        {
            throw new ClassCastException(context.toString()+ " must implement iCallable");
        }
    }
}





