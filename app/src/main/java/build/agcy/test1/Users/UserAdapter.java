package build.agcy.test1.Users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import build.agcy.test1.Models.User;
import build.agcy.test1.R;

/**
 * Created by Freeman on 17.08.2014.
 */
public class UserAdapter extends BaseAdapter {
    private final ArrayList<User> users;
    private final Context context;
     public UserAdapter(Context context, ArrayList<User> users){
        this.context = context;
        this.users = users;
    }
    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // понимаешь что вообще происходит:?вроде да мы раздуваем вью, и заносим туда данные в каждый итем
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.item_user, null);
        TextView nameView = (TextView) rootView.findViewById(R.id.user_name);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.user_photo);
        User user = getItem(position);
        nameView.setText(user.username);
        // imageView и биндим как-то картинку, не важно

        return rootView;
    }
}
