package runtime.in.crosspost;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Set;

/**
 * Created by apple on 02/11/17.
 */

public class SettingFragment extends Fragment{

    public static SettingFragment settingFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting,container,false);
    }

    public static SettingFragment getInstance(){
        if(settingFragment == null){
            settingFragment = new SettingFragment();
        }
        return settingFragment;
    }
}
