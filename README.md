# Pathfinder with A*

This repository contains some of the work done for the application.  Unfortuently, some of the files to the project have been lost with time. 

Architecture and Data flow:

The Android application architecture starts from the embedded layer, where accleometer, linear accleration, and gyroscope data is initally collected.  The data is used to render new values and postions of the TextView and MapView objects. Next the PathFinder object is invoked and passed the updated coordiantes of the user point from the MapView object.  The PathFinder then uses the A* pathfinding algorithm to calculate the shortest path between the current user node and the final destination.  The new route is displayed and the process repeats as new sensor data is available. 
