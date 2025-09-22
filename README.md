# 🚀 Xposed Module Template

A clean, ready-to-use template for building Xposed Framework modules with modern Android development tools.

## 📋 Overview

This project provides a minimal, properly configured Xposed module template that you can use as a starting point for your own Xposed modules. It includes all the necessary configuration and boilerplate code to get you up and running quickly.

## ✨ Features

- ✅ **Modern Gradle Setup** - Uses Gradle Kotlin DSL (`.gradle.kts`)
- ✅ **Proper Dependencies** - Correctly configured Xposed API with `compileOnly` scope
- ✅ **Clean Architecture** - Separate module class from Android Activity
- ✅ **Ready to Build** - No additional setup required
- ✅ **Example Hook** - Simple package load logging demonstration
- ✅ **Android Studio Compatible** - Works with latest Android Studio versions

## 🛠️ Getting Started

### Prerequisites

- Android Studio (latest version recommended)
- Android SDK with API level 24 or higher
- Device with Xposed Framework installed (rooted device or emulator)

### Setup

1. **Clone this repository**
   ```bash
   git clone https://github.com/yourusername/xposed-module-template.git
   cd xposed-module-template
   ```

2. **Open in Android Studio**
   - File → Open → Select the project directory
   - Wait for Gradle sync to complete

3. **Customize the module**
   - Update `app/build.gradle.kts` with your app details
   - Modify `AndroidManifest.xml` metadata (app name, description)
   - Edit `XposedModule.java` to implement your hooks

4. **Build and install**
   ```bash
   ./gradlew assembleDebug
   ```

## 📁 Project Structure

```
app/src/main/
├── assets/
│   └── xposed_init              # Entry point for Xposed Framework
├── java/com/example/xposedtest/
│   ├── MainActivity.java        # Regular Android Activity
│   └── XposedModule.java        # Xposed hook implementation
└── AndroidManifest.xml          # Module metadata and permissions
```

## 🔧 Key Files Explained

### `XposedModule.java`
```java
public class XposedModule implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("Test Module - Package loaded: " + lpparam.packageName);
    }
}
```
This is where your Xposed hooks go. Implement different interfaces for various hook types.

### `xposed_init`
```
com.example.xposedtest.XposedModule
```
Tells Xposed Framework which class contains your module implementation.

### `AndroidManifest.xml`
Contains essential Xposed metadata:
- `xposedmodule` - Marks this as an Xposed module
- `xposeddescription` - Module description shown in Xposed Installer
- `xposedminversion` - Minimum Xposed API version required

## 🎯 Common Hook Types

Extend your module by implementing additional interfaces:

| Interface | Purpose |
|-----------|---------|
| `IXposedHookLoadPackage` | Hook into app loading |
| `IXposedHookZygoteInit` | Hook into system startup |
| `IXposedHookInitPackageResources` | Hook into resource loading |

## 📝 Example Hooks

### Method Hooking
```java
XposedHelpers.findAndHookMethod("com.example.TargetClass",
    lpparam.classLoader, "methodName",
    String.class, // parameter types
    new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            // Code before original method
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            // Code after original method
        }
    });
```

### Constructor Hooking
```java
XposedHelpers.findAndHookConstructor("com.example.TargetClass",
    lpparam.classLoader, String.class,
    new XC_MethodHook() {
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            // Constructor hook logic
        }
    });
```

## 🚨 Important Notes

- **Dependencies**: The Xposed API is marked as `compileOnly` to prevent it from being bundled into your APK
- **Testing**: Test your module thoroughly on different Android versions and devices
- **Compatibility**: Always check target app compatibility when hooking specific apps
- **Security**: Be mindful of what data you access and how you handle it

## 🐛 Troubleshooting

### Module not loading
- Check if module is enabled in Xposed Installer
- Verify `xposed_init` file contains correct class path
- Check Xposed logs for error messages

### ClassNotFoundException
- Ensure Xposed API is marked as `compileOnly` in build.gradle
- Verify the target class exists in the hooked app

### Build errors
- Clean and rebuild: `./gradlew clean build`
- Check Android Studio sync status
- Verify SDK and build tools versions

## 📚 Resources

This template is based on the official Xposed documentation:
- [Xposed Development Tutorial](https://github.com/rovo89/xposedbridge/wiki/development-tutorial)
- [Xposed Framework API](https://github.com/rovo89/XposedBridge/wiki/Using-the-Xposed-Framework-API)
- [Xposed API Reference](https://api.xposed.info/)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit issues and pull requests to improve this template.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## ⚠️ Disclaimer

This template is for educational and development purposes. Use Xposed modules responsibly and respect app developers' terms of service. The authors are not responsible for any misuse of this template.

---

**Happy Hooking!** 🎣