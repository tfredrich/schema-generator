#! /bin/bash
#
export OUTPUT_DIR="./target/schema-out"
export CLASSES=`cat ./src/main/resources/schema/classes.txt`
#
rm -rf ${OUTPUT_DIR}
java -jar ~/bin/schema-generator-1.0.0-SNAPSHOT.jar -u "https://schema.autheus.com/oidc/" -r "account, createdAt, updatedAt" -o ${OUTPUT_DIR} $1 ${CLASSES}