/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.accumulo.core.gc;

import java.util.Objects;

import org.apache.accumulo.core.data.TableId;
import org.apache.accumulo.core.metadata.ScanServerRefTabletFile;
import org.apache.accumulo.core.metadata.StoredTabletFile;
import org.apache.hadoop.fs.Path;

/**
 * A GC reference used for streaming and delete markers. This type is a file. Subclass is a
 * directory.
 */
public class ReferenceFile implements Reference, Comparable<ReferenceFile> {
  // parts of an absolute URI, like "hdfs://1.2.3.4/accumulo/tables/2a/t-0003"
  public final TableId tableId; // 2a

  // the exact path from the file reference string that is stored in the metadata
  protected final String metadataPath;

  protected ReferenceFile(TableId tableId, String metadataPath) {
    this.tableId = Objects.requireNonNull(tableId);
    this.metadataPath = Objects.requireNonNull(metadataPath);
  }

  public ReferenceFile(TableId tableId, Path metadataPathPath) {
    this.tableId = Objects.requireNonNull(tableId);
    this.metadataPath = Objects.requireNonNull(metadataPathPath.toString());
  }

  public ReferenceFile(TableId tableId, ScanServerRefTabletFile tabletFile) {
    this.tableId = Objects.requireNonNull(tableId);
    this.metadataPath = Objects.requireNonNull(tabletFile.getNormalizedPathStr());
  }

  public ReferenceFile(TableId tableId, StoredTabletFile tabletFile) {
    this.tableId = Objects.requireNonNull(tableId);
    this.metadataPath = Objects.requireNonNull(tabletFile.getMetadataPath());
  }

  @Override
  public boolean isDirectory() {
    return false;
  }

  @Override
  public TableId getTableId() {
    return tableId;
  }

  @Override
  public String getMetadataPath() {
    return metadataPath;
  }

  @Override
  public int compareTo(ReferenceFile that) {
    if (equals(that)) {
      return 0;
    } else {
      return this.metadataPath.compareTo(that.metadataPath);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ReferenceFile other = (ReferenceFile) obj;
    return metadataPath.equals(other.metadataPath);
  }

  @Override
  public int hashCode() {
    return this.metadataPath.hashCode();
  }

  @Override
  public String toString() {
    return "Reference [id=" + tableId + ", ref=" + metadataPath + "]";
  }

}
