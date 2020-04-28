package com.samagra.user_profile.contracts;

import java.io.File;

public interface OverrideUploadFileCallback {
    void sendAppLogsToServer(File file);
}