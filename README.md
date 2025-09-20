# 🎲Random Background
## Description
This is an app that is designed to change your system's desktop wallpaper to a random image at a specified interval.
The settings of the app are controlled by a configuration file, which will eventually be manipulated through a GUI. 
The configuration file allows you to specify whether the random image comes from a directory on your system or from an API endpoint. It also allows you to specify how often the background should change.

## ✨Features
✅Current Features:
- Random image from a directory
- Change wallpaper every x minutes
- A config file determines settings
- The config file can be manipulated through the Config class

## User-Guide
Currently, everything is controlled through the configuration file.
Config File:
- changeInterval: Time in minutes between each time of changing the background image.
- imagePath: When in directory mode, this is the path to the directory containing the background images you'd like to choose from (e.g. C://User/[user]/Pictures). When in API mode, this is the URL to the API you would like to call. Note that the API must return an image file.
  - Example APIs: https://picsum.photos/1920/1080 or https://random.danielpetrica.com/api/random
- imageSource: How you would like to source your random image (directory, API)

🚧Coming Soon:
- Cross Platform functionality (assuming I have access to good VMs)
- GUI to control app/config file

## 🛠️Technologies Used
- **Java** - Core logic
- **Maven** - Dependency and build management
- **Java Native Access** - System-level wallpaper manipulation
