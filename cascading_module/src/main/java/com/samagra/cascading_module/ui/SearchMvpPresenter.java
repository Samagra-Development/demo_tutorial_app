package com.samagra.cascading_module.ui;


import com.samagra.cascading_module.base.MvpPresenter;
import com.samagra.cascading_module.models.InstitutionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface exposes presenter methods to the view ({@link SearchActivity}) so that the business logic is defined
 * in the presenter, but can be called from the view.
 * This interface should be a type of {@link MvpPresenter}
 *
 * @author Pranav Sharma
 */
//TODO : Document the functions
public interface SearchMvpPresenter<V extends SearchMvpView, I extends SearchMvpInteractor> extends MvpPresenter<V, I> {

    void loadValuesToMemory();

    void addKeyboardListeners(KeyboardHandler keyboardHandler);

    boolean isUDISEValid(String udise, String previousUdise);

    ArrayList<String> getLevel1Values();

    ArrayList<String> getLevel2ValuesUnderLevel1Set(String district);

    ArrayList<String> getLevel3ValuesUnderLevel1Set(String selectedBlock, String selectedDistrict);

    ArrayList<String> getLevel4ValuesMappedUnderLevel3AndLevel1Set(String selectedGramPanchayat, String selectedDistrict);

    InstitutionInfo getInstitutionEntityInformation(String selectedDistrict, String selectedBlock, String selectedCluster, String selectedSchoolName);

    List<InstitutionInfo> getSchoolList();

    InstitutionInfo fetchObjectFromPreferenceString(String studentInfoFromPreferences);

    String generateObjectForStudentData(Object inputObject);
}
