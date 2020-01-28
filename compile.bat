call mvn -N versions:update-child-modules
call mvn clean deploy -Dmaven.test.skip=true -DmyMavenRepoWriteUrl=