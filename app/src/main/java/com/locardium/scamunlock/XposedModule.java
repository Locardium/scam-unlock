package com.locardium.scamunlock;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedModule implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (!"com.sec.android.app.camera".equals(lpparam.packageName)) return;

        ClassLoader cl = lpparam.classLoader;

        // DimUpdaterMap
        Class<?> dimUpdaterClass = XposedHelpers.findClass(
                "com.sec.android.app.camera.setting.repository.DimUpdaterMap",
                cl
        );

        XC_MethodHook cancelDimHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                param.setResult(null);
            }
        };

        XposedBridge.hookAllMethods(dimUpdaterClass, "updateZoomValueDim", cancelDimHook);
        XposedBridge.hookAllMethods(dimUpdaterClass, "updateBackCameraVideoLensTypeDim", cancelDimHook);
        XposedBridge.hookAllMethods(dimUpdaterClass, "updateProLensTypeDim", cancelDimHook);

        // VideoZoomController: video fix
        Class<?> videoZoomClass = XposedHelpers.findClass(
                "com.sec.android.app.camera.shootingmode.video.VideoZoomController",
                cl
        );

        XposedBridge.hookAllMethods(
                videoZoomClass,
                "handleZoomValueChanged",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        param.setResult(null);
                    }
                }
        );

        XposedBridge.hookAllMethods(
                videoZoomClass,
                "isTorchOnDuringRecording",
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) {
                        return false;
                    }
                }
        );

        // ZoomController: always allow come back to wide
        Class<?> zoomControllerClass = XposedHelpers.findClass(
                "com.sec.android.app.camera.engine.ZoomController",
                cl
        );

        XposedBridge.hookAllMethods(
                zoomControllerClass,
                "isZoomToWideLensAvailable",
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) {
                        return true;
                    }
                }
        );

        // AeAfManagerImpl: enable torch without checks
        Class<?> aeAfClass = XposedHelpers.findClass(
                "com.sec.android.app.camera.engine.AeAfManagerImpl",
                cl
        );

        XposedBridge.hookAllMethods(
                aeAfClass,
                "handleBackTorchSettingChanged",
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) {
                        Object thiz = param.thisObject;
                        if (param.args != null && param.args.length > 0 && param.args[0] instanceof Integer) {
                            int mode = (Integer) param.args[0];
                            XposedHelpers.callMethod(thiz, "setTorchFlashMode", mode);
                        }
                        return null;
                    }
                }
        );
    }
}