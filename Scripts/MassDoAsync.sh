#!/bin/bash

for ((i = 1; i < 17; i++))
do
	bash Do.sh sdc$i "$1" &
done