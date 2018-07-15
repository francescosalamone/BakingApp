# Baking App project
The fourth exercise of the Android Developer Nanodegree Program by Udacity
    
## Main target
The main target for this app is to:
* Use MediaPlayer/Exoplayer to display videos.
* Handle error cases in Android.
* Add a widget to your app experience.
* Leverage a third-party library in your app.
* Use Fragments to create a responsive design that works on phones and tablets.

## App description
Create a Android Baking App that will allow Udacity’s resident baker-in-chief, Miriam, to share her recipes with the world. You will create an app that will allow a user to select a recipe and see video-guided steps for how to complete it.

## Configuration
In case an image url is missing from the json, the Unsplash service is used for show a related picture from their database.
For use this service, the Unsplash Api is required. For get the api, you need to register a new account (https://unsplash.com/developer), add a new project and get you api.
Once you have the api key, write it in your gradle.properties file as below: 

unsplashApi="HERE_YOUR_API_KEY"

be sure to enclose the api key between the double quotes.
For every question, follow the official guide (https://unsplash.com/documentation)
