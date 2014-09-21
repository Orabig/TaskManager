package fr.julienheissat.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import fr.julienheissat.database.TaskSQLiteOpenHelper;
import fr.julienheissat.modelcontroller.LocationController;
import fr.julienheissat.modelcontroller.TaskListController;

/**
 * Created by juju on 15/09/2014.
 */
public class TaskManagerApplication extends Application
{


    private SQLiteDatabase database;
    private LatLng latLng;
    private TaskListController taskListController;



    private LocationController locationController;

    @Override
    public void onCreate()
    {
        super.onCreate();
        TaskSQLiteOpenHelper helper= new TaskSQLiteOpenHelper(this);
        database = helper.getWritableDatabase();
        taskListController = new TaskListController(database);
        locationController = new LocationController(this);

    }


    public LocationController getLocationController()
    {
        return locationController;
    }


    public TaskListController getTaskListController()
    {
        return taskListController;
    }




}
