package mbds;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mbds.api.ApiService;
import mbds.api.CheckMessagesService;
import mbds.api.MessageEntry;
import mbds.api.RetrofitClient;
import mbdse.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMessages extends Fragment {
    private String contactName;
    private TextView tv;
    private Database db;
    private EditText textInput;
    private Button sendBtn;
    private long userID;
    private ApiService mAPIService;
    private iCallable mCallback;

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
        mAPIService = RetrofitClient.getAPIService();
        return v;
    }

    private void onClick(View view) {
        String message = textInput.getText().toString();
        String login = db.getLogin(this.userID);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String date = simpleDateFormat.format(new Date());

        Map<String, String> params = new HashMap<>();
        //'{"message":"testMessage","receiver":"testtest"}'
        params.put("message", message);
        params.put("receiver", this.contactName);
        Log.i("FRAGMENTMESSAGES", "sending messsage: " + message + " to: " + this.contactName);
        //TODO send message with service...have to get the token...
        mAPIService.sendMsg(mCallback.getToken(), params ).enqueue(new Callback<ResponseBody>(){

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    db.addMessage(login, FragmentMessages.this.contactName, FragmentMessages.this.contactName, message, date, FragmentMessages.this.userID);
                    textInput.setText("",null);
                    onResume();
                    Toast.makeText(getContext(),
                            "Message sent successful!", Toast.LENGTH_SHORT).show();
                    try {
                        Log.i("FRAGMENTMESSAGES", "post submitted to API." + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(),
                            "Message not sent!", Toast.LENGTH_SHORT).show();
                    Log.w("FRAGMENTMESSAGES", "post submitted to API." + response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Message not sent!", Toast.LENGTH_SHORT).show();
                Log.e("FRAGMENTMESSAGES", t.getMessage());
            }
        });
    }

    public void setText(String txt, long userID) {
        this.contactName = txt;
        this.userID = userID;
        db=Database.getIstance(this.getContext());
        if(this.contactName == null) return;
        List<MessageEntry> messages = db.readMessages(txt, userID);
        String mstr = "Chat with " + txt + " =>";
        for(MessageEntry me : messages){
            Log.e("FRAGMENT", me.toString());
            if(!me.getAuthor().equals(me.getContact())){
                mstr += "\n me: " + me.getTextmessage();
            }else{
                mstr += "\n " + txt + ": " + me.getTextmessage();
            }
        }
        if(tv!=null){
            tv.setText(mstr);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof iCallable) {
            mCallback = (iCallable) context;
        }else{
            throw new ClassCastException(context.toString()+ " must implement iCallable");
        }
    }
}
