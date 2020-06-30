package com.app.dusmile.common;

import android.content.Context;
import android.util.Log;

import com.app.dusmile.DBModel.ClientTemplate;
import com.app.dusmile.DBModel.DynamicTableField;
import com.app.dusmile.DBModel.SubCategory;
import com.app.dusmile.DBModel.TemplateJson;
import com.app.dusmile.DBModel.UpdatedTemplet;
import com.app.dusmile.R;
import com.app.dusmile.database.CategoryDB;
import com.app.dusmile.database.ClientTemplateDB;
import com.app.dusmile.database.DynamicFieldTableDB;
import com.app.dusmile.database.LoginTemplateDB;
import com.app.dusmile.database.SubCategoryDB;
import com.app.dusmile.database.TemplateJsonDB;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.example.sumaforms2.AssignedJobs;
import com.example.sumaforms2.AssignedJobsDB;
import com.example.sumaforms2.DBHelper;
import com.example.sumaforms2.SubProcessFieldDataResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sumasoft on 17/03/17.
 */

public class TemplateOperations {
    private static DBHelper dbHelper;
    static Context mContext;
    private static String Tag = "TemplateOperations";
    private static Gson gson = new Gson();
    private static List<Integer> clientId = null;

    public static List<Integer> saveUpdatedTemplateInDatabase(UpdatedTemplet response, Context context) {
        try {
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Saving Template in DB");
            mContext = context;
            dbHelper = DBHelper.getInstance(mContext);
            List<UpdatedTemplet.SubProcessFieldsDatum> subProcessFieldsData = null;
            UpdatedTemplet.TabData tabData = null;
            clientId = new ArrayList<>();
            subProcessFieldsData = response.getSubProcessFieldsData();
            tabData = response.getTabData();

           // UpdatedTemplet tabsDataResponse = gson.fromJson(tabData., UpdatedTemplet.class);
            List<UpdatedTemplet.TabData> arrayListTemplateMaster = Collections.singletonList(tabData);
            if (arrayListTemplateMaster.size() > 0) {   //check for is any template is updated or not
                //iteration for templatetabsDataResponse = {TabsDataResponse@13762} MasterList
                for (int templateMasterCnt = 0; templateMasterCnt < arrayListTemplateMaster.size(); templateMasterCnt++) {
                    String templateName = arrayListTemplateMaster.get(templateMasterCnt).getTemplateName();
                    String nbfcName = arrayListTemplateMaster.get(templateMasterCnt).getNbfcname();
                    String app_language = UserPreference.getLanguage(mContext);
                    String version_no = arrayListTemplateMaster.get(templateMasterCnt).getVersionNo();
                    clientId.add(Double.valueOf(version_no).intValue());
                    //check for template with client name,template name and version is exist in db
                    ClientTemplate clientTemplate = ClientTemplateDB.getSingleClientTemplateAccVerionNo(dbHelper, templateName, nbfcName, app_language, version_no);
                    UpdatedTemplet.TabData templateMaster = arrayListTemplateMaster.get(templateMasterCnt);
                    if (clientTemplate.getID() == null) { //if not exist then do insert/update
                        //check template with clientName and language exist in table
                        clientTemplate = ClientTemplateDB.getSingleClientTemplateByTemplateNameClientLanguage(dbHelper, templateName, nbfcName, app_language, clientTemplate.getIs_deprecated());

                        if (clientTemplate.getID() != null) { //template is exist table
                            //check for any assigned job is using this template or not
                            String client_template_id = clientTemplate.getID();
                            AssignedJobs assignedJobs = AssignedJobsDB.getSingleAssignedJobs(dbHelper, client_template_id);
                            if (assignedJobs.getID() != null) { //insert template
                                insertTemplateInTable(templateMaster, subProcessFieldsData);
                                setClientTemplateDeprecated(clientTemplate);
                            } else {  //update template
                                updateTemplateOfTable(clientTemplate, templateMaster, subProcessFieldsData);
                            }
                        } else { //template is not exist in table
                            insertTemplateInTable(templateMaster, subProcessFieldsData);
                        }
                    } else {
                        //do nothing
                        insertTemplateInTable(templateMaster, subProcessFieldsData);
                    }
                }
            }
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Saved Template in DB");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientId;
    }

    private static void setClientTemplateDeprecated(ClientTemplate clientTemplate) {
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Setting template as deprecated");
        clientTemplate.setIs_deprecated("true");
        ClientTemplateDB.updateClientTemplate(clientTemplate, dbHelper);
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Set template as deprecated");
    }

    private static void updateTemplateOfTable(ClientTemplate clientTemplate, UpdatedTemplet.TabData templateMaster, List<UpdatedTemplet.SubProcessFieldsDatum> subProcessFieldsData) {
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Updating Template in DB");
        TemplateJson templateJson = null;
        DynamicTableField dynamicFieldTable = null;
        List<String> arrayListFormNames = templateMaster.getTabs().get(0).getSubProcesses();
        //-------Start of template to be updated in client_template table ------///
        String form_array = arrayListFormNames.toString();
        clientTemplate.setVersion(templateMaster.getVersionNo());
        clientTemplate.setTemplate_form_name_array(form_array);
        if (clientTemplate.getID() != null) {
            ClientTemplateDB.updateClientTemplate(clientTemplate, dbHelper);
        }
        //-------End of template to be updated in client_template table ------///

        //-------Start of field to be updated in json_template,field_table ------///
        UpdatedTemplet.SubProcessFieldsDatum subProcessFieldDataResponse = gson.fromJson(String.valueOf(subProcessFieldsData), UpdatedTemplet.SubProcessFieldsDatum.class);
        List<UpdatedTemplet.SubProcessFieldsDatum> arrayListTemplateField = Collections.singletonList(subProcessFieldDataResponse);
        for (int formNameCnt = 0; formNameCnt < arrayListFormNames.size(); formNameCnt++) {
            String formName = arrayListFormNames.get(formNameCnt);
            String templateName = clientTemplate.getTemplate_name();
            for (int templateFieldCnt = 0; templateFieldCnt < arrayListTemplateField.size(); templateFieldCnt++) {
                String fieldFormName = subProcessFieldDataResponse.getSubProcessName();
                String fieldTemplateName = arrayListTemplateField.get(templateFieldCnt).getAsssociatedTemplateIDs().toString();
                //check for equality
                if (formName.equalsIgnoreCase(fieldFormName) && fieldTemplateName.equalsIgnoreCase(templateName)) {
                    //get row to be updated from json_template table
                    templateJson = TemplateJsonDB.getSingleTemplateJson(dbHelper, clientTemplate.getID(), fieldFormName);
                    UpdatedTemplet.SubProcessFieldsDatum templateField = arrayListTemplateField.get(templateFieldCnt);
                    String templateFieldJson = gson.toJson(templateField);
                    String jsonTemplateId = null;
                    if (templateJson.getID() != null) {
                        //update template json fields
                        jsonTemplateId = updateTemplateJsonFields(templateJson, templateField, templateFieldJson);

                    } else {
                        //insert template json fields
                       // jsonTemplateId = (String) insertTemplateJsonFields(templateField, templateFieldJson, clientTemplate.getID());
                    }
                    //check for table exist and enter all data in field_table
                    if (templateField.getTableExist()) {
                        //get row to be updated from field_table
                        dynamicFieldTable = DynamicFieldTableDB.getSingleDynamicFieldTable(dbHelper, templateJson.getID());
                        if (dynamicFieldTable.getID() != null) {
                            //update dynamic table entry
                           // updateDynamicFieldTable(dynamicFieldTable, templateField);
                        } else {
                            //insert dynamic table entry
                            insertDynamicFieldTable(templateField, jsonTemplateId);
                        }

                    }

                }
            }
        }
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Updated Template in DB");
        //-------End of field to be updated in json_template,field_table ------///
    }

    private static void insertDynamicFieldTable(UpdatedTemplet.SubProcessFieldsDatum templateField, String jsonTemplateId) {
        /*IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Inserting DynamicFieldTable");
        DynamicTableField dynamicFieldTable = new DynamicTableField();
        String tableName = templateField.getFieldName();
        //String tableHeaderArray = templateField.getTableHeaders().toString();
        String tableKeysArray = templateField.getKey();
        boolean tableManditoryFields = templateField.getIsMandatory();
        //String tableManditoryHeaders = templateField.getMandatoryTableHeaders().toString();
        String tableClearSubProcesFields = templateField.getDependentField();

        dynamicFieldTable.setTable_name(tableName);
      //  dynamicFieldTable.setTable_headers_array(tableHeaderArray);
        dynamicFieldTable.setTable_keys_array(tableKeysArray);
        dynamicFieldTable.setMandatory_table_fields(tableManditoryFields);
       // dynamicFieldTable.setMandatory_table_headers(tableManditoryHeaders);
        dynamicFieldTable.setClear_subprocess_fields(tableClearSubProcesFields);
        dynamicFieldTable.setTemplate_json_id(jsonTemplateId);
        DynamicFieldTableDB.addDynamicFieldTableEntry(dynamicFieldTable, dbHelper);
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Inserted DynamicFieldTable");*/
    }

    private static void updateDynamicFieldTable(DynamicTableField dynamicFieldTable, SubProcessFieldDataResponse.TemplateField templateField) {
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Updating DynamicFieldTable");
        String tableName = templateField.getTableID();
        String tableHeaderArray = templateField.getTableHeaders().toString();
        String tableKeysArray = templateField.getTableFieldKeys().toString();
        String tableManditoryFields = templateField.getMandatoryTableFields().toString();
        String tableManditoryHeaders = templateField.getMandatoryTableHeaders().toString();
        String tableClearSubProcesFields = templateField.getClearSubProcessFields().toString();

        dynamicFieldTable.setTable_name(tableName);
        dynamicFieldTable.setTable_headers_array(tableHeaderArray);
        dynamicFieldTable.setTable_keys_array(tableKeysArray);
        //dynamicFieldTable.setMandatory_table_fields(tableManditoryFields);
        dynamicFieldTable.setMandatory_table_headers(tableManditoryHeaders);
        dynamicFieldTable.setClear_subprocess_fields(tableClearSubProcesFields);
        DynamicFieldTableDB.updateDynamicFieldTable(dynamicFieldTable, dbHelper);
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Updated DynamicFieldTable");
    }

    private static String insertTemplateJsonFields(UpdatedTemplet.SubProcessFieldsDatum templateField, String templateFieldJson, String client_template_id) {
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Inserting Template Json fields");
        TemplateJson templateJson = new TemplateJson();
        String tableExist = String.valueOf(templateField.getTableExist());
        String controllerName = templateField.getControllerName();
        String mandatoryFieldKeys = "";
        String allFieldKeys = "";
        List<SubProcessFieldDataResponse.SubProcessField> arrayListSubProcessField = templateField.getSubProcessFields();
        //get all keys and mandatory keys
        int subProcessFieldCnt = 0;
        for (subProcessFieldCnt = 0; subProcessFieldCnt < arrayListSubProcessField.size() - 1; subProcessFieldCnt++) {
            String key = arrayListSubProcessField.get(subProcessFieldCnt).getKey();
            allFieldKeys = allFieldKeys + key + ",";
            boolean isManditory = arrayListSubProcessField.get(subProcessFieldCnt).getIsMandatory();
            if (isManditory) {
                mandatoryFieldKeys = mandatoryFieldKeys + key + ",";
            }
        }
        String key = arrayListSubProcessField.get(subProcessFieldCnt).getKey();
        allFieldKeys = allFieldKeys + key;
        boolean isManditory = arrayListSubProcessField.get(subProcessFieldCnt).getIsMandatory();
        int length = mandatoryFieldKeys.length();
        if (isManditory) {
            mandatoryFieldKeys = mandatoryFieldKeys + key;
        } else {
            if (mandatoryFieldKeys.length() > 0) {
                mandatoryFieldKeys = mandatoryFieldKeys.substring(0, length - 1);
            }
        }

        templateJson.setForm_name(templateField.getSubProcessName());
        templateJson.setField_json(templateFieldJson);
        templateJson.setClient_template_id(client_template_id);
        templateJson.setIs_table_exists(tableExist);
        templateJson.setController_name(controllerName);
        templateJson.setMandatory_field_keys(mandatoryFieldKeys);
        templateJson.setOther_field_keys(allFieldKeys);
        long json_template_id = TemplateJsonDB.addTemplateJsonEntry(templateJson, dbHelper);
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Inserted Template Json fields");
        return String.valueOf(json_template_id);
    }

    private static String updateTemplateJsonFields(TemplateJson templateJson, UpdatedTemplet.SubProcessFieldsDatum templateField, String templateFieldJson) {
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Updating Template Json fields");
        String tableExist = String.valueOf(templateField.getTableExist());
        String controllerName = templateField.getControllerName();
        String mandatoryFieldKeys = "";
        String allFieldKeys = "";
        List<SubProcessFieldDataResponse.SubProcessField> arrayListSubProcessField = templateField.getSubProcessFields();
        //get all keys and mandatory keys
        int subProcessFieldCnt = 0;
        for (subProcessFieldCnt = 0; subProcessFieldCnt < arrayListSubProcessField.size() - 1; subProcessFieldCnt++) {
            String key = arrayListSubProcessField.get(subProcessFieldCnt).getKey();
            allFieldKeys = allFieldKeys + key + ",";
            boolean isManditory = arrayListSubProcessField.get(subProcessFieldCnt).getIsMandatory();
            if (isManditory) {
                mandatoryFieldKeys = mandatoryFieldKeys + key + ",";
            }
        }
        String key = arrayListSubProcessField.get(subProcessFieldCnt).getKey();
        allFieldKeys = allFieldKeys + key;
        boolean isManditory = arrayListSubProcessField.get(subProcessFieldCnt).getIsMandatory();
        int length = mandatoryFieldKeys.length();
        if (isManditory) {
            mandatoryFieldKeys = mandatoryFieldKeys + key;
        } else {
            if (mandatoryFieldKeys.length() > 0) {
                mandatoryFieldKeys = mandatoryFieldKeys.substring(0, length - 1);
            }
        }


        templateJson.setField_json(templateFieldJson);
        templateJson.setIs_table_exists(tableExist);
        templateJson.setController_name(controllerName);
        templateJson.setMandatory_field_keys(mandatoryFieldKeys);
        templateJson.setOther_field_keys(allFieldKeys);
        TemplateJsonDB.updateTemplateJson(templateJson, dbHelper);
        String json_template_id = templateJson.getID();
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Updated Template Json fields");
        return json_template_id;
    }

    private static void insertTemplateInTable(UpdatedTemplet.TabData templateMaster, List<UpdatedTemplet.SubProcessFieldsDatum> subProcessFieldsData) {
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Inserting Template in DB");
        ClientTemplate clientTemplate = new ClientTemplate();
        List<String> arrayListFormNames = templateMaster.getTabs().get(0).getSubProcesses();
        //-------Start of template to be added in client_template table ------///
        String form_array = arrayListFormNames.toString();
        String client_name = templateMaster.getNbfcname();
        String template_name = templateMaster.getTemplateName();

        clientTemplate.setIs_deprecated("false");
        clientTemplate.setClient_name(client_name);
        clientTemplate.setTemplate_name(template_name);
        clientTemplate.setVersion(templateMaster.getVersionNo());
        clientTemplate.setTemplate_form_name_array(form_array);
        clientTemplate.setLanguage(UserPreference.getLanguage(mContext));

        long client_template_id = ClientTemplateDB.addClientTemplateEntry(clientTemplate, dbHelper);

        //-------End of template to be added in client_template table ------///

        //-------Start of field to be added in json_template,field_table ------///
        //UpdatedTemplet.SubProcessField subProcessFieldDataResponse = gson.fromJson(String.valueOf(subProcessFieldsData),  UpdatedTemplet.SubProcessField.class);
        List<UpdatedTemplet.SubProcessFieldsDatum> arrayListTemplateField = subProcessFieldsData;
        for (int formNameCnt = 0; formNameCnt < arrayListFormNames.size(); formNameCnt++) {
            String formName = arrayListFormNames.get(formNameCnt);
            String templateName = clientTemplate.getTemplate_name();
            for (int templateFieldCnt = 0; templateFieldCnt < arrayListTemplateField.size(); templateFieldCnt++) {
                String fieldFormName = arrayListTemplateField.get(templateFieldCnt).getSubProcessName();
                //as server is not sending template name removing it//String fieldTemplateName = arrayListTemplateField.get(templateFieldCnt).getTemplateName();
                //check for equality
                if (formName.equalsIgnoreCase(fieldFormName)) {
                    //get row to be updated from json_template table
                    UpdatedTemplet.SubProcessFieldsDatum templateField = arrayListTemplateField.get(templateFieldCnt);
                    String templateFieldJson = gson.toJson(templateField);
                    String jsonTemplateId = null;

                    //insert template json fields
                    jsonTemplateId = insertTemplateJsonFields(templateField, templateFieldJson, String.valueOf(client_template_id));

                    //check for table exist and enter all data in field_table
                    if (templateField.getTableExist()) {
                        //insert dynamic table entry
                        insertDynamicFieldTable(templateField, jsonTemplateId);
                    }
                    //insertDynamicFieldTable(templateField, jsonTemplateId);

                }
            }
        }
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Inserted Template in DB");
        //-------End of field to be added in json_template,field_table ------///
    }


    public static void getFormsFromDBAndInsertIntoMenus(int clientTemplateId, Context context) {
        dbHelper = DBHelper.getInstance(context);
        List<String> formNameList = new ArrayList<>();
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Inserting Template forms menus in DB");
        int loginJsonTemplateId = LoginTemplateDB.getLoginJsontemplateID(dbHelper, "Menu Details", UserPreference.getLanguage(context));

        if (loginJsonTemplateId > 0) {
            int categoryId = CategoryDB.getCategoryIdDependsOnLoginJsonID(dbHelper, String.valueOf(loginJsonTemplateId));
            SubCategoryDB.deleteMenusFromDB(dbHelper, String.valueOf(categoryId), "true");
            List<SubCategory> subCategoryMenuList = new ArrayList<>();
            subCategoryMenuList = SubCategoryDB.getSubCategoriesDependsOnCategory(dbHelper, String.valueOf(loginJsonTemplateId));
            int seqNo = subCategoryMenuList.size();
            //SubCategoryDB.deleteStaticMenusFromDB(dbHelper, String.valueOf(categoryId), context.getString(R.string.pending_menu));
            // SubCategoryDB.deleteStaticMenusFromDB(dbHelper, String.valueOf(categoryId), context.getString(R.string.upload_menu));
            formNameList = TemplateJsonDB.getAllFormNamesDependsOnClientTemplateID(dbHelper, String.valueOf(clientTemplateId));
            for (int i = 0; i < formNameList.size(); i++) {
                if (!SubCategoryDB.checkSubCategoryPresentOrNot(dbHelper, formNameList.get(i))) {
                    seqNo++;
                    SubCategory subCategory = new SubCategory();
                    subCategory.setSubcategory_name(formNameList.get(i));
                    subCategory.setCategory_id(String.valueOf(categoryId));
                    subCategory.setSequence_no(String.valueOf(seqNo));
                    subCategory.setIsFormMenu("true");
                    subCategory.setIcon(String.valueOf(formNameList.get(i)));
                    long id = SubCategoryDB.addSubCategoryEntry(subCategory, dbHelper);
                    Log.i("ID", "" + id);
                }
            }
        }
    }

    public static void addStaticMenus(int categoryId, int seqId, Context context) {
        mContext = context;
        dbHelper = DBHelper.getInstance(mContext);
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Inserting Static menus in DB");
        String staticMenuArray[] = {mContext.getString(R.string.pending_menu)};
        for (int j = 0; j < staticMenuArray.length; j++) {
            if (!SubCategoryDB.checkSubCategoryPresentOrNot(dbHelper, staticMenuArray[j])) {
                seqId++;
                SubCategory subCategory = new SubCategory();
                subCategory.setSubcategory_name(staticMenuArray[j]);
                subCategory.setCategory_id(String.valueOf(categoryId));
                subCategory.setSequence_no(String.valueOf(seqId));
                subCategory.setIsFormMenu("false");
                subCategory.setIcon("upload_pending.png");
                SubCategoryDB.addSubCategoryEntry(subCategory, dbHelper);
            }
        }
    }
}
