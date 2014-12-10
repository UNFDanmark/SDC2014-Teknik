#!/bin/bash

bash SDCMassDoAsync.sh "DISPLAY=:0 zenity --info --text='$1'"

bash API.sh "$1"