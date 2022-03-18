reproducer for https://github.com/quarkusio/quarkus/issues/24395
========================

Reproduce the issue: 

```bash
mvn test
```

You should see:

```log
WARN  [io.qua.ope.run.QuarkusContextStorage] (executor-thread-0) Context in storage not the expected context, Scope.close was not called correctly
```
