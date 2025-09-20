# 🎲Random Background
## Description
This is an app that is designed to change your system's desktop wallpaper to a random image at a specified interval.
The settings of the app are controlled by a configuration file, which will eventually be manipulated through a GUI. 
The configuration file allows you to specify whether the random image comes from a directory on your system or from an API endpoint. It also allows you to specify how often the background should change.

## ✨Features
✅Current Features:
- Random image from a directory
- Change wallpaper every x minutes
- Settings are determined by a config file
- The config file can be manipulated through the Config class

🚧Coming Soon:
- Random image from API
- Cross Platform functionality (assuming I have access to good VMs)
- GUI to control app/config file

## 🛠️Technologies Used
- **Java** - Core logic
- **Maven** - Dependency and build management
- **Java Native Access** - System-level wallpaper manipulation
