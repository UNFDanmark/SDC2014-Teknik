#!/bin/bash

# Upload ssh-key
bash SudoMassDo.sh "echo $(cat Keys/id_rsa) >> /home/sdc/.ssh/id_rsa"
bash SudoMassDo.sh "echo $(cat Keys/id_rsa.pub) >> /home/sdc/.ssh/id_rsa.pub"