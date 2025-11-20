# Process Limit (Android TV)

Process Limit is a lightweight Android TV utility that modifies the system setting `max_cached_processes` and forces the background process limit to **0**, preventing apps from staying cached in memory.  
This tool can improve performance on low-memory Android TV devices by reducing RAM usage and eliminating unnecessary background tasks.

---

## 🛠 What This App Does

Android controls cached background processes using the following global system key:

```
activity_manager_constants > max_cached_processes
```

This application writes the following value to the system:

```
max_cached_processes=0
```

This results in:
- No cached background processes
- Reduced memory pressure
- A smoother launcher/UI experience on low-RAM TV devices

---

## 🔐 Permission Requirement

This app requires **WRITE_SECURE_SETTINGS**, a system-level permission.  
It **will not work** until the permission is manually granted using ADB.

---

## 📦 Installation Guide

### 1) Install the APK

```bash
adb install ProcessLimit.apk
```

### 2) Grant Secure Permission

```bash
adb shell pm grant com.yourdomain.processlimit android.permission.WRITE_SECURE_SETTINGS
```

> **Note:** Replace `com.yourdomain.processlimit` if your package name is different.

### 3) Open the App via ADB (Optional)

```bash
adb shell monkey -p com.yourdomain.processlimit 1
```

---

## ⚠ Important Notes

- Setting the background limit to **0** will close tasks immediately when you leave them.
- Foreground apps (e.g., YouTube, Netflix, video players) **will not be affected**.
- If your TV vendor restricts secure permissions, functionality may vary.

---

## 🔧 Reversing the Change

If you want to reset to default behavior:

```bash
adb shell settings delete global activity_manager_constants
```

---

## 📄 License

This project is distributed under the **GNU General Public License v3 (GPLv3)**.  
See the `LICENSE` file for details.

```
© 2025 Mahmut Alperen Ünal
```