package com.samagra.parent.ui.Settings;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.samagra.parent.R;

import org.jetbrains.annotations.NotNull;

public class UpdateAppLanguageFragment extends Fragment {

    private TextView hindiLanguage;
    private TextView englishLanguage;
    private ImageView checkEnglish, checkHindi;
    private String currentLanguage, selectedLanguage;
    private ILanguageChangedListener languageChangedListener;

    public static UpdateAppLanguageFragment newInstance(String language, ILanguageChangedListener iLanguageChangedListener) {
        Bundle bundle = new Bundle();
        bundle.putString("appLanguage", language);
        UpdateAppLanguageFragment changeLanguageActivity = new UpdateAppLanguageFragment();
        changeLanguageActivity.languageChangedListener =iLanguageChangedListener;
        changeLanguageActivity.setArguments(bundle);
        return changeLanguageActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_change_language, container, false);
    }

    public void onViewCreated(@NotNull View root, Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        hindiLanguage = root.findViewById(R.id.language_hindi);
        englishLanguage = root.findViewById(R.id.language_english);
        checkEnglish = root.findViewById(R.id.check_english);
        Button updateLanguage = root.findViewById(R.id.update_language);
        checkHindi = root.findViewById(R.id.check_hindi);
        ImageView closeScreen = root.findViewById(R.id.ttb_close_button);
        closeScreen.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        if (getArguments() != null && getArguments().getString("appLanguage") != null && getArguments().getString("appLanguage").equals("hi")) {
            hindiLanguage.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            hindiLanguage.setTypeface(Typeface.DEFAULT_BOLD);
            englishLanguage.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_grey));
            englishLanguage.setTypeface(Typeface.DEFAULT);
            checkEnglish.setVisibility(View.INVISIBLE);
            checkHindi.setVisibility(View.VISIBLE);
            currentLanguage = "hi";
            selectedLanguage = "hi";
        } else {
            englishLanguage.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            englishLanguage.setTypeface(Typeface.DEFAULT_BOLD);
            hindiLanguage.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_grey));
            hindiLanguage.setTypeface(Typeface.DEFAULT);
            checkEnglish.setVisibility(View.VISIBLE);
            checkHindi.setVisibility(View.INVISIBLE);
            currentLanguage = "en";
            selectedLanguage = "en";
        }
        hindiLanguage.setOnClickListener(v -> {
            if (!selectedLanguage.equals("hi"))
                onLanguageSelected(hindiLanguage, checkHindi, englishLanguage, checkEnglish, "hi");
        });

        englishLanguage.setOnClickListener(v -> {
            if (!selectedLanguage.equals("en"))
                onLanguageSelected(englishLanguage, checkEnglish, hindiLanguage, checkHindi, "en");

        });
        updateLanguage.setOnClickListener(v -> {
            onUpdateLanguageSelected();
        });

    }

    private void onUpdateLanguageSelected() {
        if(!currentLanguage.equals(selectedLanguage)) {
            languageChangedListener.onLanguageChanged(selectedLanguage);
        }
        getActivity().onBackPressed();

    }

    private void onLanguageSelected(TextView selectedTextView, ImageView selectedCheck, TextView deselectedTextView, ImageView deselectedCheck, String selectedCode) {
        selectedTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.primaryTextColor));
        selectedTextView.setTypeface(Typeface.DEFAULT_BOLD);
        deselectedTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_grey));
        deselectedTextView.setTypeface(Typeface.DEFAULT);
        selectedCheck.setVisibility(View.VISIBLE);
        deselectedCheck.setVisibility(View.INVISIBLE);
        selectedLanguage = selectedCode;
    }


}
