package com.samagra.ancillaryscreens.screens.passReset;

public interface ChangePasswordActionListener {
    /**
     * The operation succeeded
     */
    void onSuccess();

    /**
     * The operation failed
     *
     * @param exception which caused the failure
     */
    void onFailure(Exception exception);
}


