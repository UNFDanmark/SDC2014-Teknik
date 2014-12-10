SDC_EMAIL="unf bruger"
SDC_PASSWORD="unf pass"
PARSE_APP_ID="parse app id"
PARSE_API_KEY="parse api key"
MESSAGE=$1

curl -X POST \
  -H "X-Parse-Application-Id: $PARSE_APP_ID" \
  -H "X-Parse-REST-API-Key: $PARSE_API_KEY" \
  -H "Content-Type: application/json" \
  -d "{\"where\": {\"deviceType\": \"ios\"}, \"data\": {\"alert\": \"$MESSAGE\"}}" \
  https://api.parse.com/1/push
  
curl -X POST \
  -H "X-Parse-Application-Id: $PARSE_APP_ID" \
  -H "X-Parse-REST-API-Key: $PARSE_API_KEY" \
  -H "Content-Type: application/json" \
  -d "{\"where\": {\"deviceType\": \"android\"}, \"data\": {\"alert\": \"$MESSAGE\"}}" \
  https://api.parse.com/1/push

curl -X POST \
  -d "email=$SDC_EMAIL&password=$SDC_PASSWORD&text=$MESSAGE" \
  http://shared.devel.unf.dk/script/sdc-status-updates/resources/new-update.php
