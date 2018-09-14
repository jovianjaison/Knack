package joviananthony.knack;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExamAdapterArr extends ArrayAdapter<Exams> {
    public ExamAdapterArr(Activity context, ArrayList<Exams> Exams) {

        super(context, 0, Exams);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Exams currentExam = getItem(position);

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.textView5);
        nameTextView.setText(currentExam.getName());

        TextView tagsTextView = (TextView) listItemView.findViewById(R.id.textView6);
        tagsTextView.setText(currentExam.getTags());


        return listItemView;
    }
}