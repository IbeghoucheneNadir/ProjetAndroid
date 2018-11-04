package com.example.mbdse.tp1android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SecActivityFragment extends Fragment {
        String txt;
        TextView tv;

public SecActivityFragment(){}

public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
{
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_sec, container, false);
        tv= (TextView) v.findViewById(R.id.idTxt);
        return v;
}
        public void setText(String txt)
        {
                this.txt = txt;
            if(tv!=null)
                tv.setText(txt);
        }

        @Override
        public void onResume()
        {
                super.onResume();
                if(tv!=null)
                        tv.setText(txt);
        }
}
