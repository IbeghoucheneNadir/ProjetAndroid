package mbds;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import mbdse.R;

public class FragmentMessages extends Fragment {
    String txt;
    TextView tv;

    public FragmentMessages(){}

    @Override
    public void onResume() {
        super.onResume();
        if(tv!=null) tv.setText(txt);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_messages, container, false);
        tv= (TextView) v.findViewById(R.id.idTxt);
        return v;
    }

    public void setText(String txt) {
        this.txt = txt;
        if(tv!=null)
            tv.setText(txt);
    }
}
