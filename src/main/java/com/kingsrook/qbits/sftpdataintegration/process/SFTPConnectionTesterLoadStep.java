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


import java.util.ArrayList;
import com.kingsrook.qbits.sftpdataintegration.model.SFTPConnection;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.instances.QMetaDataVariableInterpreter;
import com.kingsrook.qqq.backend.core.logging.QLogger;
import com.kingsrook.qqq.backend.core.model.actions.processes.ProcessSummaryLine;
import com.kingsrook.qqq.backend.core.model.actions.processes.ProcessSummaryLineInterface;
import com.kingsrook.qqq.backend.core.model.actions.processes.RunBackendStepInput;
import com.kingsrook.qqq.backend.core.model.actions.processes.RunBackendStepOutput;
import com.kingsrook.qqq.backend.core.model.actions.processes.Status;
import com.kingsrook.qqq.backend.core.processes.implementations.etl.streamedwithfrontend.AbstractLoadStep;
import com.kingsrook.qqq.backend.core.processes.implementations.etl.streamedwithfrontend.ProcessSummaryProviderInterface;
import com.kingsrook.qqq.backend.core.utils.StringUtils;
import com.kingsrook.qqq.backend.module.filesystem.sftp.actions.AbstractSFTPAction;
import com.kingsrook.qqq.backend.module.filesystem.sftp.actions.SFTPTestConnectionAction;
import org.apache.commons.lang3.BooleanUtils;


/*******************************************************************************
 ** Test SFTP Connections
 *******************************************************************************/
public class SFTPConnectionTesterLoadStep extends AbstractLoadStep implements ProcessSummaryProviderInterface
{
   private static final QLogger LOG = QLogger.getLogger(SFTPConnectionTesterLoadStep.class);

   private ArrayList<ProcessSummaryLineInterface> summaryLines = new ArrayList<>();



   /*******************************************************************************
    **
    *******************************************************************************/
   @Override
   public void runOnePage(RunBackendStepInput runBackendStepInput, RunBackendStepOutput runBackendStepOutput) throws QException
   {
      //////////////////////////////////////////////////////////////////////
      // add a private key for our server if we see an env var w/ a value //
      //////////////////////////////////////////////////////////////////////
      byte[] privateKeyBytes          = null;
      String sftpPrivateKeyEnvVarName = runBackendStepInput.getValueString("sftpPrivateKeyEnvVarName");
      if(StringUtils.hasContent(sftpPrivateKeyEnvVarName))
      {
         try
         {
            String pem = new QMetaDataVariableInterpreter().interpret("${env." + sftpPrivateKeyEnvVarName + "}");
            if(StringUtils.hasContent(pem))
            {
               privateKeyBytes = AbstractSFTPAction.pemStringToDecodedBytes(pem);
            }
         }
         catch(Exception e)
         {
            String message = "There was an error interpreting the application server's SFTP private key, which will cause public-key based authentication to fail.";
            LOG.warn(message, e);
            summaryLines.add(new ProcessSummaryLine(Status.WARNING, null, message));
         }
      }

      for(SFTPConnection sftpConnection : runBackendStepInput.getRecordsAsEntities(SFTPConnection.class))
      {
         SFTPTestConnectionAction.SFTPTestConnectionTestInput input = new SFTPTestConnectionAction.SFTPTestConnectionTestInput()
            .withUsername(sftpConnection.getUsername())
            .withPassword(sftpConnection.getPassword())
            .withHostName(sftpConnection.getHostname())
            .withPort(sftpConnection.getPort())
            .withBasePath(sftpConnection.getBasePath())
            .withPrivateKey(privateKeyBytes);

         SFTPTestConnectionAction.SFTPTestConnectionTestOutput output = new SFTPTestConnectionAction().testConnection(input);

         String recordLabel = sftpConnection.getName() + " (Id " + sftpConnection.getId() + ")";
         if(output.getIsConnectionSuccess())
         {
            if(BooleanUtils.isFalse(output.getIsListBasePathSuccess()))
            {
               summaryLines.add(new ProcessSummaryLine(Status.WARNING, null, recordLabel + " connected, but failed to read the base path: " + output.getListBasePathErrorMessage()));
            }
            else
            {
               summaryLines.add(new ProcessSummaryLine(Status.OK, null, recordLabel + " connected successfully"));
            }
         }
         else
         {
            summaryLines.add(new ProcessSummaryLine(Status.ERROR, null, recordLabel + " failed to connect: " + output.getConnectionErrorMessage()));
         }
      }
   }



   /***************************************************************************
    **
    ***************************************************************************/
   @Override
   public ArrayList<ProcessSummaryLineInterface> getProcessSummary(RunBackendStepOutput runBackendStepOutput, boolean isForResultScreen)
   {
      return (summaryLines);
   }
}
