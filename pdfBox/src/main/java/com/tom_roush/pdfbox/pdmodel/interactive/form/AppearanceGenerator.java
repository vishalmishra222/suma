/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tom_roush.pdfbox.pdmodel.interactive.form;

import java.io.IOException;

import com.tom_roush.pdfbox.pdmodel.common.PDTextStream;

import android.util.Log;

/**
 * (Re-) Generate the appearance for a field.
 * 
 * The fields appearance defines the 'look' the field has when it's rendered for display or printing.
 */
public final class AppearanceGenerator
{
	private AppearanceGenerator()
	{
	}

	/**
	 * Generate the appearances for a single field.
	 * 
	 * @param field The field which appearances need to be generated.
	 * @throws IOException
	 */
	public static void generateFieldAppearances(PDField field) throws IOException
	{
		if (field instanceof PDVariableText)
		{
			AppearanceGeneratorHelper apHelper = null;

			Object fieldValue = null;
			apHelper = new AppearanceGeneratorHelper(field.getAcroForm(),
					(PDVariableText) field);

			fieldValue = field.getValue();

			// TODO: implement the handling for additional values.
			if (fieldValue instanceof String)
			{
				apHelper.setAppearanceValue((String) fieldValue);
			}
			else if (fieldValue instanceof PDTextStream)
			{
				apHelper.setAppearanceValue(((PDTextStream) fieldValue).getAsString());
			}
			else if (fieldValue != null)
			{
				Log.d("PdfBoxAndroid", "Can't generate the appearance for values typed "
						+ fieldValue.getClass().getName() + ".");
			}
		}
		// TODO: implement the handling for additional field types
		else
		{
			Log.d("PdfBoxAndroid", "Unable to generate the field appearance.");
		}
	}
}