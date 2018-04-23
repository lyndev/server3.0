#!/bin/sh
rm -rf tmp
mkdir tmp
cp -r Debug tmp/FightServer
cd tmp/FightServer
rm -f *.exe *.pdb *.dll *.ilk
cd ..
tar jcvf FightServer.tar.bz2 FightServer
cp FightServer.tar.bz2 ..
rm -rf tmp


