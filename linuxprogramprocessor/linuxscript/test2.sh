#!/bin/bash
fname="test.sh"
lines=`wc -l $fname | awk '{print $1}'`
echo $lines
