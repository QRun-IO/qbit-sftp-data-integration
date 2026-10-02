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
import com.kingsrook.qbits.sftpdataintegration.process.ImportFileBulkLoadLoadStep;
import com.kingsrook.qbits.sftpdataintegration.process.ImportFileBulkLoadTransformStep;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldType;
import com.kingsrook.qqq.backend.core.model.metadata.processes.QProcessMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.QBitComponentMetaDataProducer;
import com.kingsrook.qqq.backend.core.processes.implementations.etl.streamedwithfrontend.ExtractViaQueryStep;
import com.kingsrook.qqq.backend.core.processes.implementations.etl.streamedwithfrontend.StreamedETLWithFrontendProcess;
import com.kingsrook.qqq.backend.core.processes.tracing.ProcessTracerInterface;


/*******************************************************************************
 ** Meta Data Producer for ImportFileBulkLoad
 *******************************************************************************/
public class ImportFileBulkLoadProcessMetaDataProducer extends QBitComponentMetaDataProducer<QProcessMetaData, SFTPDataIntegrationQBitConfig>
{
   public static final String NAME = "ImportFileBulkLoadProcess";

   public static final String FIELD_STAGING_TABLE                           = "stagingTableName";
   public static final String FIELD_BULK_LOAD_PROCESS_TRACER_CODE_REFERENCE = "bulkLoadProcessTracerCodeReference";



   /*******************************************************************************
    **
    *******************************************************************************/
   @Override
   public QProcessMetaData produce(QInstance qInstance) throws QException
   {
      SFTPDataIntegrationQBitConfig qBitConfig = getQBitConfig();

      QProcessMetaData processMetaData = StreamedETLWithFrontendProcess.processMetaDataBuilder()
         .withName(NAME)
         .withTableName(ImportFile.TABLE_NAME)
         .withSourceTable(ImportFile.TABLE_NAME)
         .withDestinationTable(ImportFile.TABLE_NAME)
         .withExtractStepClass(ExtractViaQueryStep.class)
         .withTransformStepClass(ImportFileBulkLoadTransformStep.class)
         .withLoadStepClass(ImportFileBulkLoadLoadStep.class)
         .withFields(List.of(
            new QFieldMetaData(FIELD_STAGING_TABLE, QFieldType.STRING)
               .withDefaultValue(qBitConfig.getEffectiveStagingFileTableName()),

            new QFieldMetaData(FIELD_BULK_LOAD_PROCESS_TRACER_CODE_REFERENCE, QFieldType.STRING)
               .withDefaultValue(qBitConfig.getProcessTracerCodeReference()),

            new QFieldMetaData(FIELD_BULK_LOAD_PROCESS_TRACER_CODE_REFERENCE + "_expectedType", QFieldType.STRING)
               .withDefaultValue(ProcessTracerInterface.class.getName())
         ))
         .withReviewStepRecordFields(List.of(
            new QFieldMetaData("id", QFieldType.INTEGER),
            new QFieldMetaData("sftpImportConfigId", QFieldType.INTEGER).withPossibleValueSourceName(SFTPImportConfig.TABLE_NAME),
            new QFieldMetaData("sourcePath", QFieldType.STRING)
         ))
         .withPreviewMessage(StreamedETLWithFrontendProcess.DEFAULT_PREVIEW_MESSAGE_PREFIX + " processed")
         .withTransactionLevelAutoCommit()
         .getProcessMetaData();

      return (processMetaData);
   }

}
