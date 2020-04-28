package com.samagra.ancillaryscreens;

import androidx.annotation.NonNull;

import com.samagra.ancillaryscreens.screens.about.AboutActivity;

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
        if (clazz.getCanonicalName().equals(AboutActivity.class.getCanonicalName())) {
            detailedMessage = AboutActivity.class.getSimpleName() + " is not initialized. You need to pass AboutBundle to configure AboutActivity.";
        }else {
            detailedMessage = "No further details Available.";
        }
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }
}
