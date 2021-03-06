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

package com.samagra.customworkmanager.impl;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.samagra.customworkmanager.impl.model.WorkSpec;
import com.samagra.customworkmanager.impl.model.WorkTypeConverters;
import com.samagra.customworkmanager.impl.utils.Preferences;

/**
 * Migration helpers for {@link com.samagra.customworkmanager.impl.WorkDatabase}.
 *
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class WorkDatabaseMigrations {

    private WorkDatabaseMigrations() {
        // does nothing
    }

    // Known WorkDatabase versions
    public static final int VERSION_1 = 1;
    public static final int VERSION_2 = 2;
    public static final int VERSION_3 = 3;
    public static final int VERSION_4 = 4;
    public static final int VERSION_5 = 5;
    public static final int VERSION_6 = 6;
    public static final int VERSION_7 = 7;

    private static final String CREATE_SYSTEM_ID_INFO =
            "CREATE TABLE IF NOT EXISTS `SystemIdInfo` (`work_spec_id` TEXT NOT NULL, `system_id`"
                    + " INTEGER NOT NULL, PRIMARY KEY(`work_spec_id`), FOREIGN KEY(`work_spec_id`)"
                    + " REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )";

    private static final String MIGRATE_ALARM_INFO_TO_SYSTEM_ID_INFO =
            "INSERT INTO SystemIdInfo(work_spec_id, system_id) "
                    + "SELECT work_spec_id, alarm_id AS system_id FROM alarmInfo";

    private static final String PERIODIC_WORK_SET_SCHEDULE_REQUESTED_AT =
            "UPDATE workspec SET schedule_requested_at=0"
                    + " WHERE state NOT IN " + WorkTypeConverters.StateIds.COMPLETED_STATES
                    + " AND schedule_requested_at=" + WorkSpec.SCHEDULE_NOT_REQUESTED_YET
                    + " AND interval_duration<>0";

    private static final String REMOVE_ALARM_INFO = "DROP TABLE IF EXISTS alarmInfo";

    private static final String WORKSPEC_ADD_TRIGGER_UPDATE_DELAY =
            "ALTER TABLE workspec ADD COLUMN `trigger_content_update_delay` INTEGER NOT NULL "
                    + "DEFAULT -1";

    private static final String WORKSPEC_ADD_TRIGGER_MAX_CONTENT_DELAY =
            "ALTER TABLE workspec ADD COLUMN `trigger_max_content_delay` INTEGER NOT NULL DEFAULT"
                    + " -1";

    private static final String CREATE_WORK_PROGRESS =
            "CREATE TABLE IF NOT EXISTS `WorkProgress` (`work_spec_id` TEXT NOT NULL, `progress`"
                    + " BLOB NOT NULL, PRIMARY KEY(`work_spec_id`), FOREIGN KEY(`work_spec_id`) "
                    + "REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )";

    /**
     * Removes the {@code alarmInfo} table and substitutes it for a more general
     * {@code SystemIdInfo} table.
     * Adds implicit work tags for all work (a tag with the worker class name).
     */
    public static Migration MIGRATION_1_2 = new Migration(VERSION_1, VERSION_2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(CREATE_SYSTEM_ID_INFO);
            database.execSQL(MIGRATE_ALARM_INFO_TO_SYSTEM_ID_INFO);
            database.execSQL(REMOVE_ALARM_INFO);
            database.execSQL("INSERT OR IGNORE INTO worktag(tag, work_spec_id) "
                    + "SELECT worker_class_name AS tag, id AS work_spec_id FROM workspec");
        }
    };

    /**
     * A {@link WorkDatabase} migration that reschedules all eligible Workers.
     */
    public static class WorkMigration extends Migration {
        final Context mContext;

        public WorkMigration(@NonNull Context context, int startVersion, int endVersion) {
            super(startVersion, endVersion);
            mContext = context;
        }

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Preferences preferences = new Preferences(mContext);
            preferences.setNeedsReschedule(true);
        }
    }

    /**
     * Marks {@code SCHEDULE_REQUESTED_AT} to something other than
     * {@code SCHEDULE_NOT_REQUESTED_AT}.
     */
    public static Migration MIGRATION_3_4 = new Migration(VERSION_3, VERSION_4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            if (Build.VERSION.SDK_INT >= WorkManagerImpl.MIN_JOB_SCHEDULER_API_LEVEL) {
                database.execSQL(PERIODIC_WORK_SET_SCHEDULE_REQUESTED_AT);
            }
        }
    };

    /**
     * Adds the {@code ContentUri} delays to the WorkSpec table.
     */
    public static Migration MIGRATION_4_5 = new Migration(VERSION_4, VERSION_5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(WORKSPEC_ADD_TRIGGER_UPDATE_DELAY);
            database.execSQL(WORKSPEC_ADD_TRIGGER_MAX_CONTENT_DELAY);
        }
    };

    /**
     * Adds {@link com.samagra.customworkmanager.impl.model.WorkProgress}.
     */
    @NonNull
    public static Migration MIGRATION_6_7 = new Migration(VERSION_6, VERSION_7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(CREATE_WORK_PROGRESS);
        }
    };
}
