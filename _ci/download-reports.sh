aws s3 sync s3://test-report-allure/$TRAVIS_BUILD_NUMBER reports/Share/$TAG_NAME
echo ls reports/Share/$TAG_NAME

#allure generate -c alfresco-tas-share-test/target/surefire-reports -o reports/Share/$TAG_NAME
