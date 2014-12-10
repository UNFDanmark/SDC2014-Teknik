#!/bin/bash

# Kopir keys
bash SudoMassDo.sh 'rm /home/sdcadmin/Desktop/id_rsa && rm /home/sdcadmin/Desktop/id_rsa.pub'

for ((i = 1; i < 17; i++))
do
	scp /Keys/id_rsa sdcadmin@sdc$i:/home/sdcadmin/Desktop/id_rsa
	scp /Keys/id_rsa.pub sdcadmin@sdc$i:/home/sdcadmin/Desktop/id_rsa.pub
done

bash SudoMassDo.sh 'cp /home/sdcadmin/Desktop/id_rsa /home/sdc/.ssh/id_rsa && cp /home/sdcadmin/Desktop/id_rsa.pub /home/sdc/.ssh/id_rsa.pub'
bash SudoMassDo.sh 'chown sdc /home/sdcadmin/.ssh/id_rsa && chown sdc /home/sdcadmin/.ssh/id_rsa.pub'

bash SudoMassDo.sh 'rm /home/sdcadmin/Desktop/id_rsa && rm /home/sdcadmin/Desktop/id_rsa.pub'