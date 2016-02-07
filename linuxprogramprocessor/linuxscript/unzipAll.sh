#!/bin/bash
function ergodic(){
for file in ` ls $1`
do
    if [ -f $1"/"$file ]
    then
        if [ "${file##*.}" = "zip" ]
        then
            lsar $1"/"$file
            unar $1"/"$file -o "UnzipPool"
        fi
    fi
done
}
ergodic $1
