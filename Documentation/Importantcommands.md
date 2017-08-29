Project Related:
./gradlew clean build to build the project
./gradlew bootRun to run the spring application.

Mongo:
1. enter "mongod" to start mongo
2. enter "mongo" to enter mongo shell
3. "show dbs" : To list all dbs
4. "use <dbname>": to run queries on that db
5. "show collections" : to list collections in that db
6. "db.<collection>.find()": to list all collections in that db
7. "db.<collection>.remove({})": to truncate the collection
8. "db.<collection>.drop()" : to drop a collection
9. "db.<collection>.getIndexes()" : to list indexes in a collection
10. "db['<collection>'].find({})" : to handle special chars in collection name.

git:

1. to find out number of lines in git repo : git ls-files | xargs cat|wc -l
2. to find out number of lines per file : git ls-files | xargs wc -l

//greping service logs

journalctl -u studentadda -b --no-pager | grep -B 10 -A 3 "status is"