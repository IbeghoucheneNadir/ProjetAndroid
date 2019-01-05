package mbds;

public interface TextAdapterListener {

    void textClicked(String str, TextAdapter.MyViewHolder holder);
    boolean longtextClicked(String str, TextAdapter.MyViewHolder holder);

}
