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

package com.kingsrook.qbits.sftpdataintegration.process;


import java.util.List;
import com.kingsrook.qbits.sftpdataintegration.BaseTest;
import com.kingsrook.qbits.sftpdataintegration.metadata.RenderReportForSFTPExportProcessMetaDataProducer;
import com.kingsrook.qbits.sftpdataintegration.metadata.SFTPExportDestinationFileTableMetaDataProducer;
import com.kingsrook.qbits.sftpdataintegration.model.SFTPConnection;
import com.kingsrook.qbits.sftpdataintegration.model.SFTPExportConfig;
import com.kingsrook.qqq.backend.core.actions.processes.QProcessCallbackFactory;
import com.kingsrook.qqq.backend.core.actions.processes.RunProcessAction;
import com.kingsrook.qqq.backend.core.actions.tables.InsertAction;
import com.kingsrook.qqq.backend.core.actions.tables.QueryAction;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.actions.processes.RunProcessInput;
import com.kingsrook.qqq.backend.core.model.actions.processes.RunProcessOutput;
import com.kingsrook.qqq.backend.core.model.actions.tables.insert.InsertInput;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.model.savedreports.SavedReport;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


/*******************************************************************************
 **
 *******************************************************************************/
public class RenderReportForSFTPExportProcessTest extends BaseTest
{
   /*******************************************************************************
    **
    *******************************************************************************/
   @Test
   void test() throws QException
   {
      Integer savedReportId = new InsertAction().executeForRecord(new InsertInput(SavedReport.TABLE_NAME).withRecordEntity(new SavedReport()
         .withTableName(SFTPConnection.TABLE_NAME)
         .withLabel("sftp conns report")
         .withQueryFilterJson("{}")
         .withColumnsJson("""
            {"columns":[{"name":"id","isVisible":true,"width":75,"pinned":"left"},{"name":"name","isVisible":true,"width":75,"pinned":"left"}]}
            """)
      )).getValueInteger("id");

      Integer sftpConnectionId = new InsertAction().executeForRecord(new InsertInput(SFTPConnection.TABLE_NAME).withRecordEntity(new SFTPConnection()
         .withName("Test Conn")
         .withHostname("localhost")
         .withIsActive(true)
      )).getValueInteger("id");

      QRecord sftpExportConfigRecord = new InsertAction().executeForRecord(new InsertInput(SFTPExportConfig.TABLE_NAME).withRecordEntity(new SFTPExportConfig()
         .withName("Test Config")
         .withSftpConnectionId(sftpConnectionId)
         .withSavedReportId(savedReportId)
         .withFormat("CSV")
         .withIsActive(true)
      ));

      RunProcessOutput runProcessOutput = runProcess(sftpExportConfigRecord);

      List<QRecord> exportedFileRecords = QueryAction.execute(SFTPExportDestinationFileTableMetaDataProducer.NAME, null);
      assertEquals(1, exportedFileRecords.size());
      assertThat(exportedFileRecords.get(0).getValueString("fileName")).matches("sftp conns report - 20..-..-..-....\\.csv");
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private static RunProcessOutput runProcess(QRecord sftpExportConfigRecord) throws QException
   {
      RunProcessInput input = new RunProcessInput();
      input.setProcessName(RenderReportForSFTPExportProcessMetaDataProducer.NAME);
      input.setCallback(QProcessCallbackFactory.forRecord(sftpExportConfigRecord));
      input.setFrontendStepBehavior(RunProcessInput.FrontendStepBehavior.SKIP);
      RunProcessOutput runProcessOutput = new RunProcessAction().execute(input);
      return runProcessOutput;
   }

}
