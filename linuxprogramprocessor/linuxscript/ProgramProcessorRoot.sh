#!/bin/bash
function ergodic(){
#echo $alllines
for file in `ls $1`
do
    if [ -d $1"/"$file ]
    then
        ergodic $1"/"$file $2
    else
        if [ "${file##*.}" = "java" ]
        then
            fname=$1"/"$file
            lines=`wc -l $fname | awk '{print $1}'`

#            echo "lines:$lines"

            alllines=$(( $alllines + $lines ))

#            echo "alllines:$alllines"

            if [ $alllines -gt 100000 ]
            then
                java -jar ProgramProcessor.jar tempPool $2
                rm -rf tempPool
                mkdir tempPool
                alllines=0
            fi
            cfile=$file
            if [ -e "tempPool/"$file ]
            then
                out=`date +%s` 
               cfile="R"$out"T"$file
            fi
            cp $1"/"$file "tempPool/"$cfile
        fi
    fi
done
}
if [ ! -e tempPool ]
then
    mkdir tempPool
else
    rm -rf mkdir tempPool
    mkdir tempPool
fi
alllines=0
ergodic $1 $2
java -jar ProgramProcessor.jar tempPool $2
#echo "over alllines:$alllines"
