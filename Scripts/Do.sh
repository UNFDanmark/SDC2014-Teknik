#!/bin/bash

ssh sdcadmin@$1 "cat /etc/hostname && $2"