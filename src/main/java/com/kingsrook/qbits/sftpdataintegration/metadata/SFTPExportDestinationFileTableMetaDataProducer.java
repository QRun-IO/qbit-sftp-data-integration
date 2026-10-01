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


import com.kingsrook.qbits.sftpdataintegration.SFTPDataIntegrationQBitConfig;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QIcon;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.QBitComponentMetaDataProducer;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;
import com.kingsrook.qqq.backend.module.filesystem.base.model.metadata.FilesystemTableMetaDataBuilder;


/*******************************************************************************
 ** Meta Data Producer for SFTPExportFileDestinationTable
 *******************************************************************************/
public class SFTPExportDestinationFileTableMetaDataProducer extends QBitComponentMetaDataProducer<QTableMetaData, SFTPDataIntegrationQBitConfig>
{
   public static final String NAME = "SFTPExportDestinationFileTable";



   /***************************************************************************
    **
    ***************************************************************************/
   @Override
   public boolean isEnabled()
   {
      return (getQBitConfig().getDestinationFileTableConfig().getDoProvideTable());
   }



   /*******************************************************************************
    **
    *******************************************************************************/
   @Override
   public QTableMetaData produce(QInstance qInstance) throws QException
   {
      QTableMetaData table = new FilesystemTableMetaDataBuilder()
         .withName(NAME)
         .withBackend(qInstance.getBackend(getQBitConfig().getDestinationFileTableConfig().getBackendName()))
         .buildStandardCardinalityOneTable()
         .withLabel("SFTP Export Destination File")
         .withIcon(new QIcon("drive_folder_upload"));

      return (table);
   }

}
