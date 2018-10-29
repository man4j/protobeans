call mvn versions:update-child-modules
call mvn clean install deploy:deploy -Dmaven.test.skip=true