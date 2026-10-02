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


import java.util.Objects;
import com.kingsrook.qqq.backend.core.model.metadata.fields.AdornmentType;
import com.kingsrook.qqq.backend.core.model.metadata.fields.FieldAdornment;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.possiblevalues.PossibleValueEnum;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.QMetaDataProducingPossibleValueEnum;
import static com.kingsrook.qqq.backend.core.model.metadata.fields.AdornmentType.ChipValues.iconAndColorValues;


/*******************************************************************************
 ** ImportFileStatusEnum - possible value enum
 *******************************************************************************/
@QMetaDataProducingPossibleValueEnum
public enum ImportFileStatusEnum implements PossibleValueEnum<Integer>
{
   PENDING(1, "Pending"),
   PROCESSING(2, "Processing"),
   COMPLETE(3, "Complete"),
   ERROR(4, "Error");

   private final Integer id;
   private final String  label;

   public static final String NAME = "ImportFileStatusEnum";



   /***************************************************************************
    *
    ***************************************************************************/
   public static void addChipAdornmentToField(QFieldMetaData field)
   {
      field.withFieldAdornment(new FieldAdornment(AdornmentType.CHIP)
         .withValues(iconAndColorValues(PENDING, "pending", AdornmentType.ChipValues.COLOR_DEFAULT))
         .withValues(iconAndColorValues(PROCESSING, "double_arrow", AdornmentType.ChipValues.COLOR_INFO))
         .withValues(iconAndColorValues(COMPLETE, "done", AdornmentType.ChipValues.COLOR_SUCCESS))
         .withValues(iconAndColorValues(ERROR, "error", AdornmentType.ChipValues.COLOR_ERROR)));
   }



   /*******************************************************************************
    **
    *******************************************************************************/
   ImportFileStatusEnum(Integer id, String label)
   {
      this.id = id;
      this.label = label;
   }



   /*******************************************************************************
    ** Get instance by id
    **
    *******************************************************************************/
   public static ImportFileStatusEnum getById(Integer id)
   {
      if(id == null)
      {
         return (null);
      }

      for(ImportFileStatusEnum value : ImportFileStatusEnum.values())
      {
         if(Objects.equals(value.id, id))
         {
            return (value);
         }
      }

      return (null);
   }



   /*******************************************************************************
    ** Getter for id
    **
    *******************************************************************************/
   public Integer getId()
   {
      return id;
   }



   /*******************************************************************************
    ** Getter for label
    **
    *******************************************************************************/
   public String getLabel()
   {
      return label;
   }



   /*******************************************************************************
    **
    *******************************************************************************/
   @Override
   public Integer getPossibleValueId()
   {
      return (getId());
   }



   /*******************************************************************************
    **
    *******************************************************************************/
   @Override
   public String getPossibleValueLabel()
   {
      return (getLabel());
   }
}
