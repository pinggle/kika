## 编译:  
./gradlew clean build -x test  

## 测试:  
写入: msg=hello  
curl -v -H "Content-Type: text/plain" -X POST http://localhost:8080/api/kika/msg -d hello  
读取: msg  
curl -iv -X GET -H "Content-Type: text/plain" http://localhost:8080/api/kika/msg  
删除: msg  
curl -X DELETE http://localhost:8080/api/kika/msg  


