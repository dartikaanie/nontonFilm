package com.anie.dara.nontonfilm;

import android.database.Cursor;

public interface LoadCallback {
    void preExecute();

    void postExecute(Cursor films);
}
