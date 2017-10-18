#!/bin/bash

cd "$(dirname $0)"/..

if [ ! -f pushToBintray.sh ]; then 
    wget https://raw.githubusercontent.com/robotpy/bintray-publish-p2-updatesite/master/pushToBintray.sh
fi

bash pushToBintray.sh $DEPLOY_USERNAME $DEPLOY_SECRET robotpy eclipse-plugin eclipse-plugin latest robotpy.updatesite/target/site