# TindraTracker

![iPhone Retina](https://raw.githubusercontent.com/mstahv/tindratracker/master/iphone-screenshot.png "Mobile tracking app")

Simple GPS tracking application using the following technologies:

 * Java 8 & JPA
 * Spring Boot, http://projects.spring.io/spring-boot/
 * Vaadin 7, https://vaadin.com/download
 * Vaadin TouchKit, https://vaadin.com/touchkit
 * GeoTools, http://www.geotools.org/
 * Leaflet and v-leaflet, http://vaadin.com/addon/v-leaflet

For tracking device we use TK102-2 GSM/GPS tracker with logging over TCP/IP.


## Building the project

First, make sure you have the following tools installed:
 * Java 8 JDK
 * Maven
 * Git

Use the following commands to clone the GitHub project and run the Maven build:
```
git clone https://github.com/mstahv/tindratracker.git
cd tindratracker/
mvn install
```

Once completed an executable jar file is created. Run that
```
cd target
java -jar tindratracker-0.0.10-SNAPSHOT.jar
```

You can now access the web server at http://localhost:9090

## Configuring the TK201 GPS Tracker device

To configure the TK102 to use the GPRS reporting with your server,
send the following SMS configuration commands to the device:

```
# Set GPRS accesspoint for TCP logging
sms> apn123456 <your_internet_accesspoint>

# Configure server and port
sms> adminip123456 <your_server_ip> <your_server_port>

# Setting autotrack on 5sec interval
sms> t005s***n123456

```
Notes:
 - Here the `123456` is the default password of the TK102 device and if you have changed it, you should use one you chose.
 - You should adjust the reporting interval to be meaning for you. Typically 5s interval eats the device battery in 4-6 hours.
 - The default server port is `50123`. You can change that in the file `org.vaadin.tindra.Server`.
