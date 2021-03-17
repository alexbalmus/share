source _ci/init_tag.sh

allure generate -c alfresco-tas-share-test/target/surefire-reports -o reports/Share/$TAG_NAME
aws s3 sync reports/Share/$TAG_NAME s3://test-report-allure/reports/Share/$TAG_NAME