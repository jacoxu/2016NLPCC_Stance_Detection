#!/bin/bash
#auther:Mikolov, modifier: Jacob Xu. -- 20160614
# iphonese, bianpao, fankong, ertai, jinmo
topic="iphonese"
rm alldata.txt
rm alldata-id.txt

echo "Current topic is "${topic}" ..."
cat "TaskA_train_labeled_text_"${topic} "TaskA_train_unlabeled_text_"${topic} "TaskA_test_unlabeled_text_"${topic} > alldata.txt
awk 'BEGIN{a=0;}{print "_*" a " " $0; a++;}' < alldata.txt > alldata-id.txt

gcc word2vec.c -o word2vec -lm -pthread -O3 -march=native -funroll-loops
time ./word2vec -train ./alldata-id.txt -output ${topic}"_vectors.txt" -cbow 0 -size 50 -window 5 -negative 5 -hs 0 -sample 1e-4 -threads 40 -binary 0 -iter 20 -min-count 1 -sentence-vectors 1
grep '_\*' ${topic}"_vectors.txt" > ${topic}"_para2vecs.txt"
sed -i "s+_\*+0+g" ${topic}"_para2vecs.txt"
echo "It is done, OK!"