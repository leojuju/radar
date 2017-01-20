sed -i "s/localhost:8080\/radarKafkaMonitorApi/localhost:8882\/radarKafkaMonitorApi/g" `grep "localhost:8080/radarKafkaMonitorApi" -rl monitor-htmls`
