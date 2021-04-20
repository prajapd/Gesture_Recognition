package ca.uwaterloo.cs349;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;

import java.util.ArrayList;

public class AdditionFragment extends Fragment {

    private SharedViewModel mViewModel;
    String name = "";

    private void saveData(final SharedViewModel model) {
        SharedPreferences sp = requireActivity().
                getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(model.getAllDrawings().getValue());
        editor.putString("drawing list", json);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        final LayoutInflater inflater = getLayoutInflater();
        final View root = inflater.inflate(R.layout.fragment_addition, null);
        DrawingCanvas drawingCanvas = root.findViewById(R.id.drawing_board);
        drawingCanvas.clearBitmap();
    }

    public AlertDialog createDialog(final View root, final SharedViewModel model) {
        //call saveData here
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = getLayoutInflater();

        //inflate and set the layout fo the dialog
        builder.setView(inflater.inflate(R.layout.save_drawing_dialog_box, null)).setPositiveButton(
                R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View view = inflater.inflate(R.layout.save_drawing_dialog_box, null);
                        EditText getText = (EditText) view.findViewById(R.id.drawing_name);
                        // clear screen

                        DrawingCanvas drawingCanvas = root.findViewById(R.id.drawing_board);
                        drawingCanvas.saveCompleted();


                        //want to create an instance of the "single drawing" and add it to an array of drawings
                        SingleDrawing singleDrawing = new SingleDrawing(name, drawingCanvas.getLastPathStartX(),
                                drawingCanvas.getLastPathStartY(), drawingCanvas.getPoints());

                        Integer value = model.getId().getValue();
                        singleDrawing.setId(value);
                        model.getId().setValue(++value); //increments id

                        //add to array of drawings
                        ArrayList<SingleDrawing> temp = model.getAllDrawings().getValue();
                        temp.add(singleDrawing);
                        model.getAllDrawings().setValue(temp);
                        saveData(model); //saves data to storage
                        Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();
                    }
                }
        ).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return builder.create();
    }

    public AlertDialog createEmptyCanvasDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = getLayoutInflater();

        //inflate and set the layout fo the dialog
        builder.setView(inflater.inflate(R.layout.empty_canvas_dialog_box, null)).setPositiveButton(
                R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }
        );

        return builder.create();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_addition, container, false);

        Button clear = (Button) root.findViewById(R.id.clear_canvas_button);
        final Button save = (Button) root.findViewById(R.id.save_gesture_button);
        clear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DrawingCanvas drawingCanvas = root.findViewById(R.id.drawing_board);
                drawingCanvas.clearBitmap();
                return true;
            }
        });
        final AlertDialog dialog = createDialog(root, mViewModel);
        final AlertDialog emptyCanvas = createEmptyCanvasDialog();

        save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DrawingCanvas drawingCanvas = root.findViewById(R.id.drawing_board);
                if(drawingCanvas.getPoints().isEmpty()) {
                    emptyCanvas.show();
                } else {
                    dialog.show();
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                    EditText drawName = (EditText) dialog.findViewById(R.id.drawing_name);
                    drawName.setText("");
                    drawName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s.length() != 0) {
                                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                            } else {
                                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                            }
                            name = s.toString();
                        }
                    });
                }
                return true;
            }
        });

        return root;
    }
}