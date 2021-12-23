/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.spark.sql.catalyst.optimizer

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.command.SetCommand

import org.apache.submarine.spark.security.SparkAccessControlException

/**
 * For banning end-users from set restricted spark configurations
 */
case class SubmarineConfigurationCheckExtension(spark: SparkSession)
  extends (LogicalPlan => Unit) {

  final val RESTRICT_LIST_KEY = "spark.sql.submarine.conf.restricted.list"

  private val bannedList: Seq[String] =
    RESTRICT_LIST_KEY ::
      "spark.sql.runSQLOnFiles" ::
      "spark.sql.extensions" ::
      spark.conf.getOption(RESTRICT_LIST_KEY).map(_.split(',').toList).getOrElse(Nil)

  override def apply(plan: LogicalPlan): Unit = plan match {
    case SetCommand(Some(("spark.sql.optimizer.excludedRules", Some(v))))
        if v.contains("Submarine") =>
      throw new SparkAccessControlException("Excluding Submarine security rules is not allowed")
    case SetCommand(Some((k, Some(_)))) if bannedList.contains(k) =>
      throw new SparkAccessControlException(s"Modifying $k is not allowed")
    case _ =>
  }
}
