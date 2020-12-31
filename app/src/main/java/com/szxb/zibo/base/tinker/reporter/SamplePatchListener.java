/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.szxb.zibo.base.tinker.reporter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.szxb.lib.Util.MiLog;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.base.tinker.BuildInfo;
import com.szxb.zibo.base.tinker.Utils;
import com.szxb.zibo.base.tinker.crash.SampleUncaughtExceptionHandler;
import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;
import java.util.Properties;


/**
 * Created by zhangshaowen on 16/4/30.
 * optional, you can just use DefaultPatchListener
 * we can check whatever you want whether we actually send a patch request
 * such as we can check rom space or apk channel
 */
public class SamplePatchListener extends DefaultPatchListener {
    private static final String TAG = "升级";

    protected static final long NEW_PATCH_RESTRICTION_SPACE_SIZE_MIN = 60 * 1024 * 1024;

    private final int maxMemory;

    public SamplePatchListener(Context context) {
        super(context);
        maxMemory = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        MiLog.i(TAG, "SamplePatchListener application maxMemory:" + maxMemory);
    }

    /**
     * because we use the defaultCheckPatchReceived method
     * the error code define by myself should after {@code ShareConstants.ERROR_RECOVER_INSERVICE
     *
     * @param path
     * @param newPatch
     * @return
     */
    @Override
    public int patchCheck(String path, String patchMd5) {
        File patchFile = new File(path);
        MiLog.i(TAG, "patchCheck  补丁路径：" + path + "    补丁包大小：" + SharePatchFileUtil.getFileOrDirectorySize(patchFile));
        int returnCode = super.patchCheck(path, patchMd5);
        MiLog.i(TAG, "patchCheck  md5校验返回:" + returnCode + "          patchMd5:" + patchMd5);
        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            returnCode = Utils.checkForPatchRecover(NEW_PATCH_RESTRICTION_SPACE_SIZE_MIN, maxMemory);
        }
        MiLog.i(TAG, "patchCheck  检测包大小是否超限:" + returnCode);
        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            SharedPreferences sp = context.getSharedPreferences(ShareConstants.TINKER_SHARE_PREFERENCE_CONFIG, Context.MODE_MULTI_PROCESS);
            //optional, only disable this patch file with md5
            int fastCrashCount = sp.getInt(patchMd5, 0);
            if (fastCrashCount >= SampleUncaughtExceptionHandler.MAX_CRASH_COUNT) {
                returnCode = Utils.ERROR_PATCH_CRASH_LIMIT;
            }
        }
        MiLog.i(TAG, "patchCheck  checkcrash返回:" + returnCode);
        // Warning, it is just a sample case, you don't need to copy all of these
        // Interception some of the request
        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            Properties properties = ShareTinkerInternals.fastGetPatchPackageMeta(patchFile);
            if (properties == null) {
                returnCode = Utils.ERROR_PATCH_CONDITION_NOT_SATISFIED;
            } else {
                String platform = properties.getProperty(Utils.PLATFORM);
                MiLog.i(TAG, "patchCheck  get platform:" + platform);
                // check patch platform require
                if (platform == null || !platform.equals(BuildInfo.PLATFORM)) {
                    returnCode = Utils.ERROR_PATCH_CONDITION_NOT_SATISFIED;
                }
            }
        }
        MiLog.i(TAG, "patchCheck  fastGetPatchPackageMeta返回:" + returnCode);
        SampleTinkerReport.onTryApply(returnCode == ShareConstants.ERROR_PATCH_OK);

        String[] str = BusApp.getInstance().getPakageVersion().split(".");
        int apkFirstVersion = 0;
        int apkSencondVersion = 0;
        try {
            apkFirstVersion = Integer.parseInt(str[0]);
            apkSencondVersion = Integer.parseInt(str[1]);
        } catch (Exception e) {

        }
        if (apkFirstVersion >= 1 && apkSencondVersion >= 5 && !BusApp.getPosManager().getIsClean()) {
            BusApp.getInstance().cleanParch();
        }


        return returnCode;
    }
}
