package runtime.in.crosspost;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by apple on 02/11/17.
 */

public class PostFragment extends Fragment {

    public static PostFragment postFragment;

    CheckBox chbFacebook, chbGoogle, chbTwitter, chbInstagram;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        chbFacebook = (CheckBox) rootView.findViewById(R.id.facebook);
        chbGoogle = (CheckBox) rootView.findViewById(R.id.google);
        chbTwitter = (CheckBox) rootView.findViewById(R.id.twitter);
        chbInstagram = (CheckBox) rootView.findViewById(R.id.instagram);

        MyCheckedChangeListener checkedChangeListener = new MyCheckedChangeListener();
        chbFacebook.setOnCheckedChangeListener(checkedChangeListener);
        chbGoogle.setOnCheckedChangeListener(checkedChangeListener);
        chbTwitter.setOnCheckedChangeListener(checkedChangeListener);
        chbInstagram.setOnCheckedChangeListener(checkedChangeListener);
        return rootView;
    }

    public static PostFragment getInstance() {
        if (postFragment == null) {
            postFragment = new PostFragment();
        }
        return postFragment;
    }

    class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (compoundButton.getId() == R.id.facebook) {
                if (isChecked)
                    chbFacebook.setTextColor(Color.parseColor(Constants.Colors.FacebookBlue));
                else
                    chbFacebook.setTextColor(Color.BLACK);

            } else if (compoundButton.getId() == R.id.google) {
                if (isChecked)
                    chbGoogle.setTextColor(Color.parseColor(Constants.Colors.GoogleRed));
                else
                    chbGoogle.setTextColor(Color.BLACK);
            } else if (compoundButton.getId() == R.id.twitter) {
                if (isChecked)
                    chbTwitter.setTextColor(Color.parseColor(Constants.Colors.TwitterBlue));
                else
                    chbTwitter.setTextColor(Color.BLACK);
            } else if (compoundButton.getId() == R.id.instagram) {
                if (isChecked)
                    chbInstagram.setTextColor(Color.parseColor(Constants.Colors.InstagramViolet));
                else
                    chbInstagram.setTextColor(Color.BLACK);
            }
        }
    }


}
