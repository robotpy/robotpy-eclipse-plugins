#!/bin/bash

cd "$(dirname $0)"/..

if [ ! -f pushToBintray.sh ]; then
    wget https://raw.githubusercontent.com/robotpy/bintray-publish-p2-updatesite/master/pushToBintray.sh
fi

bash pushToBintray.sh $1 $2 robotpy eclipse-plugin eclipse-plugin $3 robotpy.updatesite/target/site
