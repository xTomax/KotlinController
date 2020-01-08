# KotlinController
SlotCar controller in kotlin (basic functionalities). Works by connection to an esp8266 running micropython with a Telnet server (https://github.com/cpopp/MicroTelnetServer) running to allow simple TCP connections and commands to console (imports, and speed changes).
From Android connection (WIFI) to ESP8266 in AP mode is needed and commands shall be sent directly using TCP connection.
Hardware used will come in time, for now I'm just using a voltage regulator 15V to 5V in order to power the esp directly from power input. And a SW3205 to contol the Ground on/off to the track.
