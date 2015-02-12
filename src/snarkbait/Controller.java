/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snarkbait;

import java.io.File;

/** Class Controller<br>
 *
 * @author snarkbait
 */
public class Controller 
{
    
    private final Encryptor encryptFile;
    private Bank fileBank;
    private Bank compressBank;
    private Bank encryptBank;
    
    public Controller()
    {
        encryptFile = new Encryptor();
    }
    
    public byte[] getFileBank()
    {
        return fileBank.getBank();
    }
    
    public byte[] getCompressBank()
    {
        return compressBank.getBank();
    }
    
    public void setCompressBank(byte[] b)
    {
        compressBank = new Bank(b);
    }
    
    public byte[] getEncryptBank()
    {
        return encryptBank.getBank();
    }
    
    public boolean initFile(File f)
    {
        encryptFile.setFile(f);
        toConsole("Attempting to read file..." + f.getName());
        fileBank = ZipFunctions.fileToBuffer(f);
        toConsole("File contents CRC32 checksum = " + fileBank.getCRC32Hex());
        return ZipFunctions.isEncrypted(fileBank.getBank());
    }
    
    public byte[] addHeaders(byte flags)
    {
        byte[] tempBank = ZipFunctions.makeHeader(flags, encryptFile.getOutFileName(), fileBank.getCRC32());
        return ZipFunctions.addHeader(tempBank, fileBank.getBank());
    }
    
    public void readHeader()
    {
        
    }
    
// <editor-fold defaultstate="collapsed" desc="Encryptor class wrapper functions">
    // Encryptor wrapper functions
    
    /** keyToString
     * returns value from <b>Encryptor.keyToString</b>
     * @return 32, 64 or 128 bit hex string of encryption key
     */
    public String keyToString()
    {
        return encryptFile.keyToString();
    }
    
    /** setBits(int bits)
     * sets value to Encryptor.setBits
     * @param bits The bit level of the encryption key, 32, 64 or 128
     */
    public void setBits(int bits)
    {
        encryptFile.setBits(bits);
    }
    public int getBits()
    {
        return encryptFile.getBits();
    }
    public void getRandomKey()
    {
        encryptFile.getRandomKey();
    }
    public void setHashKey(byte[] keySHA)
    {
        encryptFile.setHashKey(keySHA);
    }
    public void setKey(long value, int index)
    {
        encryptFile.setKey(value, index);
    }
    public int getKey(int index)
    {
        return encryptFile.getKey(index);
    }
    public void setOutFileName(String name)
    {
        encryptFile.setOutFileName(name);
    }
//</editor-fold>            
    
    public void compressFile(byte[] inBank)
    {
        compressBank = ZipFunctions.compress(inBank);
        toConsole("Compressed contents CRC32 checksum = " + compressBank.getCRC32Hex());
    }
    
    
    public void encrypt()
    {
        encryptBank = encryptFile.encrypt(compressBank.getBank());
        toConsole("Encrypted contents CRC32 checksum = " + encryptBank.getCRC32Hex());
        
    }
    
    public void saveFile(File f)
    {
        String outPath = encryptFile.getFileParent() + "\\" + encryptFile.getOutFileName();
        toConsole("Attempting to write " + outPath);
        ZipFunctions.bufferToFile(encryptBank.getBank(), f, encryptFile.getOutFileName());
    }
    
    public static void toConsole(String s)
    {
        FileEncryptGUI.toConsole(s);
    }
}
