# [MAGPIE](http://www.hevs.ch/en/mini-sites/projets-produits/aislab/projets/magpie-4826)
[![Build Status](https://travis-ci.org/aislab-hevs/magpie.svg)](https://travis-ci.org/aislab-hevs/magpie)

## Description
MAGPIE agent platform for Android. It include a module for the watch and the Smartphone

## Table of contents
- [Contents of MAGPIE] (#contents-of-magpie)
- [Required dependencies](#required-dependencies)
- [Quick start](#quick-start)
- [How to import the project in Android Studio](#how-to-import-the-project-in-android-studio)
- [Run the tests](#run-the-tests)
- [Documentation](#documentation)
- [Launch the project on Smartwatch](#smartwatch_project)
- [Copyright and license](#copyright-and-license)


## Contents of MAGPIE
- [library](https://github.com/aislab-hevs/magpie/tree/master/MAGPIE/library): the MAGPIE agent platform.
- [sample-debs](https://github.com/aislab-hevs/magpie/tree/master/MAGPIE/sample-debs): a demo application integrating the use of MAGPIE in a Distributed Event Based System.
- [sample-sensor](https://github.com/aislab-hevs/magpie/tree/master/MAGPIE/sample-sensor): a demo application showing how to use MAGPIE with a BioHarness sensor.
- [server](https://github.com/aislab-hevs/magpie/tree/master/MAGPIE/server): a Spring based server to be used with the sample-debs application. 

- [Watch](https://github.com/kflauri2312lffds/Android_watch_magpie/tree/master/MAGPIE/watch): the application for the watch

- [Smartphone](https://github.com/kflauri2312lffds/Android_watch_magpie/tree/master/MAGPIE/smartphone): the application in the phone, related to the watch


 
## Required dependencies
- [Andorid Studio & Android SDK](https://developer.android.com/sdk/installing/index.html?pkg=studio)
- [Gradle](http://www.gradle.org/installation)

The Android Build Tools version used is 25.0.0. It is necessary to install this version or change the version to the installed version in /library/build.gradle/ buildToolsVersion "25.0.0"

## Quick start
Two quick start options are available:

- [Download the latest release](https://github.com/aislab-hevs/magpie/archive/master.zip).
- Clone the reop: `git clone https://github.com/aislab-hevs/magpie.git`.

## How to import the project in Android Studio
- If no project is open in Android Studio:
  - Select "Open an existing Android Studio project"
  - Select the folder /magpie/MAGPIE/
  - Click on "OK"

- If an other project is already open in Android Studio:
  - Select File -> Import Project...
  - Select the folder /magpie/MAGPIE/
  - Click on "OK"

## Run the tests
- In Android Studio
  - Open the "Android" perspective
  - Right click on .ch.hevs.aislab.magpie.simpletest (androidTest) and select "Run" -> "All Tests"
  
  **OR**

  - Open the "Project" perspective
  - Right click on /app/src/androidTest/java/ and select "Run" -> "Run 'All Tests'"

- Command line
  - Switch to /magpie/MAGPIE/
  - Use the command to execute the tests: ./gradlew build connectedCheck

 For testing a running emulator or a connected device is needed.
 
 
 
## Smartwatch_project
To be able to launch the project on the smartwatch, 
- open the project on Android studio  (MASTER/MAGPIE)
-Create a emulator Android Wear 2.0 device OR use a real device (minimum version: 25)
-Activate the developper option
-Activate the debug by USB
-Launch the app via Android Studio (module: watch)

To connect the watch with the phone follow the tutorial
https://developer.android.com/training/wearables/apps/creating.html

## Smartphone_project
-Install first the app on the watch (see previous section)
-install the app on the phone (module: Smartphone)
-Don't forget the commande on Android studio prompt to be able to pair the phone with the watch :  adb -d forward tcp:5601 tcp:5601

## Watch_library
It's a module used in both Watch and Smartphone module.
  
## Copyright and license
Code and documentation copyright 2014-2015 [AISLab HES-SO Valais](http://www.hevs.ch/fr/mini-sites/projets-produits/aislab/). Code released under [the BSD license](https://github.com/aislab-hevs/magpie/blob/master/LICENSE).
