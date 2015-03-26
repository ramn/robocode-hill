#!/bin/bash

#  -DWORKINGDIRECTORY=<path>  Set the working directory
#  -DROBOTPATH=<path>         Set the robots directory (default is 'robots')
#  -DBATTLEPATH=<path>        Set the battles directory (default is 'battles')
#  -DNOSECURITY=true|false    Enable/disable Robocode's security manager
#  -Ddebug=true|false         Enable/disable debugging used for preventing
#                             robot timeouts and skipped turns, and allows an
#                             an unlimited painting buffer when debugging robots
#  -DlogMessages=true|false   Log messages and warnings will be disabled
#  -DlogErrors=true|false     Log errors will be disabled
#  -DEXPERIMENTAL=true|false  Enable/disable access to peer in robot interfaces
#  -DPARALLEL=true|false      Enable/disable parallel processing of robots turns
#  -DRANDOMSEED=<long number> Set seed for deterministic behavior of random
#                             numbers

SCALA_LIB="$HOME/.ivy2/cache/org.scala-lang/scala-library/jars/scala-library-2.11.6.jar"


pushd $(dirname $0)/../target/robocode/

java -Xmx512M -DlogMessages=true -DlogErrors=true -Ddebug=true \
  -cp libs/robocode.jar:"$SCALA_LIB" robocode.Robocode $*

popd
