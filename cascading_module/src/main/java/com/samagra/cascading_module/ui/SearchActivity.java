package com.samagra.cascading_module.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.samagra.cascading_module.CascadingModuleDriver;
import com.samagra.cascading_module.R;
import com.samagra.cascading_module.R2;
import com.samagra.cascading_module.base.BaseActivity;
import com.samagra.cascading_module.models.InstitutionInfo;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * View part of the Search Screen. This class only handles the UI operations, all the business logic is simply
 * abstracted from this Activity. It <b>must</b> implement the {@link SearchMvpView} and extend the {@link BaseActivity}.
 *
 * @author Pranav Sharma
 */
public class SearchActivity extends BaseActivity implements SearchMvpView {

    @BindView(R2.id.level_1_spinner)
    public Spinner level1Spinner;
    @BindView(R2.id.level_2_spinner)
    public Spinner level2Spinner;
    @BindView(R2.id.level_3_spinner)
    public Spinner level3Spinner;
    @BindView(R2.id.level_4_spinner)
    public Spinner level4Spinner;
    @BindView(R2.id.next_button)
    public Button nextButton;
    @BindView(R2.id.rootView)
    public ConstraintLayout rootView;

    private String selectedLevel1Element ="";
    private String selectedLevel2Element = "";
    private String selectedLevel3Element = "";
    private String selectedLevel4Element = "";
    InstitutionInfo selectedAggregateElement;
    private KeyboardHandler keyboardHandler;
    private int count = 0;
    private String two_Spaces = "   ";
    @Inject
    SearchPresenter<SearchMvpView, SearchMvpInteractor> searchPresenter;
    private Unbinder unbinder;
    SharedPreferences sharedPreferences;
    String retreivedAggregateStringFromPreferences;
    InstitutionInfo retreivedAggregateDataFromPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this);
        searchPresenter.onAttach(this);
        setupToolbar();
        initializeKeyboardHandler();
        searchPresenter.addKeyboardListeners(keyboardHandler);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        retreivedAggregateStringFromPreferences = sharedPreferences.getString("cascadingStoredAggregate", "");

        retreivedAggregateDataFromPreferences = TextUtils.isEmpty(retreivedAggregateStringFromPreferences) ? null : searchPresenter.fetchObjectFromPreferenceString(retreivedAggregateStringFromPreferences);
        if (retreivedAggregateDataFromPreferences != null) {
            count = 1;
            selectedAggregateElement = retreivedAggregateDataFromPreferences;
            selectedLevel1Element = retreivedAggregateDataFromPreferences.District;
            selectedLevel2Element = retreivedAggregateDataFromPreferences.Block;
            selectedLevel3Element = retreivedAggregateDataFromPreferences.Category;
            selectedLevel4Element = retreivedAggregateDataFromPreferences.Institution;
        }
        searchPresenter.loadValuesToMemory();
        initSpinners();
        initNextButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        customizeToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Institution Details Selection");
        setSupportActionBar(toolbar);
    }

    private void customizeToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    private void initializeKeyboardHandler() {
        keyboardHandler = new KeyboardHandler(false,
                false, null, SearchActivity.this);
    }

    private void initSpinners() {
        addValuesToSpinner(level1Spinner, searchPresenter.getLevel1Values());
        setListenerOnLevel1Spinner();
        setListenerOnLevel2Spinner();
        setListenerOnLevel3Spinner();
        setListenerOnLevel4Spinner();
        if (retreivedAggregateStringFromPreferences != null && retreivedAggregateDataFromPreferences != null) {
            nextButton.setEnabled(true);
            level2Spinner.setEnabled(true);
            level3Spinner.setEnabled(true);
            level4Spinner.setEnabled(true);
            setDataForSpinnersExplicit(retreivedAggregateDataFromPreferences, false);
        } else {
            nextButton.setEnabled(false);
            level2Spinner.setEnabled(false);
            level3Spinner.setEnabled(true);
            level4Spinner.setEnabled(true);
        }
    }




    private void setDataForSpinnersExplicit(InstitutionInfo individualSelectedAggregateData, boolean b) {
        ArrayList<String> level1Values = searchPresenter.getLevel1Values();
        addValuesToSpinner(level1Spinner, level1Values);
        selectedLevel1Element = individualSelectedAggregateData.District;
        level1Spinner.setSelection(level1Values.indexOf(individualSelectedAggregateData.District));

        ArrayList<String> level2Values = searchPresenter.getLevel2ValuesUnderLevel1Set(individualSelectedAggregateData.District);
        addValuesToSpinner(level2Spinner, level2Values);
        selectedLevel2Element = individualSelectedAggregateData.Block;
        level2Spinner.setSelection(level2Values.indexOf(individualSelectedAggregateData.Block));

        ArrayList<String> level3Values = searchPresenter.getLevel3ValuesUnderLevel1Set(individualSelectedAggregateData.Block, selectedLevel1Element);
        addValuesToSpinner(level3Spinner, level3Values);
        selectedLevel3Element = individualSelectedAggregateData.Category;
        level3Spinner.setSelection(level3Values.indexOf(individualSelectedAggregateData.Category));

        ArrayList<String> level4Values = searchPresenter.getLevel4ValuesMappedUnderLevel3AndLevel1Set(individualSelectedAggregateData.Category, selectedLevel1Element);
        addValuesToSpinner(level4Spinner, level4Values);
        selectedLevel4Element = individualSelectedAggregateData.Institution;
        level4Spinner.setSelection(level4Values.indexOf(individualSelectedAggregateData.Institution));
    }

    private void setListenerOnLevel1Spinner() {
        level1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (retreivedAggregateStringFromPreferences == null || retreivedAggregateStringFromPreferences.isEmpty() || (!selectedLevel1Element.equals(parent.getItemAtPosition(position).toString()))) {
                    selectedLevel1Element = parent.getItemAtPosition(position).toString();
                    level2Spinner.setEnabled(true);
                    level2Spinner.setClickable(true);
                    keyboardHandler.spinner = null;
                    if (count == 1) {
                        if (!retreivedAggregateStringFromPreferences.isEmpty()) {
                            retreivedAggregateStringFromPreferences = "";
                            retreivedAggregateDataFromPreferences = null;
                        }
                        count = 0;
                        selectedLevel2Element = two_Spaces + getActivityContext().getResources().getString(R.string.dummy_block);
                        selectedLevel3Element = two_Spaces + getActivityContext().getResources().getString(R.string.dummy_gram_panchayat);
                        selectedLevel4Element = two_Spaces + getActivityContext().getResources().getString(R.string.dummy_hospital);
                    }
                    addValuesToSpinner(
                            level2Spinner,
                            searchPresenter.getLevel2ValuesUnderLevel1Set(selectedLevel1Element)
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                makeSpinnerDefault(level2Spinner);
                makeSpinnerDefault(level3Spinner);
                makeSpinnerDefault(level4Spinner);

            }
        });
    }


    private void setListenerOnLevel2Spinner() {
        level2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (retreivedAggregateStringFromPreferences.isEmpty() || (!selectedLevel2Element.equals(parent.getItemAtPosition(position).toString()))) {
                    selectedLevel2Element = parent.getItemAtPosition(position).toString();
                    level3Spinner.setEnabled(true);
                    level3Spinner.setClickable(true);
                    keyboardHandler.spinner = null;
                    if (count == 1) {
                        if (!retreivedAggregateStringFromPreferences.isEmpty()) {
                            retreivedAggregateStringFromPreferences = "";
                            retreivedAggregateDataFromPreferences = null;
                        }
                        count = 0;
                        selectedLevel3Element = two_Spaces + getActivityContext().getResources().getString(R.string.dummy_gram_panchayat);
                        selectedLevel4Element = two_Spaces + getActivityContext().getResources().getString(R.string.dummy_hospital);
                    }
                    addValuesToSpinner(
                            level3Spinner,
                            searchPresenter.getLevel3ValuesUnderLevel1Set(selectedLevel2Element, selectedLevel1Element)
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                makeSpinnerDefault(level3Spinner);
                makeSpinnerDefault(level4Spinner);
            }
        });
    }

    private void setListenerOnLevel3Spinner() {
        level3Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (retreivedAggregateStringFromPreferences.isEmpty() || (!selectedLevel3Element.equals(parent.getItemAtPosition(position).toString()))) {
                    selectedLevel3Element = parent.getItemAtPosition(position).toString();
                    level4Spinner.setEnabled(true);
                    level4Spinner.setClickable(true);
                    keyboardHandler.spinner = null;
                    if (count == 1) {
                        if (!retreivedAggregateStringFromPreferences.isEmpty()) {
                            retreivedAggregateStringFromPreferences = "";
                            retreivedAggregateDataFromPreferences = null;
                        }
                        count = 0;
                        selectedLevel4Element = two_Spaces + getActivityContext().getResources().getString(R.string.dummy_hospital);
                    }
                    addValuesToSpinner(
                            level4Spinner,
                            searchPresenter.getLevel4ValuesMappedUnderLevel3AndLevel1Set(selectedLevel3Element, selectedLevel1Element)
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                makeSpinnerDefault(level4Spinner);
            }
        });
    }


    private void setListenerOnLevel4Spinner() {
        level4Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (retreivedAggregateStringFromPreferences.isEmpty() || (!selectedLevel4Element.equals(parent.getItemAtPosition(position).toString()))) {
                    keyboardHandler.spinner = null;
                    selectedLevel4Element = parent.getItemAtPosition(position).toString();
                    selectedAggregateElement = new InstitutionInfo(selectedLevel1Element, selectedLevel2Element, selectedLevel3Element, selectedLevel4Element);
                    // Enable the next button
                    nextButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                makeSpinnerDefault(level4Spinner);
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void addValuesToSpinner(Spinner spinner, ArrayList<String> values) {
        String[] val = values.toArray(new String[0]);
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, val);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnTouchListener((v, event) -> {
            keyboardHandler.spinner = spinner;
            keyboardHandler.isDropDownOpen = true;
            if (keyboardHandler.isUDISEKeyboardShowing) keyboardHandler.closeUDISEKeyboard();
            return false;
        });

    }

    private void initNextButton() {
        nextButton.setOnClickListener(v -> {
            if (level1Spinner.getSelectedItem().toString().equals(two_Spaces + getActivityContext().getResources().getString(R.string.dummy_district)) ||
                    level2Spinner.getSelectedItem().toString().equals(two_Spaces + getActivityContext().getResources().getString(R.string.dummy_block)) ||
                    level3Spinner.getSelectedItem().toString().equals(two_Spaces + getActivityContext().getResources().getString(R.string.dummy_gram_panchayat)) ||
                    level4Spinner.getSelectedItem().toString().equals(two_Spaces + getActivityContext().getResources().getString(R.string.dummy_hospital))) {
                SnackbarUtils.showLongSnackbar(rootView, getActivityContext().getResources().getString(R.string.error_message_search));
            } else {
                updatePreferenceData();
                CascadingModuleDriver.sendBackData(selectedAggregateElement);
                finish();
            }
        });
    }

    @SuppressWarnings("PointlessNullCheck")
    private void updatePreferenceData() {
        if (retreivedAggregateDataFromPreferences == null || !selectedAggregateElement.equals(retreivedAggregateDataFromPreferences)) {
            sharedPreferences.edit().putString("cascadingStoredAggregate", searchPresenter.generateObjectForStudentData(selectedAggregateElement)).apply();
        }
    }

    /**
     * This functions simply shifts a {@link Spinner} to its default state; a state that prevents
     * a user from interacting with the spinner. This is essentially disabling a spinner on the UI.
     *
     * @param spinner - The {@link Spinner} widget to disable
     */
    @Override
    public void makeSpinnerDefault(Spinner spinner) {
        spinner.setEnabled(false);
        spinner.setClickable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        searchPresenter.onDetach();
    }
}
