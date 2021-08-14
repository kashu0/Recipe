package mycookapp.listener;

import com.androidstudioprojects.mycookapp.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void onModify();
}
