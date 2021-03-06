/*
 * Copyright 2018 The Android Open Source Project
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

package com.samagra.customworkmanager.impl.utils;

import androidx.annotation.RestrictTo;
import com.samagra.customworkmanager.WorkerParameters;
import com.samagra.customworkmanager.impl.WorkManagerImpl;

/**
 * A {@link Runnable} that can start work on the
 * {@link com.samagra.customworkmanager.impl.Processor}.
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class StartWorkRunnable implements Runnable {

    private WorkManagerImpl mWorkManagerImpl;
    private String mWorkSpecId;
    private WorkerParameters.RuntimeExtras mRuntimeExtras;

    public StartWorkRunnable(
            WorkManagerImpl workManagerImpl,
            String workSpecId,
            WorkerParameters.RuntimeExtras runtimeExtras) {
        mWorkManagerImpl = workManagerImpl;
        mWorkSpecId = workSpecId;
        mRuntimeExtras = runtimeExtras;
    }

    @Override
    public void run() {
        mWorkManagerImpl.getProcessor().startWork(mWorkSpecId, mRuntimeExtras);
    }
}
