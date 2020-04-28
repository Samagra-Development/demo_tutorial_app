package com.samagra.ancillaryscreens.screens.about;

import android.content.Context;

import androidx.annotation.NonNull;

import com.samagra.customworkmanager.Data;
import com.samagra.customworkmanager.Worker;
import com.samagra.customworkmanager.WorkerParameters;


public class SampleWorker extends Worker {

    // Define the parameter keys:
    public static final String KEY_X_ARG = "X";
    public static final String KEY_Y_ARG = "Y";
    public static final String KEY_Z_ARG = "Z";

    // ...and the result key:
    public static final String KEY_RESULT = "result";

    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * Override this method to do your actual background processing.  This method is called on a
     * background thread - you are required to <b>synchronously</b> do your work and return the
     * {@link Result} from this method.  Once you return from this
     * method, the Worker is considered to have finished what its doing and will be destroyed.
     * <p>
     * A Worker is given a maximum of ten minutes to finish its execution and return a
     * {@link Result}.  After this time has expired, the Worker will
     * be signalled to stop.
     *
     * @return The {@link Result} of the computation; note that
     * dependent work will not execute if you use
     * {@link Result#failure()} or
     * {@link Result#failure(Data)}
     */
    @NonNull
    @Override
    public Result doWork() {

        // Fetch the arguments (and specify default values):
        int x = getInputData().getInt(KEY_X_ARG, 0);
        int y = getInputData().getInt(KEY_Y_ARG, 0);
        float z = getInputData().getFloat(KEY_Z_ARG, 0);

        // ...do the math...
        double result = myCrazyMathFunction(x, y, z);

        //...set the output, and we're done!
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Data output = new Data.Builder()
                .putDouble(KEY_RESULT, result)
                .build();

        return Result.success(output);
    }

    private double myCrazyMathFunction(int x, int y, float z) {
        double sum = 0;
        for (float i = x; i < y; i += z) {
            sum += i;
        }
        return sum;
    }
}
