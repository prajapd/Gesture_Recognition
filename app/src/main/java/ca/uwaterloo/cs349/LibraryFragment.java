package ca.uwaterloo.cs349;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LibraryFragment extends Fragment {

    private SharedViewModel mViewModel;
    private LinearLayout parentLinearLayout;

    //on start and on resume, we should create the listings

    private void saveData(final SharedViewModel model) {
        SharedPreferences sp = requireActivity().
                getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(model.getAllDrawings().getValue());
        editor.putString("drawing list", json);
        editor.apply();
    }

    public void displayDrawings(final SharedViewModel model) {
        final ArrayList<SingleDrawing> drawings = model.getAllDrawings().getValue(); // all drawings
        if(drawings != null) {
            for(int i = 0; i < drawings.size(); i++) {
                LayoutInflater inflater =  (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.display_drawing, null); //find view from display drawing
                DisplayDrawing dd = rowView.findViewById(R.id.display_drawing); //get the canvas to draw on
                final SingleDrawing sd = drawings.get(i); //get the drawing at index i

                dd.setAttr(sd.getStartX(), sd.getStartY(), sd.getPoints());

                TextView tv = (TextView) rowView.findViewById(R.id.draw_title);
                tv.setText(sd.getDrawingTitle());
                Button delete = (Button) rowView.findViewById(R.id.delete_row_button);
                Button replace = (Button) rowView.findViewById(R.id.replace_icon);
                final int finalI = i;

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawings.remove(finalI); //remove current drawing from the list of drawings
                        model.getAllDrawings().setValue(drawings); //update the current list of drawings
                        parentLinearLayout.removeAllViews(); //remove all the current views
                        displayDrawings(model); //redraw with new set
                        saveData(model);
                    }
                });

                replace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.getCurrIndex().setValue(finalI);
                        parentLinearLayout.removeAllViews(); //remove all the current views
                        FragmentTransaction fr = getFragmentManager().beginTransaction();
                        fr.replace(R.id.linear_layout_fragment_library, new RedrawFragment());
                        fr.commit();
                    }
                });
                parentLinearLayout.addView(rowView);
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_library, container, false);
        parentLinearLayout = (LinearLayout) root.findViewById(R.id.linear_layout_fragment_library);
        parentLinearLayout.removeAllViews();
        displayDrawings(mViewModel);

        return root;
    }


}