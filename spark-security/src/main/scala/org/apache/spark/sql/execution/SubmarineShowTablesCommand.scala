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

package org.apache.spark.sql.execution

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.execution.command.{RunnableCommand, ShowTablesCommand}

import org.apache.submarine.spark.security.{RangerSparkAuthorizer, SparkPrivilegeObject, SparkPrivilegeObjectType}

case class SubmarineShowTablesCommand(child: ShowTablesCommand) extends RunnableCommand {

  override val output: Seq[Attribute] = child.output
  override def run(sparkSession: SparkSession): Seq[Row] = {
    val rows = child.run(sparkSession)
    rows.filter(r => RangerSparkAuthorizer.isAllowed(toSparkPrivilegeObject(r)))
  }

  private def toSparkPrivilegeObject(row: Row): SparkPrivilegeObject = {
    val database = row.getString(0)
    val table = row.getString(1)
    new SparkPrivilegeObject(SparkPrivilegeObjectType.TABLE_OR_VIEW, database, table)
  }
}
