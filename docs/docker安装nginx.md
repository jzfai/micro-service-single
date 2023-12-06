### docker 安装 elk

上传 [nginx](https://gitee.com/jzfai/micro-serivce-learn/tree/master/docs-file/nginx) 文件夹复制到 /docker目录下  并执行

```shell
chmod -R  777 /docker/nginx
```

nginx.yml


```yml
cat>/docker/compose/nginx.yml<<EOF
version: '3'
services:
  nginx:
    image: nginx:1.22.1
    container_name: nginx
    environment:
      # 时区上海
      TZ: Asia/Shanghai
    ports:
      - "80:80"
      - "443:443"
    volumes:
      # 配置文件映射
      - /docker/nginx/conf:/etc/nginx
      # 页面目录
      - /docker/nginx/html:/usr/share/nginx/html
      # 日志目录
      - /docker/nginx/logs:/var/log/nginx
    privileged: true
    network_mode: "host"
EOF
```

初次安装需要cp docker目录(非必要)

```
docker  cp nginx:/etc/nginx /docker/nginx/conf
docker  cp nginx:/usr/share/nginx/html /docker/nginx
```

部署

```shell
docker-compose -f  /docker/compose/nginx.yml  up -d nginx
```


开发端口

80,443



