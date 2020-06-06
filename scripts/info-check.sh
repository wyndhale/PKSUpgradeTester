#!/bin/bash

# set username and password
PROTOCOL=http
HOSTNAME=trading.home
PORT=30500
MAXTIMEOUT=1.0
CONNTIMEOUT=1.0

while :
do 
   # get pod name from info
   PODNAME=$(curl -s -m ${MAXTIMEOUT} --connect-timeout ${CONNTIMEOUT} ${PROTOCOL}://${HOSTNAME}:${PORT}/actuator/info | jq -r .hostname)
   echo ${PODNAME}
   sleep 1
done
