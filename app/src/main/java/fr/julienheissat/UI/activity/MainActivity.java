package fr.julienheissat.ui.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import fr.julienheissat.taskmanager.R;
import fr.julienheissat.ui.MainTabListener;
import fr.julienheissat.ui.fragment.MapShowFragment;
import fr.julienheissat.ui.fragment.TaskListFragment;


public class MainActivity extends ActionBarActivity implements
        TaskListFragment.OnListFragmentInteractionListener,
        MapShowFragment.OnMapFragmentInteractionListener
{

    ActionBar.Tab tab1, tab2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Notice that setContentView() is not used, because we use the root
        // android.R.id.content as the container for each fragment

        setupActionBar(savedInstanceState);
        // setup action bar for tabs
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add:
                addAction();
                return true;
            case R.id.action_filter:
                filterAction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setupActionBar (Bundle savedInstanceState)
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        tab1 = actionBar.newTab()
                .setText(R.string.list_title)
                .setTabListener(new MainTabListener<TaskListFragment>(
                        this, "list", TaskListFragment.class));
        actionBar.addTab(tab1);

        tab2 = actionBar.newTab()
                .setText(R.string.map_title)
                .setTabListener(new MainTabListener<MapShowFragment>(
                        this, "map", MapShowFragment.class));
        actionBar.addTab(tab2);

    }

    private void addAction()
    {
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivity(intent);
    }

    private void filterAction()
    {
        //To be created
    }



    @Override
    public void onListFragmentInteraction(String id)
    {
        //To be used to communicate with fragment
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {
        //To be used to communicate with fragment
    }



}



//    @Override
//Override    public void onClick(View view)
//    {
//
//        if (view.getId() == R.id.remove_button)
//        {

//        } else if (view.getId() == R.id.add_button)
//        {
//            Intent intent = new Intent(ViewTasksActivity.this, AddTaskActivity.class);
//            startActivity(intent);
//        }
//    }


//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id)
//    {
//        super.onListItemClick(l, v, position, id);
//
//        // adapter.toggleTaskCompleteAtPosition(position);
//        // Task t = (Task) adapter.getItem(position);
//        // app.saveTask(t);
//
//
//    }
