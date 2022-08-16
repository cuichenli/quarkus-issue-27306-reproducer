reproducer for https://github.com/quarkusio/quarkus/issues/27306
========================

Reproduce the issue: 

```bash
mvn test
```

You should see logs like this where `after calling sayHello` does not have trace id:

```log
server interceptor
sayHello 80fd45bfae72bd14be3e8416dbfc9fc1
client interceptor
server interceptor
sayHello 80fd45bfae72bd14be3e8416dbfc9fc1
skipped 80fd45bfae72bd14be3e8416dbfc9fc1
after calling sayHello 00000000000000000000000000000000
```
