#!/bin/bash

for ((i = 1; i < 17; i++))
do
	scp -o stricthostkeychecking=no internetpasswords/internet$i.txt sdcadmin@sdc$i.local:/home/sdcadmin/password.txt
done
