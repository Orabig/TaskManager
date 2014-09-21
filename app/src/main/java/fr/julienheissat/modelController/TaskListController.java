package fr.julienheissat.modelcontroller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static fr.julienheissat.database.TaskSQLiteOpenHelper.TASK_ADDRESS;
import static fr.julienheissat.database.TaskSQLiteOpenHelper.TASK_COMPLETE;
import static fr.julienheissat.database.TaskSQLiteOpenHelper.TASK_DATE;
import static fr.julienheissat.database.TaskSQLiteOpenHelper.TASK_ID;
import static fr.julienheissat.database.TaskSQLiteOpenHelper.TASK_LATITUDE;
import static fr.julienheissat.database.TaskSQLiteOpenHelper.TASK_LONGITUDE;
import static fr.julienheissat.database.TaskSQLiteOpenHelper.TASK_NAME;
import static fr.julienheissat.database.TaskSQLiteOpenHelper.TASK_PRIORITY;
import static fr.julienheissat.database.TaskSQLiteOpenHelper.TASK_PROJECT;
import static fr.julienheissat.database.TaskSQLiteOpenHelper.TASK_TABLE;

/**
 * Created by juju on 18/09/2014.
 */
public class TaskListController
{

    private ArrayList<Task> taskList;
    private SQLiteDatabase database;
    private ArrayList<TaskListControllerListener> listOfListener;

    public TaskListController(SQLiteDatabase database)
    {
        this.database = database;
        loadTasks();
        listOfListener = new ArrayList<TaskListControllerListener>();
    }

    private void loadTasks()
    {
        taskList = new ArrayList<Task>();
        Cursor tasksCursor  = database.query(
                TASK_TABLE,
                new String[] {TASK_ID, TASK_NAME,TASK_COMPLETE,TASK_ADDRESS,TASK_LATITUDE,TASK_LONGITUDE, TASK_PROJECT,TASK_PRIORITY,TASK_DATE},
                null, null, null, null,String.format("%s,%s",TASK_COMPLETE,TASK_NAME));
        tasksCursor.moveToFirst();
        Task t;

        if (!tasksCursor.isAfterLast())
        {
            do
            {
                long id = tasksCursor.getLong(0);
                String name= tasksCursor.getString(1);
                String boolValue=tasksCursor.getString(2);
                boolean complete=Boolean.parseBoolean(boolValue);
                String address = tasksCursor.getString(3);
                float latitude = tasksCursor.getFloat(4);
                float longitude = tasksCursor.getFloat(5);
                String project = tasksCursor.getString(6);
                String priority = tasksCursor.getString(7);
                long date= tasksCursor.getLong(8);

                t=new Task(name);
                t.setTask(id,name,complete,address,latitude,longitude,project,priority,date);
                taskList.add(t);

            }
            while (tasksCursor.moveToNext());
        }

        tasksCursor.close();

    }

    public void addTask(Task t)
    {
        assert (null != t);

        ContentValues values = new ContentValues();
        values.put(TASK_NAME,t.getName());
        values.put(TASK_COMPLETE,Boolean.toString(t.isComplete()));
        values.put(TASK_ADDRESS,t.getAddress());
        values.put(TASK_LATITUDE,t.getLatitude());
        values.put(TASK_LONGITUDE,t.getLongitude());
        values.put(TASK_PROJECT,t.getProject());
        values.put(TASK_PRIORITY,t.getPriority());
        values.put(TASK_DATE,t.getDate());

        long id = database.insert(TASK_TABLE, null, values);
        t.setId(id);

        taskList.add(t);

    }

    private void saveTask(Task t)

    {
        assert (null != t);

        ContentValues values = new ContentValues();
        values.put(TASK_NAME,t.getName());
        values.put(TASK_COMPLETE,Boolean.toString(t.isComplete()));
        values.put(TASK_ADDRESS,t.getAddress());
        values.put(TASK_LATITUDE,t.getLatitude());
        values.put(TASK_LONGITUDE,t.getLongitude());
        values.put(TASK_PROJECT,t.getProject());
        values.put(TASK_PRIORITY,t.getPriority());
        values.put(TASK_DATE,t.getDate());

        long id = t.getId();
        String where = String.format("%s = ?",TASK_ID);
        database.update(TASK_TABLE,values,where,new String[]{id+""});
    }


    public void deleteTasks(Long[]ids)
    {
        StringBuffer idList = new StringBuffer();
        for (int i=0;i<ids.length;i++)
        {
            idList.append(ids[i]);
            if(i<ids.length-1)
            {
                idList.append(",");
            }

        }

        String where = String.format("%s in (%s)",TASK_ID,idList);
        database.delete(TASK_TABLE,where,null);
    }

    public int getSize()
    {
        return taskList.size();
    }

    public Task get(int i)
    {
        return taskList.get(i);
    }

    public void toggleTask(int position)
    {
        Task task = get(position);
        task.toggleComplete();
        saveTask(task);
        updateAllListeners();
    }

    public Long[] removeCompletedTasks() {
        ArrayList<Long> completedIds= new ArrayList<Long>();
        ArrayList<Task> completedTasks= new ArrayList<Task>();
        for (Task t:taskList)
        {
            if (t.isComplete())
            {
                completedIds.add(t.getId());
                completedTasks.add(t);

            }
        }

        taskList.removeAll(completedTasks);
        deleteTasks(completedIds.toArray(new Long[]{}));
        updateAllListeners();
        return completedIds.toArray(new Long[]{});
    }

    //Interface to update adapter listeners when some specific changes to taskList happens

    public static interface TaskListControllerListener
    {
        public void taskListChanged(TaskListController taskList);
    }

    public void updateAllListeners()
    {
        for (TaskListControllerListener listener: listOfListener)
        {
            listener.taskListChanged(this);
        }
    }

    public void register(TaskListControllerListener listener)

    {
        listOfListener.add(listener);
    }
    public void unregister(TaskListControllerListener listener)
    {
        listOfListener.remove(listener);
    }
    public void unRegisterAll()
    {
        listOfListener.removeAll(listOfListener);
    }

}
