# gaze_calibration

## Introduction
Allow user to sequentially take 9 photos while the user is staring at 9 different positions on the screen. We use hardware.camera2 device here because it gives us 
more control over the camera comparing to graphics.camera, which only allows us to use original camera capturing window and requires user interaction.
We use auto-focus and auto-exposure in our capture session here, and display the captured result alive in a small window.  

## Objective
These 9 images will form the base vectors for later gaze recognition. Specifically, we will crop out the eye sections and store the pixels for each photo.
Since the photos are taken when the user stares at nine spots that completely cover the screen, we now can use superposition to determine user's gaze at any direction.

## Screen Shot
![picture alt](https://github.com/YuansongFeng/gaze_calibration/blob/master/screeshots/gaze1.jpg)
![picture alt](https://github.com/YuansongFeng/gaze_calibration/blob/master/screeshots/gaze2.jpg)
![picture alt](https://github.com/YuansongFeng/gaze_calibration/blob/master/screeshots/gaze3.jpg)
