
## BackgroundRemover library Overview

The BackgroundRemover library is a powerful and lightweight solution for removing backgrounds from images and applying multiple colors to the background. It's designed to simplify the process of isolating subjects in images, making it easy to create stunning visual effects or prepare images for various applications.


## Features

Background Removal: Efficiently removes the background from images, allowing you to focus on the main subject.

Color Fill: Fill the background with multiple colors to enhance visual appeal and customize the look of your images.

Easy Integration: Simple to integrate into your Android projects, providing a seamless experience for developers.

Versatile Use Cases: Ideal for applications such as photo editing, social media filters,Banking KYC and any scenario where background removal and color filling are required.


## Usage/Examples

To use this library, you must add the following permission to allow read and write to external storage. Refer to the sample app for a reference on how to start background remove with the right setup.

API >= 29

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32"/>

API >= 33

    <uses-permission android:name="android.permission.READ_MEDIA_VIDEO"/>





MainActivity:-

step-1

    BackgroundRemover remover=new BackgroundRemover(MainActivity.this, new BackgroundListner() {
            @Override
            public void onSuccess(Bitmap bitmap) {
              //you get the remove background image bitmp
            }

            @Override
            public void onFailure(Exception error) {
             //you get the error
            }
        });

step-2

    remover.process(imageBitmap,Color.WHITE);



## How to add to your project?
Step 1. Add the JitPack repository to your build file

    dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}


Step-2. Add the dependency in build.gradle

    dependencies { implementation 'com.github.AmarAndroidDev:Colour-Background-Remover-App:1.0.0' } 
## Demo
![Screenshot_2024-01-28-11-55-58-69_4c9ad60d90dfa5dd0501e88ee962b11a](https://github.com/AmarAndroidDev/ColourBackground_Remover/assets/135800230/db6849ec-ad06-4921-a2ad-9c39652eca65)

