
BackgroundRemover Library
Overview:
The BackgroundRemovalAndColorFill library is a powerful and lightweight solution for removing backgrounds from images and applying multiple colors to the background. It's designed to simplify the process of isolating subjects in images, making it easy to create stunning visual effects or prepare images for various applications.

Features:
Background Removal:
Efficiently removes the background from images, allowing you to focus on the main subject.

Color Fill:
Fill the background with multiple colors to enhance visual appeal and customize the look of your images.

Easy Integration:
Simple to integrate into your Android projects, providing a seamless experience for developers.

Versatile Use Cases:
Ideal for applications such as photo editing, social media filters, and any scenario where background removal and color filling are required.

How to add to your project?

Step 1.
Add the JitPack repository to your build file


dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
Step-2.
Add the dependency in build.gradle


dependencies {
	        implementation 'com.github.AmarAndroidDev:Grocery-App:Tag'
	}
 Step-3.
 Add permission in manifest file.
 
 API < 29
 
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission
    android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="28"
    tools:ignore="ScopedStorage" />
API >= 29

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32"/>

API >= 33
 <uses-permission android:name="android.permission.READ_MEDIA_VIDEO"/>
 Step-4.
new BackgroundRemover(MainActivity.this, new BackgroundListner() {
            @Override
            public void onSuccess(Bitmap bitmap) {
              //you get the remove bitmap image
            }
            @Override
            public void onFailure(Exception error) {
               ///you get the error
            }
        });

            

  
