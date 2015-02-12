/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snarkbait;

import java.io.Serializable;

/**
 *
 * @author tim
 */
public class Settings implements Serializable
{
    private int flags;
    private String outFileName;
    private boolean deleteOrig;
    private boolean keyEnter;
    private boolean saveOrig;
    private boolean saveEnter;

    public Settings()
    {
        flags = 0;
        outFileName = "";
        deleteOrig = false;
        keyEnter = false;
        saveOrig = false;
        saveEnter = false;
    }
    
    public Settings(int pFlags, String pOutFile, boolean pDelOrig, boolean pKeyEnter, boolean pSaveOrig, boolean pSaveEnter)
    {
        flags = pFlags;
        outFileName = pOutFile;
        deleteOrig = pDelOrig;
        keyEnter = pKeyEnter;
        saveOrig = pSaveOrig;
        saveEnter = pSaveEnter;
        
    }
    
    public int getFlags()
    {
        return flags;
    }
    public void setFlags(int i)
    {
        flags = i;
    }
    public String getOutFileName()
    {
        return outFileName;
    }
    public void setOutFileName(String s)
    {
        outFileName = s;
    }
    public boolean getDeleteOrig()
    {
        return deleteOrig;
    }
    public void setDeleteOrig(boolean b)
    {
        deleteOrig = b;
    }
    public void toggleDeleteOrig()
    {
        deleteOrig = !deleteOrig;
    }
    public boolean getKeyEnter()
    {
        return keyEnter;
    }
    public void setKeyEnter(boolean b)
    {
        keyEnter = b;
    }
    public void toggleKeyEnter()
    {
        keyEnter = !keyEnter;
    }
    public boolean getSaveOrig()
    {
        return saveOrig;
    }
    public void setSaveOrig(boolean b)
    {
        saveOrig = b;
    }
    public void toggleSaveOrig()
    {
        saveOrig = !saveOrig;
    }
    public boolean getSaveEnter()
    {
        return saveEnter;
    }
    public void setSaveEnter(boolean b)
    {
        saveEnter = b;
    }
    public void toggleSaveEnter()
    {
        saveEnter = !saveEnter;
    }
    @Override
    public String toString()
    {
       return "Flags: " + flags + " : Output file name: " + outFileName + "\n" +
               "Delete orig: " + deleteOrig + " : Key enter: " + keyEnter + "\n" +
               "Save Orig: " + saveOrig + " : Save enter: " + saveEnter + "\n";
    }
}
