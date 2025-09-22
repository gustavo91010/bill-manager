#!/bim/bash

TOPIC="test"
URL="http://localhost:8183/kafka/send"
MESSAGE="HelloKafka"

for i in {1..1000}
do
  curl -X POST "$URL?topic=$TOPIC&message=$MESSAGE-$i"
  echo "Mensagem $i enviada"
  sleep 0.1
done
