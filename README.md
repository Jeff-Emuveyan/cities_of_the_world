# Cities of the World

This application displays a list of all cities in the world and allows users to see the exact location of these cities on a map.

<img src="https://firebasestorage.googleapis.com/v0/b/firestore-b9d53.appspot.com/o/Screenshot_20220411-100954_citiesoftheworld.jpg?alt=media&token=1bc3c19c-3d33-4c8c-bfd5-c11e3fa86c59" width="300" height="650">

The project has three modules:
1) ```app```: This is the container module of the application. its sole duty is to display all features of the application
2) ```core```: The core module serves as the base module. This is the module all other modules depend on and get thier dependencies from. 
3) ```cities```: This module serves like a feature module. It contains the logic and views to load cities on a recycler view and map view.

## Dependencies
- Room
- Retrofit
- Hilt
- Mockito
- Coroutines
- Nav graph
- Flows

## Architecture
MVVM

## Units Tests
The code base contains both **Local unit tests** and **Instrumented unit tests**. These can be found inside the ```cities``` module.
