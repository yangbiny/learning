#!/bin/bash

# 假定最多三段，依次为 major.minor.patch

if [[ ! -f "pom.xml" ]]; then
  echo "当前目录未找到 pom.xml"
  exit -1
fi

current=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

ACTION="show"
if [[ $# -gt 0 ]]; then
  ACTION=$1
  shift
fi

function upgrade() {
  echo $(python3 -c 'import sys; o = sys.argv[1]; a = sys.argv[2]; a == "patch" and print (o.split(".")[0] + "." + o.split(".")[1] + "." + str(int(o.split(".")[2])+1) ); a == "minor" and print (o.split(".")[0] + "." + str(int(o.split(".")[1])+1) + "." + o.split(".")[2] ); a == "major" and print (str(int(o.split(".")[0]) + 1) + "." + o.split(".")[1] + "." + o.split(".")[2] );' $1 $2)
}

function commit() {
  echo "commit old: $2 new: $1"
  mvn -q versions:set -DnewVersion=$1
  mvn -q versions:commit
  echo 'commit done'
}

case "$ACTION" in
patch)
  version=$(upgrade $current patch)
  commit $version $current
  ;;
minor)
  version=$(upgrade $current minor)
  commit $version $current
  ;;
major)
  version=$(upgrade $current major)
  commit $version $current
  ;;
set)
  if [ $# -ne 1 ]; then
    echo "参数数量不正确，需要指定版本信息，类似于1.0.0"
    exit -1
  fi
  if [[ $1 =~ ^[1-9].[0-99].[0-99]$ ]]; then
    version=$1
    commit $version $current
  else
    echo "参数格式不正确，需要类似于： 1.0.0"
    exit -1
  fi
  ;;
help)
  echo './version.sh [help set patch minor major] ..'
  ;;
*)
  echo "current version: $current"
  ;;
esac
