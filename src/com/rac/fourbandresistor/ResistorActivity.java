package com.rac.fourbandresistor;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Intent;
import android.content.SharedPreferences;


public class ResistorActivity extends Activity {
    
    /** The preference key for theme color mode. */
    protected static final String PREFERENCES_COLOR_MODE = "COLOR_MODE";
    
    protected int[] themeResourceIDs = new int[] {
            R.style.AppTheme_Accessibility_Normal,
            R.style.AppTheme_Accessibility_HighContrast,
            R.style.AppTheme_Accessibility_Protanopia,
            R.style.AppTheme_Accessibility_Tritanopia,
            R.style.AppTheme_Accessibility_Deuteranopia
    };
    
    /** The state bundle key for corresponding bands. */
    protected final String[] configVarNames = new String[] {
            "Band1Value",
            "Band2Value",
            "Band3Value",
            "Band4Value"
    };
    
    /** Four bands = four color pickers. */
    public static final int COUNT_COLOR_PICKERS = 4;
    
    /** The Ohm Unicode code point. */
    public static final char OHM_SYMBOL = (char)0x2126;
    
    /** The plus/minus Unicode code point. */
    public static final char PLUSMINUS_SYMBOL = (char)0xB1;
    
    /** The resource IDs for first band colors. */
    public int[] resourceIDsBandOne = new int[] {
            //R.drawable.band1none,
            //R.drawable.band1black,
            R.drawable.band1brown,
            R.drawable.band1red,
            R.drawable.band1orange,
            R.drawable.band1yellow,
            R.drawable.band1green,
            R.drawable.band1blue,
            R.drawable.band1violet,
            R.drawable.band1grey,
            R.drawable.band1white,
            //R.drawable.band1gold,
            //R.drawable.band1silver,
    };
    
    /** The resource IDs for second band colors. */
    public int[] resourceIDsBandTwo = new int[] {
            //R.drawable.band2none,
            R.drawable.band2black,
            R.drawable.band2brown,
            R.drawable.band2red,
            R.drawable.band2orange,
            R.drawable.band2yellow,
            R.drawable.band2green,
            R.drawable.band2blue,
            R.drawable.band2violet,
            R.drawable.band2grey,
            R.drawable.band2white,
            //R.drawable.band2gold,
            //R.drawable.band2silver,
    };

    /** The resource IDs for third band colors. */
    public int[] resourceIDsBandThree = new int[] {
            //R.drawable.band3none,
            R.drawable.band3black,
            R.drawable.band3brown,
            R.drawable.band3red,
            R.drawable.band3orange,
            R.drawable.band3yellow,
            R.drawable.band3green,
            R.drawable.band3blue,
            //R.drawable.band3violet,
            //R.drawable.band3grey,
            //R.drawable.band3white,
            R.drawable.band3gold,
            R.drawable.band3silver,
    };
    
    /** The resource IDs for fourth band colors. */
    public int[] resourceIDsBandFour = new int[] {
            R.drawable.band4none,
            //R.drawable.band4black,
            R.drawable.band4brown,
            R.drawable.band4red,
            //R.drawable.band4orange,
            //R.drawable.band4yellow,
            //R.drawable.band4green,
            //R.drawable.band4blue,
            //R.drawable.band4violet,
            //R.drawable.band4grey,
            //R.drawable.band4white,
            R.drawable.band4gold,
            R.drawable.band4silver,
    };
    
    /** Multi-dimensional array containing lists of drawable resource IDs by band number. */
    protected int[][] bandImageResourceIDs = new int[][] {
            resourceIDsBandOne,
            resourceIDsBandTwo,
            resourceIDsBandThree,
            resourceIDsBandFour
    };
    
    /** The array of NumberPickers corresponding to each band color. */
    protected NumberPicker[] colorPickers = new NumberPicker[COUNT_COLOR_PICKERS];
    
    /** The array of ImageViews corresponding to resistor bands. */
    protected ImageView[] imgBands = new ImageView[4];
    
    /** The selected band colors. Each element corresponds to the index of a resource ID. */
    protected int[] colors = new int[4];
    
    /** The array of Resource IDs corresponding to resistor band ImageViews. */
    protected int[] imgViewResourceIDs = new int[] {
            R.id.resistorbandone,
            R.id.resistorbandtwo,
            R.id.resistorbandthree,
            R.id.resistorbandfour,
    };
    
    /** The array of Resource IDs corresponding to resistor band NumberPickers. */
    protected int[] colorPickerResourceIDs = new int[] {
            R.id.band1value,
            R.id.band2value,
            R.id.band3value,
            R.id.band4value
    };
    
    /** The precision values specified by the fourth band. Note that no color means +-20%. */
    protected double[] precisionValues = new double[] { 0.20D, 0.01D, 0.02D, 0.05D, 0.10D };
    
    /** The text to represent powers of 1000. */
    protected String[] modifiers = new String[] {
            "", // 0-999
            "K", // 1,000 - 999,999
            "M", // 1,000,000+
    };
    
    /** The output TextView to display the amount of resistance. */
    protected TextView txtOhms = null;
    
    /** The array of Resource IDs corresponding to String arrays for valid band colors. */
    protected int[] bandColorNamesResourceIDs = new int[] {
            R.array.band_one_colors,
            R.array.band_two_colors,
            R.array.band_three_colors,
            R.array.band_four_colors
    };
    
    /**
     * Gets the band color names for band (id+1).
     * @param id the band ID (0-3).
     * @return an array of valid values.
     */
    protected String[] getBandColorNames(int id) {
        return getResources().getStringArray(bandColorNamesResourceIDs[id]);
    }
    
    /**
     * Gets the band image corresponding to band (id+1).
     * @param id the band ID (0-3).
     * @return the ImageView corresponding to this band.
     */
    protected ImageView getBandImage(int id) {
        return imgBands[id];
    }
    
    /**
     * Gets the number picker that is to contain valid selections.
     * @param id the band ID (0-3).
     * @return the number picker corresponding to this band.
     */
    protected NumberPicker getColorPicker(int id) {
        return colorPickers[id];
    }
    
    /**
     * Sets the color picker corresponding to band (id+1).
     * @param id the band ID (0-3).
     * @param np the number picker to use to select colors.
     */
    protected void setColorPicker(int id, NumberPicker np) {
        colorPickers[id] = np;
    }
    
    /**
     * Gets the ID of a color picker.
     * @param np the number picker to get the ID of.
     * @return the ID corresponding to that NumberPicker.
     */
    protected int getColorPickerId(NumberPicker np) {
        // Iterate over the array and find which index it corresponds to.
        for (int i = 0; i < colorPickers.length; i++) {
            if (colorPickers[i] == np) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Gets the array containing resource IDs corresponding to drawable
     * images or this band.
     * @param bandNumber the band number (0-3).
     * @return the array of resource IDs corresponding to drawable images.
     */
    protected int[] getBandResourceIDs(int bandNumber) {
        return bandImageResourceIDs[bandNumber];
    }
    
    /**
     * Sets the selected color value of the band.
     * @param bandNumber the band number (0-3).
     * @param colorIndex the color index within the valid values.
     */
    protected void setColorValue(int bandNumber, int colorIndex) {
        colors[bandNumber] = colorIndex;
        updateOhmValue();
        updateBandImage(bandNumber, getBandResourceIDs(bandNumber));
    }
    
    /**
     * Sets the current picker value.
     * @param bandNumber the band number (0-4).
     * @param index the index of the NumberPicker item to select.
     */
    protected void setPickerValue(int bandNumber, int index) {
        colorPickers[bandNumber].setValue(index);
        // The setValue method does not fire events when called programmatically.
        setColorValue(bandNumber, index);
    }
    
    /**
     * Gets the Ohm resistance value using the color IDs.
     * @param firstBand the color ID used by the first band.
     * @param secondBand the color ID used by the second band.
     * @param thirdBand the color ID used by the third band.
     * @return the Ohm resistance band for this configuration.
     */
    protected double getOhmValue(int firstBand, int secondBand, int thirdBand) {
        double value = 0.0D;
        // First band. First band has an order of 1 (10^1 = 10).
        // The first band has no black (0), so we need to offset by 1.
        value += (Math.max(0, firstBand + 1)) * 10;
        
        // Second band. Second band has an order of 0 (10^0 = 1).
        value += Math.max(0, (secondBand));
        
        // Third band. Third band determines the order of magnitude
        // of the Ohm output. Rules for the third band:
        // n=0 (Black) to n=6 (Blue) means (value * 10^(n-1)) Ohms.
        // n=7 (Gold) to n=8 (Silver) means (value * 10^(6 - n)) Ohms.
        // - Special rules apply for silver and gold: instead of proceeding higher
        //   to 10^7, it goes to 10^-1, so for n = {7, 8}, f(n) = 10^(6 - n).
        if (0 <= thirdBand && thirdBand < 7) {
            value *= Math.pow(10, (thirdBand));
        } else {
            value *= Math.pow(10, (6 - thirdBand));
        }
        return value;
    }
    
    /**
     * Gets the precision value of the fourth band color.
     * @param fourthBand the color ID of the fourth band.
     * @return the precision value of this configuration.
     */
    protected double getOhmValuePrecision(int fourthBand) {
        // Fourth band. This band just determines the precision.
        return precisionValues[fourthBand];
    }
    
    /**
     * Calculates the power of the value, which will be equal to the
     * integer part of the base-1000 logarithm of the value, or 0 if
     * that would be negative.
     * @param value the Ohm resistance value.
     * @return the base-1000 order of magnitude of the value, or 0 if
     * that would be negative.
     */
    protected int getOhmPower(double value) {
        return (int)Math.max(0, Math.log10(value) / Math.log10(1000));
    }
    
    /**
     * Gets the Ohm suffix to reduce large values to smaller strings. 
     * @param power the Ohm power (0-3).
     * @return the suffix indicating this power.
     */
    protected String getOhmModifier(int power) {
        return modifiers[power];
    }
    
    /**
     * Updates the Ohm value based on the NumberPicker values.
     */
    protected void updateOhmValue() {
        double value = getOhmValue(colors[0], colors[1], colors[2]);
        double precision = getOhmValuePrecision(colors[3]);
        int power = getOhmPower(value);
        String modifier = getOhmModifier(power);
        
        // Divide it by the power of 1000 (e.g. 1K = divide by 1000, 1M = divide by 1000000).
        value /= Math.pow(1000, power);
        
        // Hide precision errors.
        value = Math.floor(100 * value) / 100;
        
        txtOhms.setText(Double.toString(value) + modifier + OHM_SYMBOL + " " + PLUSMINUS_SYMBOL + " " + (100 * precision) + "%");
    }
    
    /**
     * Updates the band image corresponding to the current configuration.
     * @param id the band ID (0-3).
     * @param resourceIDs the resource IDs to use.
     */
    protected void updateBandImage(int id, int[] resourceIDs) {
        ImageView img = getBandImage(id);
        img.setImageResource(resourceIDs[colors[id]]);
    }
    
    /**
     * Initializes a color picker.
     * @param i the band ID. This is used to align arrays which
     * determine the resources to use.
     */
    protected void initializeColorPicker(int i) {
        NumberPicker np = getColorPicker(i);
        
        String[] colorNames = getBandColorNames(i);
        // Align with arrays: start at 0, go to (colors - 1).
        np.setMinValue(0);
        np.setMaxValue(colorNames.length - 1);
        np.setDisplayedValues(colorNames);
        
        // Add an event listener to respond to changes in value.
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker np, int oldValue, int newValue) {
                int id = getColorPickerId(np);
                if (id == -1) {
                    // What the fuck? Get me out of here.
                    return;
                }
                setColorValue(id, newValue);
            }
        });
    }
    
    /**
     * Initializes the application state.
     */
    protected void initialize() {
        // Find the text field to display the value.
        txtOhms = (TextView)findViewById(R.id.resistorvalue);
        
        for (int i = 0; i < COUNT_COLOR_PICKERS; i++) {
            // Find the NumberPicker for each band.
            setColorPicker(i, (NumberPicker)findViewById(colorPickerResourceIDs[i]));
            // Find the image views for each band.
            imgBands[i] = (ImageView)findViewById(imgViewResourceIDs[i]);
            // Initialize the color picker corresponding to this band.
            initializeColorPicker(i);
        }
        
    }
    
    /**
     * Restores the activity's saved state. This will be called on
     * orientation change to retain the UI state.
     * @param instanceState the saved instance to restore from.
     */
    protected void restoreInstanceState(Bundle instanceState) {
        for (int i = 0; i < COUNT_COLOR_PICKERS; i++) {
            setPickerValue(i, instanceState.getInt(configVarNames[i], 0));
        }
    }
    
    /**
     * Gets the theme preference resource ID.
     * @return the theme preference resource ID.
     */
    protected int getThemePreference() {
        SharedPreferences prefs = getSharedPreferences("colormode", MODE_PRIVATE);
        int pref = prefs.getInt(ResistorActivity.PREFERENCES_COLOR_MODE, 1);
        return themeResourceIDs[pref];
    }
    
    /**
     * Starts the preferences activity.
     */
    protected void startPreferencesActivity() {
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivityForResult(intent, 0);
    }
    
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent intent) {
        super.onActivityResult(reqCode, resCode, intent);
        this.recreate();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        for (int i = 0; i < COUNT_COLOR_PICKERS; i++) {
            state.putInt(configVarNames[i], colors[i]);
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getThemePreference());
        setContentView(R.layout.activity_resistor);
        //View v = (View)findViewById()
        initialize();
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.resistor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_preferences) {
            startPreferencesActivity();
        }
        return super.onOptionsItemSelected(item);
    }
}
