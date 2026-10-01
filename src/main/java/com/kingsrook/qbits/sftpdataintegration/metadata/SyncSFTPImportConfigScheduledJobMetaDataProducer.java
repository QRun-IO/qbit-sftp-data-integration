/*
 * QQQ - Low-code Application Framework for Engineers.
 * Copyright (C) 2021-2025.  Kingsrook, LLC
 * 651 N Broad St Ste 205 # 6917 | Middletown DE 19709 | United States
 * contact@kingsrook.com
 * https://github.com/Kingsrook/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kingsrook.qbits.sftpdataintegration.metadata;


import java.util.List;
import com.kingsrook.qbits.sftpdataintegration.model.SFTPImportConfig;
import com.kingsrook.qbits.sftpdataintegration.process.SyncSFTPImportConfigScheduledJobTransformStep;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.metadata.MetaDataProducer;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldType;
import com.kingsrook.qqq.backend.core.model.metadata.processes.QProcessMetaData;
import com.kingsrook.qqq.backend.core.processes.implementations.tablesync.TableSyncProcess;


/*******************************************************************************
 ** Meta Data Producer for SyncSFTPImportConfigScheduledJob
 *******************************************************************************/
public class SyncSFTPImportConfigScheduledJobMetaDataProducer extends MetaDataProducer<QProcessMetaData>
{
   public static final String NAME = "syncSFTPImportConfigScheduledJob";



   /*******************************************************************************
    **
    *******************************************************************************/
   @Override
   public QProcessMetaData produce(QInstance qInstance) throws QException
   {
      QProcessMetaData processMetaData = TableSyncProcess.processMetaDataBuilder(false)
         .withName(NAME)
         .withSyncTransformStepClass(SyncSFTPImportConfigScheduledJobTransformStep.class)
         .withReviewStepRecordFields(List.of(
            new QFieldMetaData("sftpImportConfigId", QFieldType.INTEGER).withPossibleValueSourceName(SFTPImportConfig.TABLE_NAME),
            new QFieldMetaData("cronExpression", QFieldType.STRING)
         ))
         .getProcessMetaData()
         .withIsHidden(true);

      return (processMetaData);
   }

}
