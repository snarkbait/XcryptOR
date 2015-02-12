/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snarkbait;

import java.io.File;
import java.security.MessageDigest;
import java.util.Random;

/**
 *
 * @author tim
 */
public class Encryptor {
    
    private final long[] key;
    private int bits;
    private File file;
    private static byte[] keyBytes;
    private long origCRC32;
    private String origFileName;
    private String outFileName;
    
    private static final int LOWERCASE_ASCII_MIN = 65;
    private static final int NUMBER_ASCII_MIN = 48;
    
    private static Random rndgen = new Random();
    

    
    public Encryptor()
    {
        key = new long[4];
        bits = 32;
    }
    
    public Encryptor(long[] k)
    {
        key = k;
        //initKeyBytes();
    }
    
    public Encryptor(File f, long[] k)
    {
        key = k;
        file = f;
        //initKeyBytes();
    }
    
    public void setFile(File f)
    {
        file = f;
    }
    
    public String getFileParent()
    {
        return file.getParent();
    }
    
    public void setKey(long value, int index)
    {
        key[index] = value;
        loadKeyBytes(index);
    }
    public int getKey(int index)
    {
        return (int)key[index];
    }
    
    public int getBits()
    {
        return bits;
    }
    
    public void setBits(int bits)
    {
        this.bits = bits;
        initKeyBytes();
    }
    
    public long getOrigCRC32()
    {
        return origCRC32;
    }
    
    public void setOrigCRC32(long crc)
    {
        origCRC32 = crc;
    }
    
    public String getOrigFileName()
    {
        return origFileName;
    }
    
    public String getOutFileName()
    {
        return outFileName;
    }
    public void setOutFileName(String name)
    {
        outFileName = name;
    }
    private void initKeyBytes()
    {
        keyBytes = new byte[bits/8];
        

    }
    
    private void loadKeyBytes(int index)
    {
        keyBytes[0 + (index * 4)] = (byte)(key[index] & 0xFF);
        keyBytes[1 + (index * 4)] = (byte)((key[index] >> 8) & 0xFF);
        keyBytes[2 + (index * 4)] = (byte)((key[index] >> 16) & 0xFF);
        keyBytes[3 + (index * 4)] = (byte)((key[index] >> 24) & 0xFF);
        
    }
    
    public void setHashKey(byte[] b)
    {
        //initKeyBytes();
        System.arraycopy(b, 0, keyBytes, 0, bits/8);
        String s = "";
        for (int i=0;i<b.length;i++)
        {
            s += toHexString((int)b[i], 2);
	}

        for (int i = 0; i < bits/32; i++)
        {
            
            key[i] = Long.parseLong(s.substring(0 + (i * 8),8 + (i * 8)), 16);            
         }

    }
    
    public void getRandomKey()
    {
       int[] k = new int[4];
       //initKeyBytes();
       for (int j = 0; j < bits/32; j++)
        {
            for (int i = 3; i>= 0; i--)
            {
                k[i] = getRandomInt(11,255) << (i*8);
            }
        key[j] = k[3] | k[2] | k[1] | k[0];
        loadKeyBytes(j);
        }
    }
    
    public Bank encrypt(byte[] inBank)
    {
        // create a temporary bank to store the encrypted data
        // same size as original bank
        byte[] tempBank = new byte[inBank.length];
        
        for (int i = 0; i < inBank.length; i++)
        {
            if (i < 12) // this is for the 'header' information, do not encrypt
            {
                tempBank[i] = inBank[i];
            }
            else // encrypt byte
            {
                tempBank[i] = (byte)(inBank[i] ^ keyBytes[i%(bits/8)]);
            }
        }
        Bank returnBank = new Bank(tempBank); // turn into 'Bank' object
        return returnBank;
    }
    
    public static byte[] getKeySHA(String phrase, int numBits)
    {
        try 
        {
            MessageDigest md  = MessageDigest.getInstance("SHA-256");
            md.update(phrase.getBytes("UTF-8"));
            byte[] hash = md.digest();
//            String s = "";
//            for (int i=0;i<hash.length;i++)
//            {
//		s += toHexString((int)hash[i], 2);
//            }
            return hash;
             
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            return null;
        }
        
    }
    private static int getRandomInt(int low, int high)
    {
            return rndgen.nextInt(high - low + 1) + low;
    }
    
    public static String toHexString(int value, int len)
    {
        String retval = "";
        
        // we need to shift in groups of 4-bits this time
        for (int i = (len-1)*4; i >= 0; i-=4)
        {
            // shift rgb 4 bits at a time, mask 
            int j = value >> i & 0xF;
            
            // if 10 - 15 we need a - f
            if (j > 9)
            {
                j += LOWERCASE_ASCII_MIN - 10; // ASCII characters a - f
                
            }
            else
            {
                j += NUMBER_ASCII_MIN; // ASCII characters 0 - 9
            }
            retval += (char) j; // convert ASCII int value to char
        }
        return retval;
    }
    public String keyToString()
    {
        String s = "";
        for (int i = 0; i < bits/32; i++)
        {
            s += toHexString((int)key[i], 8);
            if (i < bits/32-1)
            {
                s += "-";
            }
        }
        return s;
            
    }
}
