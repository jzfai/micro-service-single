buildDockFile(){
  cat > Dockerfile-build-jar <<EOF
  FROM anapsix/alpine-java:8_server-jre_unlimited
  # 作者
  MAINTAINER kuanghua <869653722@qq.com>
  # VOLUME 指定了临时文件目录为/tmp。
  VOLUME /tmp
  # 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
  # 将jar包添加到容器中并更名为app.jar
  COPY ${module_name}.jar   /tmp/app.jar
  # 运行jar包
  ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-Duser.timezone=GMT+08","-XX:+UseCGroupMemoryLimitForHeap","-Xms64m","-Xmx128m","-jar","/tmp/app.jar"]
EOF
  echo 删除原有镜像${module_name}:${build_version}
  docker image rm -f  ${module_name}:${build_version}
  docker build -f Dockerfile-build-jar -t ${module_name}:${build_version} .
}


buildComposeYml(){
cat > buildjar.yml <<EOF
version: '3'
services:
  ${module_name}:
    image: ${module_name}:${build_version}
    container_name: ${module_name}_${module_port}
    privileged: true
    restart: always
    deploy:
      resources:
        limits:
          cpus: '0.8'
          memory: 400M
        reservations:
          cpus: '0.4'
          memory: 200M
    volumes:
        - "/deploy/velocity-template:/velocity-tmp"
        - "/usr/share/zoneinfo/Asia/Shanghai:/etc/localtime"
    healthcheck:
      test: ["CMD-SHELL", "netstat -antp | grep  ${module_port} || exit 1"]
      interval: 3s
      timeout: 10s
      retries: 3
    network_mode: "host"
EOF
}

delContainer(){
  docker container ls -a | grep ${module_name}
  if [[ $? == 0 ]]; then
    docker container rm -f  ${module_name}_${module_port}
  fi
}