安装mysql8.0

mysql.yml

```yml
cat >/docker/compose/mysql.yml<<EOF
version: '3'
services:
  mysql:
    image: mysql:8.0.31
    container_name: mysql8
    environment:
      # 时区上海
      TZ: Asia/Shanghai
      # root 密码
      MYSQL_ROOT_PASSWORD: "@Root123"
      # 初始化数据库
      MYSQL_DATABASE: hugo-cloud
    ports:
      - "3306:3306"
    volumes:
      # 数据挂载
      - /docker/mysql/data/:/var/lib/mysql/
      # 配置挂载
      - /docker/mysql/conf/:/etc/mysql/conf.d/
    command:
      # 将mysql8.0默认密码策略 修改为 原先 策略 (mysql8.0对其默认策略做了更改 会导致密码无法匹配)
      --default-authentication-plugin=mysql_native_password
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_general_ci
      --explicit_defaults_for_timestamp=true
      --lower_case_table_names=1
    privileged: true
    network_mode: "host"
EOF
```


设置配置文件 /docker/mysql/conf/mysql.cnf

```shell
cat > /docker/mysql/conf/mysql.cnf<<EOF
[mysqld]
server-id = 1 
port = 3306
EOF
```


```shell
docker-compose -f /docker/compose/mysql.yml up -d mysql
```

注：需要开启3306端口

