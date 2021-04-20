package ca.uwaterloo.cs349;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<Integer> id; //to keep track of different drawings
    private MutableLiveData<ArrayList<SingleDrawing>> allDrawings; //array of all drawings
    private MutableLiveData<Integer> currIndex; //use for redraw

    public SharedViewModel() {
        id = new MutableLiveData<>();
        id.setValue(0);
        allDrawings = new MutableLiveData<ArrayList<SingleDrawing>>();
        allDrawings.setValue(new ArrayList<SingleDrawing>());
        currIndex = new MutableLiveData<>();
        currIndex.setValue(-1);
    }

    public MutableLiveData<Integer> getId() {
        return id;
    }

    public MutableLiveData<ArrayList<SingleDrawing>> getAllDrawings() {
        return allDrawings;
    }

    public MutableLiveData<Integer> getCurrIndex() {
        return currIndex;
    }
}