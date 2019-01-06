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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mbds.api.MessageEntry;
import mbdse.R;

public class FragmentMessages extends Fragment {
    private String contactName;
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
            setText(this.contactName, this.userID);
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
        String login = db.getLogin(this.userID);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String date = simpleDateFormat.format(new Date());
        db.addMessage(login, this.contactName, this.contactName, message, date, this.userID);
        textInput.setText("",null);
        onResume();
    }

    public void setText(String txt, long userID) {
        this.contactName = txt;
        this.userID = userID;
        db=Database.getIstance(this.getContext());
        if(this.contactName == null) return;
        List<MessageEntry> messages = db.readMessages(txt, userID);
        String mstr = "Chat with " + txt + " =>";
        for(MessageEntry me : messages){
            if(me.getRecipient().equals(me.getContact())){
                mstr += "\n me: " + me.getTextmessage();
            }else{
                mstr += "\n " + txt + ": " + me.getTextmessage();
            }
        }
        if(tv!=null){
            tv.setText(mstr);
        }
    }
}
