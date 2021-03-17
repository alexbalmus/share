aws s3 sync s3://test-report-allure/$TRAVIS_BUILD_NUMBER reports/Share/$TAG_NAME
cd reports/Share/$TAG_NAME
export LIST=$(ls)
allure generate -c $LIST -o allure/Share/$TAG_NAME
aws s3 sync  allure/Share/$TAG_NAME s3://test-report-allure/final-report/Share/$TAG_NAME
