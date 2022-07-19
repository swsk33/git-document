#!/bin/bash
/etc/init.d/ssh restart
nginx
sudo -u git java -jar git-document-*.jar