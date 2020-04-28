package com.samagra.user_profile;

import androidx.annotation.NonNull;

import com.samagra.user_profile.profile.ProfileActivity;
import com.samagra.user_profile.profile.UserProfileElement;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link RuntimeException} indicating that a component is not configured Properly.
 *
 * @author Pranav Sharma
 */
public class InvalidConfigurationException extends RuntimeException {

    private final String message = "InvalidConfigurationException. Did you forget Configuring this activity ?";
    private String detailedMessage;
    private Class clazz;

    public InvalidConfigurationException(@NonNull Class clazz) {
        this.clazz = clazz;
        assignDetailedMessage();
    }

    @NotNull
    @Override
    public String toString() {
        return message + "\n Detailed Message: " + detailedMessage;
    }

    private void assignDetailedMessage() {
      if (clazz.getCanonicalName().equals(ProfileActivity.class.getCanonicalName())) {
            detailedMessage = ProfileActivity.class.getSimpleName() + " is not initialized. You need to pass UserProfileElement to configure ProfileActivity.";
        } else if (clazz.getCanonicalName().equals(UserProfileElement.class.getCanonicalName())) {
            detailedMessage = UserProfileElement.class.getSimpleName() + " is not initialized properly. For profileContentType 'SPINNER', you must pass a Non-Null and Non-Empty ArrayList<String> containing spinner entries.";
        } else {
            detailedMessage = "No further details Available.";
        }
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }
}
