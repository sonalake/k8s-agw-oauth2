#!/bin/sh

echo "Substituting configuration env variables"

cd /usr/share/nginx/html
sed -r "s#%(.+?)%#$\1#g" index.html > tempfile
envsubst < tempfile > index.html

exit 0
