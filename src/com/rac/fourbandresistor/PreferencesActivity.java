package com.rac.fourbandresistor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends Activity {
    
    /** List of items for the ArrayAdapter. */
    protected List<String> preferenceList = new ArrayList<String>();
    
    /** Preference strings. */
    protected final String[] preferences = new String[] {
            "Normal",
            "High Contrast",
            "Protan",
            "Tritan",
            "Deutran"
    };
    
    /** Theme resource IDs */
    protected final int[] themeResourceIDs = new int[] {
            R.style.AppTheme_Accessibility_Normal,
            R.style.AppTheme_Accessibility_HighContrast,
            R.style.AppTheme_Accessibility_Protanopia,
            R.style.AppTheme_Accessibility_Tritanopia,
            R.style.AppTheme_Accessibility_Deuteranopia,
    };
    
    /**
     * Initializes the preferences activity.
     */
    protected void initialize() {
        // We need a reference to the current context,
        // for reference in anonymous classes.
        final Activity context = this;
        
        ListView listView = (ListView)findViewById(R.id.list_preferences);
        
        // Populate the ArrayList.
        for (int i = 0; i < preferences.length; i++) {
            preferenceList.add(preferences[i]);
        }
        
        // Create the ArrayAdapter for the ListView.
        ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, preferenceList
        );
        listView.setAdapter(arrAdapter);
        
        // Set the listeners.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View targetView, int row, long id) {
                // Save the selected item as a preference.
                SharedPreferences prefs = getSharedPreferences("colormode", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(ResistorActivity.PREFERENCES_COLOR_MODE, row);
                editor.commit();
                // Destroy the activity.
                context.finish();
            }
        });
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_Accessibility_HighContrast);
        setContentView(R.layout.activity_preferences);
        initialize();
    }
}
