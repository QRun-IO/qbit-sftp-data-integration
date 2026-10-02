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

package com.kingsrook.qbits.sftpdataintegration.model;


import java.util.List;
import com.kingsrook.qbits.sftpdataintegration.SFTPDataIntegrationQBitConfig;
import com.kingsrook.qbits.sftpdataintegration.metadata.SFTPImportStagingFileTableMetaDataProducer;
import com.kingsrook.qqq.backend.core.actions.customizers.TableCustomizerInterface;
import com.kingsrook.qqq.backend.core.actions.permissions.PermissionsHelper;
import com.kingsrook.qqq.backend.core.actions.permissions.TablePermissionSubType;
import com.kingsrook.qqq.backend.core.context.QContext;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.actions.tables.QueryOrGetInputInterface;
import com.kingsrook.qqq.backend.core.model.actions.tables.get.GetInput;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.model.metadata.fields.AdornmentType;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.QBitConfig;


/*******************************************************************************
 **
 *******************************************************************************/
public class ImportFileCustomizer implements TableCustomizerInterface
{

   /***************************************************************************
    **
    ***************************************************************************/
   @Override
   public List<QRecord> postQuery(QueryOrGetInputInterface queryInput, List<QRecord> records) throws QException
   {
      String     stagingFileTableName = SFTPImportStagingFileTableMetaDataProducer.NAME;
      QBitConfig sourceQBitConfig     = QContext.getQInstance().getTable(ImportFile.TABLE_NAME).getSourceQBitConfig();
      if(sourceQBitConfig instanceof SFTPDataIntegrationQBitConfig sftpDataIntegrationQBitConfig)
      {
         stagingFileTableName = sftpDataIntegrationQBitConfig.getEffectiveStagingFileTableName();
      }

      if(queryInput.getShouldGenerateDisplayValues() && hasStagingFileTableReadPermission(stagingFileTableName))
      {
         for(QRecord record : records)
         {
            String stagedPath = record.getValueString("stagedPath");
            String baseName   = stagedPath.replaceFirst(".*/", "");

            String url = AdornmentType.FileDownloadValues.makeFieldDownloadUrl(stagingFileTableName, stagedPath, "contents", baseName);
            record.setValue("stagedPath:" + AdornmentType.FileDownloadValues.DOWNLOAD_URL_DYNAMIC, url);
         }
      }

      return (records);
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private static boolean hasStagingFileTableReadPermission(String tableName)
   {
      try
      {
         PermissionsHelper.checkTablePermissionThrowing(new GetInput(tableName), TablePermissionSubType.READ);
         return (true);
      }
      catch(Exception e)
      {
         ///////////////////////////////////////
         // exception indicates no permission //
         ///////////////////////////////////////
      }
      return (false);
   }

}
