#!/bin/bash

apt-get update
apt-get install -y apt-utils apt-transport-https less jq cron

mkdir -p /root/.gradle
cat > /root/.gradle/gradle.properties << EOF
org.gradle.daemon=true
org.gradle.jvmargs=-Xmx4g -XX:MaxPermSize=3072m -Dfile.encoding=UTF-8 -XX:MaxMetaspaceSize=1024m
EOF
echo 'export GRADLE_USER_HOME=/root/.gradle' >> /root/.bashrc
echo "export CHROME_BIN=/usr/bin/chromium" >> /root/.bashrc
