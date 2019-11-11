call mvn -N versions:update-child-modules
call mvn deploy -Dmaven.test.skip=true -DmyMavenRepoWriteUrl=