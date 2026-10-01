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
import com.kingsrook.qbits.sftpdataintegration.SFTPDataIntegrationQBitConfig;
import com.kingsrook.qbits.sftpdataintegration.model.ImportFile;
import com.kingsrook.qbits.sftpdataintegration.model.SFTPImportConfig;
import com.kingsrook.qbits.sftpdataintegration.process.SFTPImportFileSyncExtractStep;
import com.kingsrook.qbits.sftpdataintegration.process.SFTPImportFileSyncLoadStep;
import com.kingsrook.qbits.sftpdataintegration.process.SFTPImportFileSyncTransformStep;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.metadata.QBackendMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldType;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QIcon;
import com.kingsrook.qqq.backend.core.model.metadata.processes.QProcessMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.processes.VariantRunStrategy;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.QBitComponentMetaDataProducer;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;
import com.kingsrook.qqq.backend.core.processes.implementations.etl.streamedwithfrontend.StreamedETLWithFrontendProcess;


/*******************************************************************************
 ** Meta Data Producer for SFTPImportFileSync
 **
 ** This process exists on the SFTP Import Config table.
 **
 ** This process gets scheduled to run every-so-often, looking for new files in
 ** the SFTP source table associated with the connection, and then sync'ing them
 ** to the staging filesystem table and the ImportFile database table.
 *******************************************************************************/
public class SFTPImportFileSyncProcessMetaDataProducer extends QBitComponentMetaDataProducer<QProcessMetaData, SFTPDataIntegrationQBitConfig>
{
   public static final String NAME = "SFTPImportFileSyncProcess";



   /***************************************************************************
    **
    ***************************************************************************/
   @Override
   public int getSortOrder()
   {
      /////////////////////////////////////////////////////////////
      // needs to run after source-file-table, so, make that be. //
      /////////////////////////////////////////////////////////////
      return new SFTPImportSourceFileTableMetaDataProducer().getSortOrder() + 1;
   }



   /*******************************************************************************
    **
    *******************************************************************************/
   @Override
   public QProcessMetaData produce(QInstance qInstance) throws QException
   {
      SFTPDataIntegrationQBitConfig qBitConfig          = getQBitConfig();
      String                        sourceFileTableName = qBitConfig.getEffectiveSourceFileTableName();

      QProcessMetaData processMetaData = StreamedETLWithFrontendProcess.processMetaDataBuilder()
         .withName(NAME)
         .withLabel("SFTP Import File Sync")
         .withIcon(new QIcon().withName("cloud_sync"))
         .withTableName(SFTPImportConfig.TABLE_NAME)
         .withSourceTable(sourceFileTableName)
         .withDestinationTable(ImportFile.TABLE_NAME)
         .withExtractStepClass(SFTPImportFileSyncExtractStep.class)
         .withTransformStepClass(SFTPImportFileSyncTransformStep.class)
         .withLoadStepClass(SFTPImportFileSyncLoadStep.class)
         .withReviewStepRecordFields(List.of(
            new QFieldMetaData("sourcePath", QFieldType.STRING),
            new QFieldMetaData("stagedPath", QFieldType.STRING)
         ))
         .withTransactionLevelAutoCommit()
         .getProcessMetaData();

      //////////////////////////////////////////////////////////////////////////////////
      // if the source-file table uses variants, set that variant data on the process //
      //////////////////////////////////////////////////////////////////////////////////
      QTableMetaData   sourceFileTable   = qInstance.getTable(sourceFileTableName);
      QBackendMetaData sourceFileBackend = qInstance.getBackend(sourceFileTable.getBackendName());
      if(sourceFileBackend.getUsesVariants())
      {
         processMetaData.withVariantBackend(sourceFileBackend.getName());
         processMetaData.withVariantRunStrategy(VariantRunStrategy.SERIAL);
      }

      processMetaData.setProcessTracerCodeReference(getQBitConfig().getProcessTracerCodeReference());

      return (processMetaData);
   }

}
