package com.zweckinger.myvaccreg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.zweckinger.myvaccreg.databinding.ActivityMainBinding;
import com.zweckinger.myvaccreg.model.VeccregDatabase;
import com.zweckinger.myvaccreg.model.profile.Profile;
import com.zweckinger.myvaccreg.ui.profile.ProfileFormActivity;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static Profile activeProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Handler for creating profile, on initial startup:
        ActivityResultLauncher<Intent> createProfileActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String mode = data.getStringExtra("mode");
                            Serializable content = data.getSerializableExtra("content");
                            if (content != null) {
                                switch (mode) {
                                    case "create":
                                        Profile p = (Profile) content;
                                        p.setActive(true);
                                        VeccregDatabase.databaseWriteExecutor.execute(() -> {
                                            VeccregDatabase.instance.profileDao().insert(p);
                                            activeProfile = VeccregDatabase.instance.profileDao().getActiveProfile();
                                        });
                                        break;

                                    default:
                                        Utils.showError("Database unknown mode error");
                                }
                            } else {
                                Utils.showError("Database name error");
                            }
                        } else {
                            Utils.showError("Database result error");
                        }
                    }
                });

        //Init database (creation of singleton):
        VeccregDatabase db = VeccregDatabase.getDatabase(getApplicationContext());

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Utils.init(binding.getRoot().getContext());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_vaccinations, R.id.navigation_dashboard, R.id.navigation_profile,
                R.id.navigation_illness)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //Force initial profile:
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            activeProfile = db.profileDao().getActiveProfile();

            if(activeProfile == null) {
                List<Profile> profile = db.profileDao().getAlphabetizedProfiles();
                if (profile.size() > 0) {
                    //Disable all active profiles:
                    for (Profile p : profile) {
                        if (p.isActive()) {
                            p.setActive(false);
                            db.profileDao().update(p);
                        }
                    }

                    //Enable only select profile:
                    profile.get(0).setActive(true);
                    db.profileDao().update(profile.get(0));
                } else {
                    Intent myIntent = new Intent(binding.getRoot().getContext(), ProfileFormActivity.class);
                    myIntent.putExtra("mode", "create");
                    myIntent.putExtra("noBack", true);
                    createProfileActivityResultLauncher.launch(myIntent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actionbar_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionbar_settings) {
            startActivity(new Intent(Utils.context, SettingsActivity.class));
        } else if (id == R.id.actionbar_help) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=a0oa3Qiz5tw"));
            startActivity(browserIntent);
        } else if (id == R.id.actionbar_share_email) {
            Toast.makeText(Utils.context, "Feature not implemented", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.actionbar_export) {
            Intent myIntent = new Intent(Utils.context, ImportExportActivity.class);
            myIntent.putExtra("mode", "export");
            startActivity(myIntent);
        } else if (id == R.id.actionbar_import) {
            Intent myIntent = new Intent(Utils.context, ImportExportActivity.class);
            myIntent.putExtra("mode", "import");
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}