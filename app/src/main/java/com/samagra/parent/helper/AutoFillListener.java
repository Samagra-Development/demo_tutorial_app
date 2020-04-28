package com.samagra.parent.helper;

/**
 * Interface for callback invocation on an application action
 */
public interface AutoFillListener {
    /**
     * The operation succeeded
     */
    void onAutoFillSuccess(String formName, int id);

    /**
     * The operation failed
     *
     * @param exception which caused the failure
     */
    void onAutoFillFailure(Exception exception);
}

