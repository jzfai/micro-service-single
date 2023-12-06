### 安装docker（之前安装过这里不需要安装）

```
yum clean all && yum makecache fast
curl  http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -o /etc/yum.repos.d/docker-ce.repo
yum install -y --setopt=obsoletes=0 \
docker-ce-3:19.03.12-3.el7.x86_64 \
docker-ce-selinux-3:19.03.12-3.el7.x86_64
```

启动docker

```
systemctl start docker
```

docker 默认配置文件

```
cat >/etc/docker/daemon.json<<EOF
{
  "registry-mirrors": ["https://1jl205ki.mirror.aliyuncs.com"]
}
EOF
```

重启docker

```
systemctl restart docker
```



### docker-compose安装

 docker compose是一个命令行工具，是用于定义和运行多容器Docker应用程序的工具；通过Compose，开发者可以使用YML文件来配置应用程序需要的所有服务。 

上传  [docker-compose](https://gitee.com/jzfai/micro-serivce-learn/tree/master/docs-file/docker-compose)  到  /usr/local/bin/  下

或者执行

```
curl -L https://github.com/docker/compose/releases/download/1.21.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
```





```shell
chmod +x /usr/local/bin/docker-compose
```

查看版本

```shell
docker -version
docker-compose -v
```



