#!/bin/bash
set -x
VS_ENV=`cat env_url.txt`

#rm -rf $(pwd)/frontend/lighthouse/.lighthouseci
mkdir frontend/.lighthouseci

docker pull hemanthsridhar-docker-lhci-server-center.bintray.io/lhci:4.0
docker rm -f lhci

#create a docker container whose name is LHCI and shared memory size #of 512.
docker run -v "$(pwd)/frontend":/usr/src -w /usr/src  -d --shm-size=512m -it --name lhci hemanthsridhar-docker-lhci-server-center.bintray.io/lhci:4.0

docker exec -i lhci sh -c 'cat > ./lighthouse/env_url.json << EOF
{
  "url": "'$VS_ENV'"
}
EOF'

docker exec -i lhci sh -c 'rm -rf frontend/.lighthouseci/*'

#Run Lighthouse CI against the container named lhci
docker exec -i lhci sh -c \
'lhci autorun --config="./lighthouse/lhci.json" --collect.url='"https://google.com"'' || true

#teardown
docker rm -f lhci

