#!/bin/bash

# ==== TO-DO ====
# gp: update post-test check section to send the email - add more detail
# gp: remove lighthouse failure notification from Jenkinsfile
# gp: use VS variables now exported from infrastructure script to populate the request values for the lighthouse tests
# gp: change LHCI container name to use other job container name plus a suffix - that way it'll be unique (at preset only one LHCI can run)
# gp: add functionality of hemanthsridhar-docker-lhci-server-center.bintray.io/lhci to VS brxm container
# gp: add while loop to curl environment and wait for a "200" before proceeding, remove sleep 120 from 
# ====/TO-DO ====

#VS_VS_LAST_ENV=vs-last-env
# read in VS variables from VS_VS_LAST_ENV
#source $WORKSPACE/$VS_VS_LAST_ENV
if [ "VS_DEBUG" = "TRUE" ]; then
  echo "==== selected VS environment variables ===="
  set | egrep "^(VS_)"
  echo "====/selected VS environment variables ===="
  echo ""
fi

#set -x
VS_PORT=`cat env_port.txt`
VS_HOST=`cat env_host.txt`
rm -rf $(pwd)/frontend/.lighthouseci
mkdir frontend/.lighthouseci

docker pull hemanthsridhar-docker-lhci-server-center.bintray.io/lhci:4.0
docker rm -f lhci

# create a docker container whose name is LHCI and shared memory size #of 512.
docker run -v "$(pwd)/frontend":/usr/src -w /usr/src  -d --shm-size=512m -it --name lhci hemanthsridhar-docker-lhci-server-center.bintray.io/lhci:4.0

docker exec -i lhci sh -c 'cat > ./extra.json << EOF
{
    "Cookie" : "vs_brxm_host='$VS_HOST'; vs_brxm_port='$VS_PORT'; vs_brxm_http_host=localhost"
}
EOF'

docker exec -i lhci sh -c 'rm -rf frontend/.lighthouseci/*'

#Run Lighthouse CI against the container named lhci
docker exec -i lhci sh -c \
'lhci autorun --config="./lighthouse/lhci-desktop.json"' && LH_DESKTOP_RESULT=true || LH_DESKTOP_RESULT=false

docker exec -i lhci sh -c \
'lhci autorun --config="./lighthouse/lhci-mobile.json"' && LH_MOBILE_RESULT=true || LH_MOBILE_RESULT=false

if [ $LH_DESKTOP_RESULT = false ] || [ $LH_MOBILE_RESULT = false ]
then
  exit 1
fi

#teardown
docker rm -f lhci

