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

package com.kingsrook.qbits.sftpdataintegration;


import java.util.List;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.logging.QLogger;
import com.kingsrook.qqq.backend.core.model.metadata.MetaDataProducerHelper;
import com.kingsrook.qqq.backend.core.model.metadata.MetaDataProducerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.QBitMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.QBitProducer;


/*******************************************************************************
 **
 *******************************************************************************/
public class SFTPDataIntegrationQBitProducer implements QBitProducer
{
   private static final QLogger LOG = QLogger.getLogger(SFTPDataIntegrationQBitProducer.class);

   private SFTPDataIntegrationQBitConfig sftpDataIntegrationQBitConfig;



   /***************************************************************************
    **
    ***************************************************************************/
   @Override
   public void produce(QInstance qInstance, String namespace) throws QException
   {
      QBitMetaData qBitMetaData = new QBitMetaData()
         .withGroupId("com.kingsrook.qbits")
         .withArtifactId("sftp-data-integration")
         .withVersion("0.1.2")
         .withNamespace(namespace)
         .withConfig(sftpDataIntegrationQBitConfig);
      qInstance.addQBit(qBitMetaData);

      List<MetaDataProducerInterface<?>> producers = MetaDataProducerHelper.findProducers(getClass().getPackageName());
      finishProducing(qInstance, qBitMetaData, sftpDataIntegrationQBitConfig, producers);
   }



   /*******************************************************************************
    ** Getter for sftpDataIntegrationQBitConfig
    *******************************************************************************/
   public SFTPDataIntegrationQBitConfig getSftpDataIntegrationQBitConfig()
   {
      return (this.sftpDataIntegrationQBitConfig);
   }



   /*******************************************************************************
    ** Setter for sftpDataIntegrationQBitConfig
    *******************************************************************************/
   public void setSftpDataIntegrationQBitConfig(SFTPDataIntegrationQBitConfig sftpDataIntegrationQBitConfig)
   {
      this.sftpDataIntegrationQBitConfig = sftpDataIntegrationQBitConfig;
   }



   /*******************************************************************************
    ** Fluent setter for sftpDataIntegrationQBitConfig
    *******************************************************************************/
   public SFTPDataIntegrationQBitProducer withSftpDataIntegrationQBitConfig(SFTPDataIntegrationQBitConfig sftpDataIntegrationQBitConfig)
   {
      this.sftpDataIntegrationQBitConfig = sftpDataIntegrationQBitConfig;
      return (this);
   }


}
