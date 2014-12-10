#!/bin/bash

# Kopir fuse
bash SudoMassDo.sh 'scp -o stricthostkeychecking=no -P 512 sdcuser@sahb-mint.local:/home/sdcuser/fuse.conf /etc/fuse.conf'

# Kopir fstab
bash SudoMassDo.sh 'scp -o stricthostkeychecking=no -P 512 sdcuser@sahb-mint.local:/home/sdcuser/fstab /etc/fstab'

# Kopir key fra sdc over i root
bash SudoMassDo.sh 'cp /home/sdc/.ssh/id_rsa /root/.ssh/id_rsa'
bash SudoMassDo.sh 'cp /home/sdc/.ssh/id_rsa.pub /root/.ssh/id_rsa.pub'