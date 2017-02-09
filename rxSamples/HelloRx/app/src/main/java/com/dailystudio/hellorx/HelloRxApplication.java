package com.dailystudio.hellorx;

import com.dailystudio.app.DevBricksApplication;

/**
 * Created by nanye on 17/2/9.
 */

public class HelloRxApplication extends DevBricksApplication {

    @Override
    protected boolean isDebugBuild() {
        return BuildConfig.DEBUG;
    }

}
