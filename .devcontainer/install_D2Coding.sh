#!/bin/bash

wget https://github.com/naver/d2codingfont/releases/download/VER1.3.2/D2Coding-Ver1.3.2-20180524.zip

mkdir -p ~/.local/share/fonts

sudo apt install unzip -y

unzip -j D2Coding-Ver1.3.2-20180524.zip "D2CodingAll/*" -d ~/.local/share/fonts -o

fc-cache -f -v

fc-list | grep "D2Coding"
