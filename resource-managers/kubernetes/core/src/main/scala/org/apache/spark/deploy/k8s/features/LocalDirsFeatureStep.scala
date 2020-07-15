/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.deploy.k8s.features

import java.util.UUID

import scala.collection.JavaConverters._

import io.fabric8.kubernetes.api.model._

import org.apache.spark.deploy.k8s.{KubernetesConf, SparkPod}
import org.apache.spark.deploy.k8s.Config._

private[spark] class LocalDirsFeatureStep(
    conf: KubernetesConf,
    defaultLocalDir: String = s"/var/data/spark-${UUID.randomUUID}")
  extends KubernetesFeatureConfigStep {

  private val useLocalDirTmpFs = conf.get(KUBERNETES_LOCAL_DIRS_TMPFS)

  private val resolvedLocalDirs = Option(conf.sparkConf.getenv("SPARK_LOCAL_DIRS"))
    .orElse(conf.getOption("spark.local.dir"))
    .getOrElse(defaultLocalDir)
    .split(",")

  override def configurePod(pod: SparkPod): SparkPod = {
    val containerWithLocalDirVolumeMounts = new ContainerBuilder(pod.container)
      .addNewEnv()
        .withName("SPARK_LOCAL_DIRS")
        .withValue(resolvedLocalDirs.mkString(","))
        .endEnv()
      .build()
    SparkPod(pod.pod, containerWithLocalDirVolumeMounts)
  }
}
