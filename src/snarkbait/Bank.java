/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snarkbait;

/** Class<b>Bank</b><br>
 * Creates memory bank byte[ ] arrays and automatically gets the <b>CRC32</b> checksum.<br>
 * 
 * @author snarkbait
 */
public class Bank {
    
    private final byte[] bank;
    private long crc32;
    
    public Bank(int size)
    {
        bank = new byte[size];
        crc32 = 0;
    }
    
    public Bank(byte[] b)
    {
        bank = b;
        setCRC32();
    }
    
    public byte[] getBank()
    {
        return bank;
    }
    
    public int getSize()
    {
        return bank.length;
    }
    
    public void setCRC32()
    {
        crc32 = ZipFunctions.getCrc32(bank);
    }
    
    public long getCRC32()
    {
        return crc32;
    }
    
    public String getCRC32Hex()
    {
        return "0x" + Encryptor.toHexString((int)crc32,8);
    }
    
    
}
