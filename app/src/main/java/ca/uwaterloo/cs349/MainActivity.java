package ca.uwaterloo.cs349;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SharedViewModel mViewModel;
    private LinearLayout parentLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //load data into view model
        mViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        SharedPreferences sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("drawing list", null);
        Type type  = new TypeToken<ArrayList<SingleDrawing>>() {}.getType();
        ArrayList<SingleDrawing> temp = gson.fromJson(json, type);
        if(temp != null) {
            mViewModel.getAllDrawings().setValue(temp);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_library, R.id.navigation_addition)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }


}