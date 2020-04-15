## Drone Er4 :

Project, Geii : realisation of an app to control a drone.
Final objective : the drone has to follow the face on the user.
Project finished on april 15,2020.

## Original Work :

StarstreamDroneGEII-Upgrade2017

https://github.com/maxlabuche/StarstreamDroneGEII-Upgrade2017

Documentation:

```
AR Drone Developer Guide : https://projects.ardrone.org/wiki/ardrone-api/Developer_Guide
Open CV Documentation : http://docs.opencv.org/
Vitamio Documentation : https://www.vitamio.org/en/docs/
Handling Controler Actions : http://developer.android.com/training/game-controllers/controller-input.html

```
Authors :

```
This project has been made by a group of 5 students of the GEII (Electrical and Industrial Computing Engineering) department of Université Savoie Mont Blanc in France for their last year term project. This has been updated by another group of 6 students from the same university in 2017. They have given a name to the project : "Starstream".


Students (2017) :


BLANC Sofian
CAPALDI Timothée
MARMONT Maxime (@marmontm)
MASSON Thomas
PARIOT Valentin
PETTIER Loïc (@loicpettier)
Original students :

Bouguerra Bilel (@BilelBgr)
Dancre Antoine
Genoud Quentin
Nabhan Stephane (@BlackStef)
Ranarivelo Andre (@AJRdev)
Mentor :

Caron Bernard

```
License:

```
This project is licensed under the terms of the MIT license.

(January 2017)

```
## ChangeLog

* v3.4
    * last release.
    * changes APP :
      * FINAL FIX : Video receiving
      * ToDO : Fix display
      * FIX : Crashes
      
* v2
    * First commit

## Authors

* **Gaspard Misery** - *Geii student* - [GitHub](https://github.com/GaspardCtrl)
* **Kelian Sermet** - *Geii student* - [GitHub](https://github.com/KelianS)

### Installing

What things you need to install the software and how to run :

```
  Prerequisites : If app already instaled it's recomended to unistall or delete storage.
```

```
  Instalation : With Android Studio, nothing specific.
```

## Running the app

```
  Step one : Wait untill app sucesfully scan your bluetooth device. (and then click on it)
    If it does not : Try to restart the app to make sure you accepted the uses of localisation data.
                     Try to reScan using the `Scan` button from trop right corner.
                     Make sure the robot is turned on.
                     
  Step two : Wait a fiew time untill your device is properly connected to your phone. (pretty quick here)
    If it does not : Try to restart using the button from top right corner.
    If it still does not : Retry from step One.
                     
  Step three : Turn on the bluetooth helmet and then hit `Headset` button
  
  Step four : Wait again till helmet is connected.. 
    If it does not : Retry from step three. (again)
    If Helmet is connected and show no signal or state :  Restart from step One. (sorry). 
```

### Referencies :

Utilisation de :
https://github.com/codeminders/javadrone
https://github.com/manastech/javadrone/tree/master/javadrone-api
https://github.com/AutonomyLab/ardrone_autonomy/

https://developer.parrot.com/docs/groundsdk-android/index.html

https://developer.parrot.com/docs/refdoc-android/

https://developer.parrot.com/docs/groundsdk-android-samples/hello_drone.html

https://github.com/Parrot-Developers/groundsdk-android

https://developer.android.com/training/game-controllers/
