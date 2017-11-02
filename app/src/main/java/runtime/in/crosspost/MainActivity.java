package runtime.in.crosspost;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Add the main postFragment
        this.changeFragment(FragmentType.PostFragment);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MainActivity.this.changeFragment(FragmentType.PostFragment);
                    return true;
                case R.id.navigation_dashboard:
                    MainActivity.this.changeFragment(FragmentType.SettingsFragment);
                    return true;
            }
            return false;
        }
    };

    private void changeFragment(FragmentType fragmentType) {
        if (this.fragmentManager == null) {
            this.fragmentManager = getFragmentManager();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (fragmentType) {
            case PostFragment:
                transaction.replace(R.id.fragmentContainer, PostFragment.getInstance());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case SettingsFragment:
                transaction.replace(R.id.fragmentContainer, SettingFragment.getInstance());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }

    }

    enum FragmentType {
        PostFragment,
        SettingsFragment
    }
}
