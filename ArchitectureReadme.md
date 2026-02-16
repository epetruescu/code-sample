deployed in a kubernetes enviroment.
PostgreSQL
Intersection with 2 pairs of stop lights.
each pair would be synced
Example:
1st street. North to south
Washington Street, East to West
A pair can have a dedicated turning lane or not
Timer set for green, and yellow. Red would depend on the other pair of lights
Technically yellow depends on the speed of traffic but will ignore that for this sample and just have a user set or a default time
Green will be user set or will use the default value

User creates an intersection. Provides names and direction
most fields will be defaulted
On saving a intersection, an id will be generated and returned to the user.
On save, a task will start for the lights and will continue until stopped.
Should have some kind of lock where only one thread can modify the lights at a time, though i think locking a light running taksk would be good.
User can stop the intersection light
On booting the application, the lights start up. 
Maybe the main object can be the intersection and then another object would be the pair of street lights. 
A hard stop of two pairs. Startup would just be on the order of the light pairs.

The Ui page would start off getting the list of intersections. would say if the intersection is active or not
Polling for the intersection status would be easiest but a websocket would be fun
Polling would just get the status of all the lights.
Websocket would just tell the UI when a status has changed

Redis would be good for locks but a manual lock would work. Can also just have a local cache since this is a small project but i would like to run 2 pods like usual.

Redis has pub/sub which would help for websockets over multiple pods.