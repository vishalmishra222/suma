package com.tom_roush.pdfbox.pdmodel.documentinterchange.logicalstructure;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import com.tom_roush.pdfbox.cos.COSArray;
import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.common.COSDictionaryMap;
import com.tom_roush.pdfbox.pdmodel.common.PDNameTreeNode;
import com.tom_roush.pdfbox.pdmodel.common.PDNumberTreeNode;

import android.util.Log;

/**
 * A root of a structure tree.
 * 
 * @author Ben Litchfield
 * @author Johannes Koch
 */
public class PDStructureTreeRoot extends PDStructureNode
{
    private static final String TYPE = "StructTreeRoot";

    /**
     * Default Constructor.
     * 
     */
    public PDStructureTreeRoot()
    {
        super(TYPE);
    }

    /**
     * Constructor for an existing structure element.
     * 
     * @param dic The existing dictionary.
     */
    public PDStructureTreeRoot(COSDictionary dic)
    {
        super(dic);
    }

    /**
     * Returns the K array entry.
     * 
     * @return the K array entry
     */
    public COSArray getKArray()
    {
        COSBase k = this.getCOSDictionary().getDictionaryObject(COSName.K);
        if (k != null)
        {
            if (k instanceof COSDictionary)
            {
                COSDictionary kdict = (COSDictionary) k;
                k = kdict.getDictionaryObject(COSName.K);
                if (k instanceof COSArray)
                {
                    return (COSArray) k;
                }
            }
            else
            {
                return (COSArray) k;
            }
        }
        return null;
    }

    /**
     * Returns the K entry.
     * 
     * @return the K entry
     */
    public COSBase getK()
    {
        return this.getCOSDictionary().getDictionaryObject(COSName.K);
    }

    /**
     * Sets the K entry.
     * 
     * @param k the K value
     */
    public void setK(COSBase k)
    {
        this.getCOSDictionary().setItem(COSName.K, k);
    }

    /**
     * Returns the ID tree.
     * 
     * @return the ID tree
     */
    public PDNameTreeNode getIDTree()
    {
        COSDictionary idTreeDic = (COSDictionary) this.getCOSDictionary().getDictionaryObject(COSName.ID_TREE);
        if (idTreeDic != null)
        {
            return new PDNameTreeNode(idTreeDic, PDStructureElement.class);
        }
        return null;
    }

    /**
     * Sets the ID tree.
     * 
     * @param idTree the ID tree
     */
    public void setIDTree(PDNameTreeNode idTree)
    {
        this.getCOSDictionary().setItem(COSName.ID_TREE, idTree);
    }

    /**
     * Returns the parent tree.
     * 
     * @return the parent tree
     */
    public PDNumberTreeNode getParentTree()
    {
        COSDictionary parentTreeDic = (COSDictionary) this.getCOSDictionary().getDictionaryObject(COSName.PARENT_TREE);
        if (parentTreeDic != null)
        {
            return new PDNumberTreeNode(parentTreeDic, COSBase.class);
        }
        return null;
    }

    /**
     * Sets the parent tree.
     * 
     * @param parentTree the parent tree
     */
    public void setParentTree(PDNumberTreeNode parentTree)
    {
        this.getCOSDictionary().setItem(COSName.PARENT_TREE, parentTree);
    }

    /**
     * Returns the next key in the parent tree.
     * 
     * @return the next key in the parent tree
     */
    public int getParentTreeNextKey()
    {
        return this.getCOSDictionary().getInt(COSName.PARENT_TREE_NEXT_KEY);
    }

    /**
     * Sets the next key in the parent tree.
     * 
     * @param parentTreeNextkey the next key in the parent tree.
     */
    public void setParentTreeNextKey(int parentTreeNextkey)
    {
        this.getCOSDictionary().setInt(COSName.PARENT_TREE_NEXT_KEY, parentTreeNextkey);
    }

    /**
     * Returns the role map.
     * 
     * @return the role map
     */
    public Map<String, Object> getRoleMap()
    {
        COSBase rm = this.getCOSDictionary().getDictionaryObject(COSName.ROLE_MAP);
        if (rm instanceof COSDictionary)
        {
            try
            {
                return COSDictionaryMap.convertBasicTypesToMap((COSDictionary) rm);
            }
            catch (IOException e)
            {
            	Log.e("PdfBoxAndroid", e.getMessage(),e);
            }
        }
        return new Hashtable<String, Object>();
    }

    /**
     * Sets the role map.
     * 
     * @param roleMap the role map
     */
    public void setRoleMap(Map<String, String> roleMap)
    {
        COSDictionary rmDic = new COSDictionary();
        for (Map.Entry<String, String> entry : roleMap.entrySet())
        {
        	rmDic.setName(entry.getKey(), entry.getValue());
        }
        this.getCOSDictionary().setItem(COSName.ROLE_MAP, rmDic);
    }

}
