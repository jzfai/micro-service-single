#!/usr/bin/env bash
module_name="hugo-admin"
build_version="2.0.0"
module_port="10100"

. ./build-jar-utils.sh


echo "4.生成dockerfile文件"
buildDockFile


echo "5.生成docker-compose文件"
buildComposeYml


echo "6.启动前先删除要部署容器"
delContainer

wait;
echo "7.进行部署"
/usr/local/bin/docker-compose  -f   buildjar.yml up   -d  ${module_name}

