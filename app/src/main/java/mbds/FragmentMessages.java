package mbds;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import mbdse.R;

public class FragmentMessages extends Fragment {
    private String txt;
    private TextView tv;
    private Database db;
    private EditText textInput;
    private Button sendBtn;
    private long userID;

    public FragmentMessages(){}

    @Override
    public void onResume() {
        super.onResume();
        if(tv!=null){
            setText(this.txt, this.userID);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_messages, container, false);
        tv = v.findViewById(R.id.idTxt);
        textInput = v.findViewById(R.id.messageText);
        sendBtn = v.findViewById(R.id.messageSendButton);
        sendBtn.setOnClickListener(view -> onClick(view));

        return v;
    }

    private void onClick(View view) {
        String message = textInput.getText().toString();
        db.addMessage(this.txt, this.userID, message, true);
        textInput.setText("",null);
        onResume();
    }

    public void setText(String txt, long userID) {
        this.txt = txt;
        this.userID = userID;
        db=Database.getIstance(this.getContext());
        List<Pair<String, Boolean>> messages = db.readMessages(txt, userID);
        String mstr = "Chat with " + txt + " =>";
        for(Pair<String, Boolean> p : messages){
            if(p.second){
                mstr += "\n me: " + p.first;
            }else{
                mstr += "\n " + txt + ": " + p.first;
            }
        }
        if(tv!=null){
            tv.setText(mstr);
        }
    }
}
