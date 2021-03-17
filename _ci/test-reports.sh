allure generate -c alfresco-tas-share-test/target/surefire-reports -o reports/allure-results-html
aws s3 sync reports/allure-results-html s3://test-report-allure/reports/