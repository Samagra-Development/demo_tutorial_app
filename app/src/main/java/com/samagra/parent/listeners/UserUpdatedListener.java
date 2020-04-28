package com.samagra.parent.listeners;

public interface UserUpdatedListener {
    void onSuccess();
    void onFailure(Exception exception);
}
