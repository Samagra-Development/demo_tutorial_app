package com.samagra.ancillaryscreens.models;

import android.os.Bundle;

import com.samagra.ancillaryscreens.screens.about.AboutActivity;

/**
 * This class acts as a configuration object for the {@link AboutActivity}.
 * This class contains a {@link Bundle} containing all the variables necessary for personalizing the {@link AboutActivity}
 *
 * @author Pranav Sharma
 */
public class AboutBundle {
    private String screenTitle;
    private String websiteUrl;
    private String forumUrl;
    private int websiteIconResId;
    private int websiteLinkTextResId;
    private int websiteSummaryDescriptionResId;
    public Bundle aboutExchangeBundle;

    /**
     * All the variables required for configuring the {@link AboutActivity} have to be passed here.
     * No alternate public constructors since all the variables are mandatory.
     */
    public AboutBundle(String screenTitle, String websiteUrl, String forumUrl, int websiteIconResId, int websiteLinkTextResId, int websiteSummaryDescriptionResId) {
        this.screenTitle = screenTitle;
        this.websiteUrl = websiteUrl;
        this.forumUrl = forumUrl;
        this.websiteIconResId = websiteIconResId;
        this.websiteLinkTextResId = websiteLinkTextResId;
        this.websiteSummaryDescriptionResId = websiteSummaryDescriptionResId;
        this.aboutExchangeBundle = generateExchangeBundle();
    }

    private AboutBundle() {
        // No Access.
    }

    /**
     * This method uses all the class members provided via {@link AboutBundle#AboutBundle(String, String, String, int, int, int)}
     * and generates a {@link Bundle} that can be passed along with an {@link android.content.Intent} to {@link AboutActivity}
     *
     * @return a {@link Bundle} created from all the class members.
     * @see AboutActivity#configureActivityFromBundle(Bundle)
     */
    private Bundle generateExchangeBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("title", screenTitle);
        bundle.putString("websiteUrl", websiteUrl);
        bundle.putString("forumUrl", forumUrl);
        bundle.putInt("websiteIconRes", websiteIconResId);
        bundle.putInt("websiteLinkText", websiteLinkTextResId);
        bundle.putInt("websiteSummaryDesc", websiteSummaryDescriptionResId);
        return bundle;
    }

    /**
     * This function converts a compatible {@link Bundle} to {@link AboutBundle}.
     * If an non-compatible {@link Bundle} was passed instead of a compatible one, the resulting {@link AboutBundle}
     * would be initialised with default values for all member variables
     *
     * @param bundle - The {@link Bundle} to convert to {@link AboutBundle}
     * @return aboutBundle with values from bundle.
     */
    public static AboutBundle getAboutBundleFromBundle(Bundle bundle) {
        AboutBundle aboutBundle = new AboutBundle();
        aboutBundle.screenTitle = bundle.getString("title");
        aboutBundle.websiteUrl = bundle.getString("websiteUrl");
        aboutBundle.forumUrl = bundle.getString("forumUrl");
        aboutBundle.websiteIconResId = bundle.getInt("websiteIconRes");
        aboutBundle.websiteLinkTextResId = bundle.getInt("websiteLinkText");
        aboutBundle.websiteSummaryDescriptionResId = bundle.getInt("websiteSummaryDesc");
        return aboutBundle;
    }

    public String getScreenTitle() {
        return screenTitle;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getForumUrl() {
        return forumUrl;
    }

    public int getWebsiteIconResId() {
        return websiteIconResId;
    }

    public int getWebsiteLinkTextResId() {
        return websiteLinkTextResId;
    }

    public int getWebsiteSummaryDescriptionResId() {
        return websiteSummaryDescriptionResId;
    }
}
