#!/bin/bash
if [ -e path.txt ]
then
    rm path.txt
fi
files=`ls $1`
for file in $files
do
    echo $1"/"$file >> path.txt
done
echo $filelist
if [ ! -e counts ]
then
    mkdir counts
else
    rm -rf counts
    mkdir counts
fi
make-batch-counts path.txt 5 cat counts -order 3
merge-batch-counts counts
if [ -e trainfile.lm ]
then
    rm trainfile.lm
fi
make-big-lm -read counts/*.gz -order 3 -lm trainfile.lm
