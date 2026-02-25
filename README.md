# Welcome to the Rygen Java & Vue Work Sample

This is the code sample by Edward Petruescu
<hr>

## Setting Up Your Environment

> This sample requires a host with Node.js and Java 17 installed. Tilt and docker. With Tilt it will download a redis and postgres container for deployment

I like using tilt, it makes things fun. 

 ```shell
  ./gradlew build -x test
 ```

- Check that you can run the frontend:

 ```shell
  cd light-controller-ui
  npm install
  npm run build
 ```
Run tilt in the base folder

 ```shell
  Tilt up
 ```

To teardown

 ```shell
  tilt down
 ```

## How the app works
This app is built with the idea that an Intersection has signal groups
Signal groups are assigned to phases

![img.png](img.png)

A signal group can just be North-south bound light. East-West. 
But they can also include Turn signal lights, pedestrian lights or any other signals that are required by the intersection.
An intersection can have n number of lights all on different sequences. Any overlap or how it physically looks is to be handled by the user

There can be emergency and overrides added but i refrained from doing so.

The idea is that street signals are created by trained professionals on the field usually with analog equipment (or analong like).
This app is just mimicking it with the UI pretty much acting like the controller

## How the scheduling works

This app is designed for a multipod system. it would work better with virtual threads but that was only introduced in java 21

When an intersection starts, the thread tries getting the lock for the intersection.
It then Schedules it to be ran every second instead of dynamically triggering updates as having a thread go through it is safer execution wise
When a thread starts up the ownership cycle it checks if it has leadership over the intersection, and if the intersection is still active.
If its all good then it moves forward with actually changing lights and going through the phases.
During a phase it will change all the lights for the signal groups attached to it
using a mod operator for sequences in a phase, it determines if it goes to the next phase

## Architecture changes configurations
I originally wanted to have cascading changes in postgresql  but im honestly more used to a no sql system with manual changes.

As a app, i didnt want to limit the user/trained professional so it was built in that manner.
A tilt setup mimics what a deployment would look like and keeps implementation wise simple.

I used OpenApi client generator to get the json from the controllers so i didnt have to manually create every object.

Redis also has an auto pilot for leadership where it will renew automatically so i cleaned that up.

## Addressing each required feature

>Turning on lights at an intersection

The UI has an activate button. When an intersection is modified to be active then the scheduling starts.

> Turning off lights at an intersection
 
Intersection can be updated to be inactive. Stop message gets set to the worker service and the worker service also checks to see if it is still active

>Updating light configuration

Only able to update inactive intersection. Doesnt make sense to modify a currently running light. When the intersection starts back up it will use the new params

>Retrieving light configuration

Selecting an intersection or calling the controller gets all relevant info for that intersection

>Ensuring light synchronization within an intersection

Done via phases object. Only one phase is active at a time. User sets up the lights themselves. 
I considered having a light conflict detector but on thinking through the use cases u can have so many different kinds of lights that it was out of the scope time wise to do.
