package ca.uwaterloo.cs349;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class RedrawFragment extends Fragment {

    private SharedViewModel mViewModel;
    String name = "";

    public AlertDialog createDialog(final View root, final SharedViewModel model, final SingleDrawing redraw_drawing,
                                    final int index) {
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

                        DrawingCanvas drawingCanvas = root.findViewById(R.id.drawing_board_redraw);
                        drawingCanvas.saveCompleted();

                        //we save to current single drawing
                        redraw_drawing.getPoints().clear();
                        redraw_drawing.getPoints().addAll(drawingCanvas.getPoints()); //add the new points
                        redraw_drawing.setDrawingTitle(name);
                        redraw_drawing.setStartX(drawingCanvas.getLastPathStartX());
                        redraw_drawing.setStartY(drawingCanvas.getLastPathStartY());

                        model.getAllDrawings().getValue().set(index, redraw_drawing);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_redraw, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Redraw");

        Integer index = mViewModel.getCurrIndex().getValue();
        final SingleDrawing redraw_drawing = mViewModel.getAllDrawings().getValue().get(index);
        final DrawingCanvas redraw_canvas = (DrawingCanvas) root.findViewById(R.id.drawing_board_redraw);

        redraw_canvas.redrawSetup(redraw_drawing.getPoints(), redraw_drawing.getStartX(), redraw_drawing.getStartY());

        final AlertDialog dialog = createDialog(root, mViewModel, redraw_drawing, index);
        final AlertDialog emptyCanvas = createEmptyCanvasDialog();



            Button back = (Button) root.findViewById(R.id.return_button);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Library");
                    ConstraintLayout parentLinearLayout = (ConstraintLayout) root.findViewById(R.id.redraw_frag_layout);
                    parentLinearLayout.removeAllViews();
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.redraw_frag_layout, new LibraryFragment());
                    fr.commit();
                }
            });

            final Button save = (Button) root.findViewById(R.id.save_redraw_button);
            save.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    DrawingCanvas drawingCanvas = root.findViewById(R.id.drawing_board_redraw);
                    if(drawingCanvas.getPoints().isEmpty()) {
                        emptyCanvas.show();
                    } else {
                        dialog.show();
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                        EditText drawName = (EditText) dialog.findViewById(R.id.drawing_name);
                        drawName.setText(redraw_drawing.getDrawingTitle()); //we set text to be the current name
                        name = redraw_drawing.getDrawingTitle();
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

            Button clear = (Button) root.findViewById(R.id.clear_canvas_redraw_button);
            clear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DrawingCanvas redraw_canvas = (DrawingCanvas) root.findViewById(R.id.drawing_board_redraw);
                redraw_canvas.clearBitmap();
                return true;
                }
        });
        return root;
    }
}