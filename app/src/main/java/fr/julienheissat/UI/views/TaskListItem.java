package fr.julienheissat.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.julienheissat.modelcontroller.Task;
import fr.julienheissat.taskmanager.R;

/**
 * Created by juju on 15/09/2014.
 */

public class TaskListItem extends RelativeLayout {


    private ImageView imagePriority;
    private CheckedTextView checkbox;
    private TextView projectView;
    private TextView dateView;

    private Task task;
//    private TextView addressText;

    public TaskListItem(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        imagePriority = (ImageView) findViewById(R.id.list_image);
        checkbox = (CheckedTextView) findViewById(R.id.list_title);
        projectView = (TextView) findViewById(R.id.list_project);
        dateView = (TextView) findViewById(R.id.list_date);
//        addressText = (TextView) findViewById(R.id.address_text_list);

    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
        checkbox.setText(task.getName());
        checkbox.setChecked(task.isComplete());


        Drawable draw = getResources().getDrawable(R.drawable.priority_icon);
        imagePriority.setImageDrawable(draw);

        projectView.setText(task.getProject());
        dateView.setText(task.getDateString());


//        if (task.hasAddress())
//        {
//            addressText.setText(task.getAddress());
//            addressText.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            addressText.setVisibility(View.GONE);
//        }

    }
}
