#!/bin/bash
clear
echo ""
echo ""
echo "======================================================"
echo "==== RUNNING JENKINS SHELL COMMANDS on $NODE_NAME"
echo ""
echo "==== selected Jenkins environment variables ===="
set | egrep "BRANCH|BUILD|CHANGE|GIT|JENKINS|JOB|WORKSPACE"
echo "====/selected Jenkins environment variables ===="

# ==== TODO ====
# split into functions
# activate clean-up routine


# ==== ADJUSTABLE VARIABLES ====
# gp:to-do we need to update these to set only if they're not set already, that way the Dev can override in the Jenkinsfile
#  == Tomcat Variables ==
DOCKERFILE_PATH=/home/jenkins/vs-dockerfile/
DOCKERFILE_NAME=vs-brxm
DOCKERFILE_LOCATION=$DOCKERFILE_PATH/$DOCKERFILE_NAME
VS_DATESTAMP=`date +%Y%m%d`
VS_HOST_IP_ADDRESS=`/usr/sbin/ip ad sh  | egrep "global noprefixroute" | awk '{print $2}' | sed -e "s/\/.*$//"`
#  == brXM Instance Variables ==
VS_BRXM_INSTANCE_HTTP_HOST=localhost
#  == Hosting Environment Variables ==
VS_PROXY_SERVER_SCHEME=https
VS_PROXY_SERVER_FQDN=feature.visitscotland.com
# the next variable "VS_BRXM_PORT_OVERRIDE" should only be used by operations for debug purposes, an available port will be found and used later in this script
#VS_BRXM_PORT_OVERRIDE=8080
VS_FRONTEND_DIR=ui-integration/node_modules/@visitscotland/vs-component-library
VS_SSR_PACKAGE_DIR=ui-integration
VS_HIPPO_TOMCAT_PORT=8080
VS_SSR_PACKAGE_SOURCE="$VS_SSR_PACKAGE_DIR/ssr/server/ $VS_FRONTEND_DIR/dist/ssr/ $VS_FRONTEND_DIR/node_modules/"
VS_SSR_PACKAGE_TARGET="./target"
VS_SSR_PACKAGE_NAME="vs-ssr-package.tar.gz"
VS_SSR_APP_PORT=8082
# ====/ADJUSTABLE VARIABLES ====

# ==== PREPARE ENVIRONMENT ====
# unset variables
CONTAINER_LIST=
PARENT_JOB_NAME=
RESERVED_PORT_LIST=
# set container name from branch name - removing / characters
CONTAINER_NAME=`echo $JOB_NAME | sed -e "s/\/.*//g"`"_"`basename $BRANCH_NAME`
VS_CONTAINER_INTERNAL_PORT=
#/==== PREPARE ENVIRONMENT ====

echo "==== selected VS environment variables ===="
set | egrep "VS_"
set | egrep "CONTAINER"
echo "====/selected VS environment variables ===="

# check to see if a container called $CONTAINER_NAME is running, if so set $CONTAINER_RUNNING to Docker's CONTAINER ID
echo ""
echo "checking for running containers with name $CONTAINER_NAME"
CONTAINER_RUNNING=`docker ps -aq --filter "name=$CONTAINER_NAME" --filter "status=running"`
if [ ! -z "$CONTAINER_RUNNING" ]; then
  echo " - running container found, ID:$CONTAINER_RUNNING, with name $CONTAINER_NAME"
else
  echo " - no running container found with name $CONTAINER_NAME"
fi

# check to see if a container called $CONTAINER_NAME is existent, if so set $CONTAINER_EXISTS to that container's "CONTAINER ID"
echo ""
echo "checking for non-running containers with name $CONTAINER_NAME"
CONTAINER_EXISTS=`docker ps -aq --filter "name=$CONTAINER_NAME"`
if [ ! -z "$CONTAINER_EXISTS" ]; then
  CONTAINER_STATUS=`docker ps -a --filter "name=$CONTAINER_NAME" --format "table {{.Status}}" | tail -1`
  echo " - non-running container found, ID:$CONTAINER_EXISTS, with name $CONTAINER_NAME and status:$CONTAINER_STATUS"
else
  echo " - no container found with name $CONTAINER_NAME"
fi

# stop running containers
# TO-DO - undeploy application first
echo ""
echo "stopping running containers with name $CONTAINER_NAME"
for CONTAINER in `docker ps | egrep "$CONTAINER_NAME" | awk '{print $1}'`; do echo stopping $CONTAINER; docker stop $CONTAINER; done

# delete stopped containers
# gp: this will never work because "docker container ls" will only show running containers
echo ""
echo "deleting containers with name $CONTAINER_NAME"
docker container ls | egrep "$CONTAINER_NAME"
for CONTAINER in `docker container ls -a | egrep "$CONTAINER_NAME" | awk '{print $1}'`; do echo deleting $CONTAINER; docker container rm -f $CONTAINER; done
#docker container rm $CONTAINER_NAME

# delete existing images - does this have a purpose? will there ever be an image with the name $CONTAINER_NAME?
echo ""
echo "deleting any docker images with name $CONTAINER_NAME"
docker images | egrep "$CONTAINER_NAME"
for IMAGE in `docker images | egrep "$CONTAINER_NAME" | awk '{print $3}'`; do echo deleting $IMAGE; docker image rm -f $IMAGE; done

# gp:DONE - even if override is set we must still check to ensure it's free, move the while loop to after the if block and just add PORT/MAXPORT values into the if. If the override port if in use the job must fail
if [ "$GIT_BRANCH" == "develop" ]; then
  echo ""
  echo "GIT_BRANCH is $GIT_BRANCH, OVERRIDE PORT will be set to 8100"
  VS_BRXM_PORT_OVERRIDE=8100
fi

if [ -z "$VS_BRXM_PORT_OVERRIDE" ]; then
  PORT=8000
  MAXPORT=8099
  echo ""
  echo "finding a free port to map to the new container's Tomcat port - range $PORT-$MAXPORT"
else
  PORT=$VS_BRXM_PORT_OVERRIDE
  MAXPORT=$VS_BRXM_PORT_OVERRIDE
  echo ""
  echo "PORT will be set to $VS_BRXM_PORT_OVERRIDE due to VS_BRXM_PORT_OVERRIDE"
fi

# check all branches to see what ports are "reserved" by existing containers
PARENT_JOB_NAME=`echo $JOB_NAME | sed -e "s/\/.*//g"`
echo ""
echo "checking for ports reserved by other branches in $PARENT_JOB_NAME"
#for CONTAINER in `curl -s $JENKINS_URL/job/$PARENT_JOB_NAME/rssLatest | sed -e "s/type=\"text\/html\" href=\"/\n/g" | egrep "^https" | sed -e "s/%252F/\//g" | sed "s/\".*//g" | sed -e "s/htt.*\/\(.*\)\/[0-9]*\//\1/g" | egrep -v "http"`; do
#  CONTAINER_LIST="$CONTAINER_LIST $CONTAINER"
#  RESERVED_PORT=`docker inspect --format='{{(index (index .NetworkSettings.Ports "8080/tcp") 0).HostPort}}' $PARENT_JOB_NAME\_$CONTAINER 2>/dev/null`
#  if [ ! -z "$RESERVED_PORT" ]; then
#    RESERVED_PORT_LIST="$RESERVED_PORT_LIST $RESERVED_PORT"
#    echo "$RESERVED_PORT is reserved by $PARENT_JOB_NAME\_$CONTAINER"
#  fi
#done
for CONTAINER in `curl -s $JENKINS_URL/job/$PARENT_JOB_NAME/rssLatest | sed -e "s/type=\"text\/html\" href=\"/\n/g" | egrep "^https" | sed -e "s/%252F/\//g" | sed "s/\".*//g" | sed -e "s/htt.*\/\(.*\)\/[0-9]*\//$PARENT_JOB_NAME\_\1/g" | egrep -v "http"`; do
  BRANCH_CONTAINER_LIST="$BRANCH_CONTAINER_LIST $CONTAINER"
  RESERVED_PORT=`docker inspect --format='{{(index (index .HostConfig.PortBindings "8080/tcp") 0).HostPort}}' $CONTAINER 2>/dev/null`
  if [ ! -z "$RESERVED_PORT" ]; then
    RESERVED_PORT_LIST="$RESERVED_PORT_LIST $RESERVED_PORT"
    echo "$RESERVED_PORT is reserved by $CONTAINER"
  fi
done
echo ""
echo "checking for ports reserved by pull requests in $PARENT_JOB_NAME"
for CONTAINER in `curl -s $JENKINS_URL/job/$PARENT_JOB_NAME/view/change-requests/rssLatest | sed -e "s/type=\"text\/html\" href=\"/\n/g" | egrep "^https" | sed -e "s/%252F/\//g" | sed "s/\".*//g" | sed -e "s/htt.*\/\(.*\)\/[0-9]*\//$PARENT_JOB_NAME\_\1/g" | egrep -v "http"`; do
  BRANCH_CONTAINER_LIST="$BRANCH_CONTAINER_LIST $CONTAINER"
  RESERVED_PORT=`docker inspect --format='{{(index (index .HostConfig.PortBindings "8080/tcp") 0).HostPort}}' $CONTAINER 2>/dev/null`
  if [ ! -z "$RESERVED_PORT" ]; then
    RESERVED_PORT_LIST="$RESERVED_PORT_LIST $RESERVED_PORT"
    echo "$RESERVED_PORT is reserved by $CONTAINER"
  fi
done;
if [ ! -z "$RESERVED_PORT_LIST" ]; then echo "Ports $RESERVED_PORT_LIST are reserved"; fi

# tidy containers when building the "develop" branch
if [ "$GIT_BRANCH" == "develop" ]; then
  echo ""
  echo "checking all containers on $NODE_NAME matching $PARENT_JOB_NAME*"
  for CONTAINER in `docker ps -a --filter "name=$PARENT_JOB_NAME*" --format "table {{.Names}}" | tail -n +2`; do
    CONTAINER_MATCHED=
    ALL_CONTAINER_LIST="$ALL_CONTAINER_LIST $CONTAINER"
    #echo "checking to see if there's a branch for $CONTAINER"
    for BRANCH_CONTAINER in $BRANCH_CONTAINER_LIST; do
      if [ "$CONTAINER" = "$BRANCH_CONTAINER" ]; then
        echo "there is a branch associated with $CONTAINER"
        CONTAINER_MATCHED="TRUE"
        break
      fi
    done
    if [ ! "$CONTAINER_MATCHED" = "TRUE" ]; then
    	echo "no branch was found matching container $CONTAINER. I could run docker container rm -f $CONTAINER, but I won't just yet"
        #echo "no branch was found matching container $CONTAINER, running docker container rm -f $CONTAINER"
        #docker container rm -f $CONTAINER
    fi
  done
fi

while [ $PORT -le $MAXPORT ]; do
  FREE=`netstat -an | egrep "LISTEN *$" | grep $PORT`
  if [ "$FREE" = "" ]; then
    echo " - netstat says $PORT is free, checking it's not reserved"
    for RESERVED_PORT in $RESERVED_PORT_LIST; do
      if [ "$RESERVED_PORT" = "$PORT" ]; then
        echo " - docker says $RESERVED_PORT is reserved"
        PORT_RESERVED="TRUE"
      else
        PORT_RESERVED="FALSE"
      fi
    done
    if [ ! "$PORT_RESERVED" = "TRUE" ]; then
      break
    else
      PORT=$((PORT+1))
      sleep 0
    fi
  else
    PORT=$((PORT+1))
    sleep 0
  fi
done

# testing - don't run this for develop to see what happens if port is not avaiable
if [ $PORT -gt $MAXPORT ]; then
  if [ ! -z "$VS_BRXM_PORT_OVERRIDE" ] && [ ! "$PORT_RESERVED" = "TRUE" ]; then
    FAIL_REASON="OVERRIDE PORT $VS_BRXM_PORT_OVERRIDE is in use, setting PORT to NULL"
  elif [ ! -z "$VS_BRXM_PORT_OVERRIDE" ] && [ "$PORT_RESERVED" = "TRUE" ]; then
    FAIL_REASON="OVERRIDE PORT $VS_BRXM_PORT_OVERRIDE is reserved, setting PORT to NULL"
  else  
    FAIL_REASON="port scan reached $MAXPORT, no ports are free, setting PORT to NULL"
  fi
  PORT=NULL
  SAFE_TO_PROCEED=FALSE
  echo " - $FAIL_REASON"
fi

# search for latest Hippo distribution files if HIPPO_LATEST is not already set
if [ ! "$SAFE_TO_PROCEED" = "FALSE" ]; then
  if [ -z $HIPPO_LATEST ]; then
    # search in $WORKSPACE/target/ for files matching "*.tar.gz"
    echo ""
    echo "searching for latest Hippo distribution files in $WORKSPACE/target"
    HIPPO_LATEST=`ls -alht $WORKSPACE/target/*.tar.gz | head -1 | awk '{print $9}'` 2>&1 > /dev/null
    if [ -z "$HIPPO_LATEST" ]; then
      # recursive search in $WORKSPACE/ for files matching "*.tar.gz"
      echo "no archive found in $WORKSPACE/target/, widening search"
      HIPPO_LATEST=`find $WORKSPACE/ -name "*.tar.gz" | head -1`
    fi

    if [ ! -z "$HIPPO_LATEST" ]; then
      echo " - found $HIPPO_LATEST"
    else
      HIPPO_LATEST=NULL
      SAFE_TO_PROCEED=FALSE
      FAIL_REASON="no archive found in $WORKSPACE, giving up"
      echo " - $FAIL_REASON"
    fi
  else
    echo "search for distribution files will not be run as HIPPO_LATEST was overridden to $HIPPO_LATEST"
  fi
else
  echo ""
  echo "search for distribution files will not be run due to previous failures"
fi

# package SSR app files
if [ "$VS_SSR_PROXY_ON" = "TRUE" ] && [ ! "$SAFE_TO_PROCEED" = "FALSE" ]; then
  echo "packaging SSR application"
  if [ -d "$VS_FRONTEND_DIR" ]; then
    tar -zcf $VS_SSR_PACKAGE_TARGET/$VS_SSR_PACKAGE_NAME $VS_SSR_PACKAGE_SOURCE
    RETURN_CODE=$?; echo $RETURN_CODE
    if [ ! "$RETURN_CODE" = "0" ]; then
      SAFE_TO_PROCEED=FALSE
      FAIL_REASON="Failed to package SSR app from $VS_FRONTEND_DIR, command exited with $RETURN_CODE"
    fi
  fi
fi

# create Docker container
if [ ! "$SAFE_TO_PROCEED" = "FALSE" ]; then
  sleep 5
  if [ "$VS_SSR_PROXY_ON" = "TRUE" ]; then
    VS_CONTAINER_EXPOSE_PORT=$VS_SSR_APP_PORT
  else
    VS_CONTAINER_EXPOSE_PORT=$VS_HIPPO_TOMCAT_PORT
  fi
  echo ""
  echo "about to create a new Docker container with:"
  VS_DOCKER_CMD=docker run -d --name $CONTAINER_NAME -p $PORT:$VS_CONTAINER_EXPOSE_PORT --env VS_SSR_PROXY_ON=$VS_SSR_PROXY_ON --env VS_SSR_PACKAGE_NAME=$VS_SSR_PACKAGE_NAME $DOCKERFILE_NAME /bin/bash -c "/usr/local/bin/vs-mysqld-start && /usr/local/bin/vs-hippo && while [ ! -f /home/hippo/tomcat_8080/logs/cms.log ]; do echo no log; sleep 2; done; tail -f /home/hippo/tomcat_8080/logs/cms.log"
  echo $VS_DOCKER_CMD
  docker run -d --name $CONTAINER_NAME -p $PORT:$VS_CONTAINER_EXPOSE_PORT --env VS_SSR_PROXY_ON=$VS_SSR_PROXY_ON --env VS_SSR_PACKAGE_NAME=$VS_SSR_PACKAGE_NAME $DOCKERFILE_NAME /bin/bash -c "/usr/local/bin/vs-mysqld-start && /usr/local/bin/vs-hippo && while [ ! -f /home/hippo/tomcat_8080/logs/cms.log ]; do echo no log; sleep 2; done; tail -f /home/hippo/tomcat_8080/logs/cms.log"
  RETURN_CODE=$?; echo $RETURN_CODE
  if [ ! "$RETURN_CODE" = "0" ]; then
    SAFE_TO_PROCEED=FALSE
    FAIL_REASON="Docker failed to start container $CONTAINER_NAME, command exited with $RETURN_CODE"
  fi
  sleep 10
else
  echo ""
  echo "container will not be started due to previous failures"
fi

# copy build artefacts to container
if [ ! "$SAFE_TO_PROCEED" = "FALSE" ]; then
  echo ""
  echo "about to copy $HIPPO_LATEST to container $CONTAINER_NAME:/home/hippo"
  docker cp $HIPPO_LATEST $CONTAINER_NAME:/home/hippo
  RETURN_CODE=$?; echo $RETURN_CODE
  if [ ! "$RETURN_CODE" = "0" ]; then
    SAFE_TO_PROCEED=FALSE
    FAIL_REASON="Docker failed to run cp command against $CONTAINER_NAME, command exited with $RETURN_CODE"
  fi
else
  echo ""
  echo "docker cp will not be run due to previous failures"
fi

if [ "$VS_SSR_PROXY_ON" = "TRUE" ] && [ ! "$SAFE_TO_PROCEED" = "FALSE" ]; then
  echo ""
  echo "about to copy $VS_SSR_PACKAGE_NAME to container $CONTAINER_NAME:/home/hippo"
  docker cp $VS_SSR_PACKAGE_TARGET/$VS_SSR_PACKAGE_NAME $CONTAINER_NAME:/home/hippo
  RETURN_CODE=$?; echo $RETURN_CODE
  if [ ! "$RETURN_CODE" = "0" ]; then
    SAFE_TO_PROCEED=FALSE
    FAIL_REASON="Docker failed to run cp command against $CONTAINER_NAME, command exited with $RETURN_CODE"
  fi
elif [ ! "$VS_SSR_PROXY_ON" = "TRUE" ] && [ ! "$SAFE_TO_PROCEED" = "FALSE" ]; then
  echo ""
  echo "docker cp of VS_SSR_PACKAGE_NAME:$VS_SSR_PACKAGE_NAME will not be run due VS_SSR_PROXY_ON:$VS_SSR_PROXY_ON"
else
  echo ""
  echo "docker cp will not be run due to previous failures"
fi

if [ ! "$SAFE_TO_PROCEED" = "FALSE" ]; then
  echo ""
  echo "about to execute "/usr/local/bin/vs-hippo nodb" in container $CONTAINER_NAME"
  docker exec -d $CONTAINER_NAME /usr/local/bin/vs-hippo nodb
  RETURN_CODE=$?; echo $RETURN_CODE
  if [ ! "$RETURN_CODE" = "0" ]; then
    SAFE_TO_PROCEED=FALSE
    FAIL_REASON="Docker failed to run exec command in $CONTAINER_NAME, command exited with $RETURN_CODE"
  fi
else
  echo ""
  echo "docker exec will not be run due to previous failures"
fi

if [ ! "$SAFE_TO_PROCEED" = "FALSE" ]; then
  EXIT_CODE=0
  echo ""
  echo ""
  echo "###############################################################################################################################"
  echo ""
  echo "The site instance for branch $GIT_BRANCH should now be available in a few moments on $NODE_NAME - $VS_HOST_IP_ADDRESS at:"
  echo "  - $VS_PROXY_SERVER_SCHEME://$VS_PROXY_SERVER_FQDN/?vs_brxm_host=$VS_HOST_IP_ADDRESS&vs_brxm_port=$PORT&vs_brxm_http_host=$VS_BRXM_INSTANCE_HTTP_HOST"
  echo ""
  echo "The CMS for the instance should now be available at:"
  echo "  - $VS_PROXY_SERVER_SCHEME://$VS_PROXY_SERVER_FQDN/cms/?vs_brxm_host=$VS_HOST_IP_ADDRESS&vs_brxm_port=$PORT&vs_brxm_http_host=$VS_BRXM_INSTANCE_HTTP_HOST"
  echo "and the Console at:"
  echo "  - $VS_PROXY_SERVER_SCHEME://$VS_PROXY_SERVER_FQDN/cms/console/?vs_brxm_host=$VS_HOST_IP_ADDRESS&vs_brxm_port=$PORT&vs_brxm_http_host=$VS_BRXM_INSTANCE_HTTP_HOST"
  echo ""
  echo "To clear the proxy server settings between sessions either close your browser or browse to:"
  echo "  - $VS_PROXY_SERVER_SCHEME://$VS_PROXY_SERVER_FQDN/?vs_brxm_reset"
  echo ""
  echo ""
  echo "Direct Tomcat access - available only on the Web Development LAN"
  echo "  - http://$VS_HOST_IP_ADDRESS:$PORT/cms/"
  echo "  - http://$VS_HOST_IP_ADDRESS:$PORT/site/"
  echo "    -  needs a HOST header of localhost:8080 to be passed with the request"
  echo ""
  echo "###############################################################################################################################"
  echo ""
  echo ""
else
  EXIT_CODE=127
  echo ""
  echo ""
  echo "###############################################################################################################################"
  echo ""
  echo "JOB FAILED because $FAIL_REASON"
  echo ""
  echo "###############################################################################################################################"
  echo ""
  echo ""
fi

exit $EXIT_CODE
