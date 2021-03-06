/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samagra.customworkmanager;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.UUID;

/**
 * Updates progress for a {@link com.samagra.customworkmanager.ListenableWorker}.
 */
public interface ProgressUpdater {

    /**
     * @param context The application {@link Context}.
     * @param id      The {@link UUID} identifying the {@link ListenableWorker}
     * @param data    The progress {@link Data}
     * @return The {@link ListenableFuture} which resolves after progress is persisted.
     * Cancelling this future is a no-op.
     */
    @NonNull
    ListenableFuture<Void> updateProgress(
            @NonNull Context context,
            @NonNull UUID id,
            @NonNull Data data);
}
