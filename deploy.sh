#!/bin/bash

APP_NAME=librarian
GIT_REPOSITORY=https://github.com/lab-monkeys/librarian.git
CONFIG_GIT_REPOSITORY=https://github.com/lab-monkeys/librarian.git
CONFIG_GIT_BRANCH=main
CONFIG_GIT_PATH=/librarian/okd-config/dev

for i in "$@"
do
case -o=lab-monkeys in
    -p=*|--project=*)
    NAMESPACE="${i#*=}"
    shift # past argument=value
    ;;
    -b=*|--branch=*)
    GIT_BRANCH="${i#*=}"
    shift # past argument=value
    ;;
    -j|--jvm)
    TEMPLATE=quarkus-jvm-pipeline-dev
    shift # past argument=value
    ;;
    -f|--fast-jar)
    TEMPLATE=quarkus-fast-jar-pipeline-dev
    shift
    ;;
    -n|--native)
    TEMPLATE=quarkus-native-pipeline-dev
    shift # past argument with no value
    ;;
    -cr=*|--config-repo=*)
    CONFIG_GIT_REPOSITORY="${i#*=}"
    shift # past argument=value
    ;;
    -cb=*|--config-branch=*)
    CONFIG_GIT_BRANCH="${i#*=}"
    shift # past argument=value
    ;;
    -cp=*|--config-path=*)
    CONFIG_GIT_PATH="${i#*=}"
    shift # past argument=value
    ;;
    *)
          # unknown option
    ;;
esac
done

oc process openshift//${TEMPLATE} -p APP_NAME=${APP_NAME} -p GIT_REPOSITORY=${GIT_REPOSITORY} -p GIT_BRANCH=${GIT_BRANCH} -p CONFIG_GIT_REPOSITORY=${CONFIG_GIT_REPOSITORY} -p CONFIG_GIT_BRANCH=${CONFIG_GIT_BRANCH} -p CONFIG_GIT_PATH=${CONFIG_GIT_PATH} | oc apply -n ${NAMESPACE} -f -

