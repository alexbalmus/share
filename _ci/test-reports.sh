allure generate -c reports/allure-results -o reports/allure-results-html
aws s3 sync reports/allure-results-html s3://test-report-allure/reports/
allure open reports