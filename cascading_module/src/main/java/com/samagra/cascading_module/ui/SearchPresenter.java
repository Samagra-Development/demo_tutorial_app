package com.samagra.cascading_module.ui;


import android.app.Activity;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.samagra.cascading_module.CascadingModuleDriver;
import com.samagra.cascading_module.R;
import com.samagra.cascading_module.base.BasePresenter;
import com.samagra.cascading_module.models.InstitutionInfo;
import com.samagra.cascading_module.tasks.SearchSchoolTask;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * The Presenter class for Search Screen. This class controls interaction between the View and Data.
 * This class <b>must</b> implement the {@link SearchMvpPresenter} and <b>must</b> be a type of {@link BasePresenter}.
 *
 * @author Pranav Sharma
 */
public class SearchPresenter<V extends SearchMvpView, I extends SearchMvpInteractor> extends BasePresenter<V, I> implements SearchMvpPresenter<V, I> {

    private List<InstitutionInfo> listOfHostpitals = new ArrayList<>();
    private String two_Spaces = "   ";

    @Inject
    public SearchPresenter(I mvpInteractor) {
        super(mvpInteractor);
    }

    //TODO : Make Asynchronous for less delay in loading
    @Override
    public void loadValuesToMemory() {
        File dataFile = new File(CascadingModuleDriver.ROOT + "/data2.json");
        try {
            JsonReader jsonReader = new JsonReader(new FileReader(dataFile));

            Gson gson = new GsonBuilder()
                    .enableComplexMapKeySerialization()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .setPrettyPrinting()
                    .setVersion(1.0)
                    .create();

            Type type = new TypeToken<ArrayList<InstitutionInfo>>() {
            }.getType();
            listOfHostpitals = gson.fromJson(jsonReader, type);
            addDummySchoolAtTheStart();
            jsonReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            Timber.e("Exception in loading data to memory %s", e.getMessage());
        }
    }

    private Document updateDocumentTag(Document document, String tag, String value){
        try{
            if(document.getElementsByTagName(tag).item(0).getChildNodes().getLength() > 0)
                document.getElementsByTagName(tag).item(0).getChildNodes().item(0).setNodeValue(value);
            else
                document.getElementsByTagName(tag).item(0).appendChild(document.createTextNode(value));
        }catch (Exception e){
            Timber.e("Unable to autofill: %s %s", tag, value);
            return document;
        }
        return document;
    }

    private void addDummySchoolAtTheStart() {
        InstitutionInfo dummy = new InstitutionInfo("  Select the District Name",
                "  Select the Block Name",
                "  Select the Institution Category",
                "  Select the Institution Name");
        listOfHostpitals.add(0, dummy);
    }

    @Override
    public void addKeyboardListeners(KeyboardHandler keyboardHandler) {
        KeyboardVisibilityEvent.setEventListener((Activity) getMvpView().getActivityContext(), isOpen -> {
            keyboardHandler.isUDISEKeyboardShowing = isOpen;
            if (isOpen) keyboardHandler.closeDropDown();
        });
    }

    @Override
    public boolean isUDISEValid(String udise, String previousUdise) {
        Pattern testPattern = Pattern.compile("^[0-9]{10}$");
        Matcher testString = testPattern.matcher(udise);
        if (previousUdise != null)
            return testString.matches() && !previousUdise.equals(udise);
        else
            return testString.matches();
    }

    @Override
    public ArrayList<String> getLevel1Values() {
        ArrayList<String> districtValues = new ArrayList<>();

        for (int i = 1; i < listOfHostpitals.size(); i++) {
            districtValues.add(listOfHostpitals.get(i).District);
        }
        ArrayList<String> newList = SearchSchoolTask.makeUnique(districtValues);
        newList.add(0, two_Spaces + getMvpView().getActivityContext().getResources().getString(R.string.dummy_district));
        return newList;
    }

    @Override
    public ArrayList<String> getLevel2ValuesUnderLevel1Set(String district) {
        ArrayList<String> blockValues = new ArrayList<>();
        for (int i = 0; i < listOfHostpitals.size(); i++) {
            if (listOfHostpitals.get(i).District.equals(district)) {
                blockValues.add(listOfHostpitals.get(i).Block);
            }
        }
        ArrayList<String> newList = SearchSchoolTask.makeUnique(blockValues);
        newList.add(0, two_Spaces + getMvpView().getActivityContext().getResources().getString(R.string.dummy_block));
        return newList;
    }

    @Override
    public ArrayList<String> getLevel3ValuesUnderLevel1Set(String selectedBlock, String selectedDistrict){
        ArrayList<String> gramPanchayatsUnderBlock = new ArrayList<>();
        for (int i = 0; i < listOfHostpitals.size(); i++) {
            if (listOfHostpitals.get(i).Block.equals(selectedBlock) && listOfHostpitals.get(i).District.equals(selectedDistrict)) {
                gramPanchayatsUnderBlock.add(listOfHostpitals.get(i).Category);
            }
        }
        ArrayList<String> newList = SearchSchoolTask.makeUnique(gramPanchayatsUnderBlock);
        newList.add(0, two_Spaces + getMvpView().getActivityContext().getResources().getString(R.string.dummy_gram_panchayat));
        return newList;
    }

    @Override
    public ArrayList<String> getLevel4ValuesMappedUnderLevel3AndLevel1Set(String selectedInstitutionType, String selectedDistrict){
        ArrayList<String> hospitalsUnderGP = new ArrayList<>();
        for (int i = 0; i < listOfHostpitals.size(); i++) {
            if (listOfHostpitals.get(i).Category.equals(selectedInstitutionType) && listOfHostpitals.get(i).District.equals(selectedDistrict)) {
                hospitalsUnderGP.add(listOfHostpitals.get(i).Institution);
            }
        }
        ArrayList<String> newList = SearchSchoolTask.makeUnique(hospitalsUnderGP);
        newList.add(0, two_Spaces + getMvpView().getActivityContext().getResources().getString(R.string.dummy_hospital));
        return newList;
    }



    @Override
    public InstitutionInfo getInstitutionEntityInformation(String selectedDistrict, String selectedBlock, String selectedInstitutionType, String selectedInstitutionName) {
        for (InstitutionInfo institutionInfo : listOfHostpitals) {
            if (institutionInfo.District.equals(selectedDistrict)
                    && institutionInfo.Block.equals(selectedBlock)
                    && institutionInfo.Category.equals(selectedInstitutionType)
                    && institutionInfo.Institution.equals(selectedInstitutionName)
            ) return institutionInfo;
        }
        return null;
    }

    @Override
    public InstitutionInfo fetchObjectFromPreferenceString(String studentInfoFromPreferences) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(studentInfoFromPreferences, InstitutionInfo.class);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public String generateObjectForStudentData(Object inputObject) {
        try {
            Gson gson = new Gson();
            return gson.toJson(inputObject);
        } catch ( Exception e) {
            return "";
        }
    }

    @Override
    @NonNull
    public List<InstitutionInfo> getSchoolList() {
        return listOfHostpitals;
    }
}