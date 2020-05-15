#!/bin/sh
SHELL_PATH=`pwd -P`
echo $SHELL_PATH

fold_name="mixing_test"

noise_path=$SHELL_PATH"/"$fold_name"/source_noise/"
noise_list=( $(ls $noise_path) ) 
echo ${noise_list[*]} 

clean_path=$SHELL_PATH"/"$fold_name"/source_clean/"
clean_list=( $(ls $clean_path) )
echo ${clean_list[*]} 

for ((i=0;i<${#noise_list[@]};i++)); do
    for ((j=0;j<${#clean_list[@]};j++)); do
        num=`expr $i \* $j + $i`
        cmd_1='python create_mixed_audio_file.py '
        cmd_2=' --clean_file ./'$fold_name'/source_clean/'${clean_list[$j]}
        cmd_3=' --noise_file ./'$fold_name'/source_noise/'${noise_list[$i]}
        cmd_4=' --output_mixed_file ./'$fold_name'/output_mixed/'$num'.wav --snr 0'
        cmd_full=$cmd_1$cmd_2$cmd_3$cmd_4
        # echo $cmd_full
        $cmd_full
    done
done