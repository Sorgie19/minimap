#!/bin/bash
#Parsing Mac Addresses
timeout 60s ./probe-sniff-command.sh
java MacAddressParser /root/sniff-probes/probes.txt /root/sniff-probes/uniqueProbes.txt
#Upload to database
./insertDataToDatabase.sh
#delete file
rm probes.txt
rm uniqueProbes.txt
