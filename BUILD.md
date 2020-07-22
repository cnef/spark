# Create build image

```shell
# cd dev/create-release/spark-rm
# docker build -t build-spark-2 ./
```


# Build sprk

[build spark](https://spark.apache.org/docs/latest/building-spark.html)

```shell
# docker run -it --rm -e MAVEN_OPTS="-Xmx16g -XX:ReservedCodeCacheSize=2g" -w /root/spark -v sparksource:/root/spark build-spark-2
# time ./dev/make-distribution.sh --name custom-spark-2 --pip --tgz -Psparkr -Phadoop-2.7 -Phive -Phive-thriftserver -Pmesos -Pyarn -Pkubernetes
```

# Build spark image

```shell
# cd sparksource
# ./bin/docker-image-tool.sh -r registry.vizion.ai/library/ml -t 2.4-$(git rev-parse --short HEAD) build
# docker push registry.vizion.ai/library/ml/spark-py:2.4-$(git rev-parse --short HEAD)
```
