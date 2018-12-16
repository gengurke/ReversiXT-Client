#!/bin/bash

for filename in *.map ; do
	
	echo "Diese Datei: $filename"
	fname=$(basename "$filename")
	echo "hat den Namen: $fname"
	fdir=$(dirname "$filename")
	echo "und steht im Verzeichnis: $fdir"
	./server_nogl -m $filename -t 1
	
done
	