source _ci/init_tag.sh

aws s3 sync s3://test-report-allure/$TRAVIS_BUILD_NUMBER reports/$TRAVIS_BUILD_NUMBER
cd reports/$TRAVIS_BUILD_NUMBER
export LIST=$(ls)

echo $LIST

allure generate -c $LIST -o allure/$TRAVIS_BUILD_NUMBER
aws s3 sync  allure/$TRAVIS_BUILD_NUMBER s3://test-report-allure/final-report/$TRAVIS_BUILD_NUMBER
