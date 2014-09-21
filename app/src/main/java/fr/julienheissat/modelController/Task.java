package fr.julienheissat.modelcontroller;

import android.location.Address;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by juju on 15/09/2014.
 */
public class Task
{

    private String name;
    private boolean complete;
    private long id;
    private String address;
    private double latitude;
    private double longitude;
    private String project;
    private String priority;
    private long date;

    public Task(String name)
    {
        this.name = name;
    }

    public Task(){}



    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isComplete()
    {
        return complete;

    }

    public void setComplete(boolean complete)
    {
        this.complete = complete;
    }

    public void toggleComplete()
    {
        complete = !complete;

    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(Address a)
    {
        if (null == a)
        {
            address = null;
            latitude = 0;
            longitude = 0;
        } else
        {
            int maxAddressLine = a.getMaxAddressLineIndex();
            StringBuffer sb = new StringBuffer("");

            for (int i = 0; i < maxAddressLine; i++)
            {
                sb.append(a.getAddressLine(i) + " ");
            }

            address = sb.toString();
            latitude = a.getLatitude();
            longitude = a.getLongitude();

        }


    }

    public void setAddress(String address)
    {

        this.address = address;
    }

    public boolean hasAddress()
    {
        return (null != address);

    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public void setTask(long id, String name, boolean complete, String address, double latitude, double longitude, String project, String priority, long date)
    {
        this.id = id;
        this.name = name;
        this.complete = complete;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.project = project;
        this.priority = priority;
        this.date = date;

    }

    public String getProject()
    {
        return project;
    }

    public void setProject(String project)
    {
        this.project = project;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public long getDate()
    {
        return date;
    }

    public String getDateString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultDate = new Date(date);
        return  sdf.format(resultDate);
    }

    public void setDate(long date)
    {
        this.date = date;
    }

}
