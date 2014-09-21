package fr.julienheissat.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import fr.julienheissat.modelcontroller.TaskListController;
import fr.julienheissat.taskmanager.R;
import fr.julienheissat.ui.views.TaskListItem;

/**
 * Created by juju on 15/09/2014.
 */
public class TaskListAdapter extends BaseAdapter implements TaskListController.TaskListControllerListener
{
    private TaskListController tasks;
    private Context context;
    private boolean useList = true;
    private LayoutInflater mInflater;


    public TaskListAdapter(TaskListController tasks, Context context)
    {
        this.tasks = tasks;
        this.tasks.register(this);
        this.context = context;
    }

    /**
     * * @param position * @param convertView * @param parent * @return
     */
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TaskListItem tli;

        if (mInflater==null)
        {

         mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null)
        {
            if (useList)
            {
                tli = (TaskListItem) mInflater.inflate(R.layout.view_task_list_item, null);

            } else

            {
                tli = (TaskListItem) mInflater.inflate(R.layout.view_task_grid_item, null);
            }

        } else
        {
            tli = (TaskListItem) convertView;
        }
        tli.setTask(tasks.get(position));
        return tli;
    }


    @Override
    public int getCount() {
        return tasks.getSize();
    }

    @Override
    public Object getItem(int i) {
        return (null == tasks) ? null : tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void forceReload()
    {

        notifyDataSetChanged();
    }


    @Override
    public void taskListChanged(TaskListController taskList)
    {
        notifyDataSetChanged();
    }
}
