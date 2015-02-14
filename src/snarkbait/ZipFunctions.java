/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snarkbait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
// import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 *
 * @author tim
 */
public class ZipFunctions 
{
    private static final byte[] header = { 0x58, 0x43, 0x52, 0x59, 0x50, 0x54, 0x4f, 0x52 };
                                            // text 'XCRYPTOR' in bytes
    private static final byte version = 1;
    
    public static byte[] makeHeader(byte flags, String filename, long crc)
    {
        int fLen = filename.length();
        
        // create large enough bank
        byte[] hdrBank = new byte[18 + fLen];
        
        // put text header at beginning
        System.arraycopy(header, 0, hdrBank, 0, 8);
        
        // put version
        hdrBank[8] = version;
        
        //put bit flags
        hdrBank[9] = flags;
        
        // put original file CRC32
        hdrBank[10] = (byte)((crc >> 24) & 0xff);
        hdrBank[11] = (byte)((crc >> 16) & 0xff);
        hdrBank[12] = (byte)((crc >> 8) & 0xff);
        hdrBank[13] = (byte) (crc & 0xff);
        
        // put filename length integer as bytes
        hdrBank[14] = (byte) ((fLen >> 24) & 0xff);
        hdrBank[15] = (byte) ((fLen >> 16) & 0xff);
        hdrBank[16] = (byte) ((fLen >> 8) & 0xff);
        hdrBank[17] = (byte) (fLen  & 0xff);
        
        // put filename chars
        for (int j = 0;j<fLen; j++)
        {
            hdrBank[j + 18] = (byte)filename.charAt(j);
        }
        return hdrBank;
    }
    
    public static byte[] addHeader(byte[] head, byte[] compbank)
    {
        // get new size
        byte[] data = new byte[head.length + compbank.length];
        
        // copy header
        System.arraycopy(head, 0, data, 0, head.length);
        
        // copy data
        System.arraycopy(compbank, 0, data, head.length, compbank.length);
        return data;
    }
    
    public static void saveSettings(Settings settings)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream("settings.dat", false);
            ObjectOutputStream objos = new ObjectOutputStream(fos);
            objos.writeObject(settings);
            objos.close();
        }
        catch ( FileNotFoundException fnfe)
        {
            FileEncryptGUI.toConsole("Unable to save settings file");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static boolean isEncrypted(byte[] inBank)
    {
        for (int i = 0; i < 8; i++) {
            if (header[i] != inBank[i])
            {
                return false;
            }            
        }
        return true;
    }
    
    public static BitFlag getFlagsFromHeader(byte[] inBank)
    {
        return new BitFlag((int)inBank[9] &0xff);
    }
    
    public static long getCRC32FromHeader(byte[] inBank)
    {
        return (inBank[10] << 24) | (inBank[11] << 16) | (inBank[12] << 8) | inBank[13];
    }
    
    public static String getFileNameFromHeader(byte[] inBank)
    {
        String retval = "";
        int size = (inBank[14] << 24) | (inBank[15] << 16) | (inBank[16] << 8) | inBank[17];
        for (int i = 18; i < size; i++) 
        {
            retval+= (char)inBank[i];    
        }
        return retval;
    }
    public static Settings loadSettings()
    {
        Settings temp = null;
        try
        {
            FileInputStream fis = new FileInputStream("settings.dat");
            ObjectInputStream objIn = new ObjectInputStream(fis);
            
            temp = (Settings) objIn.readObject();
            objIn.close();
        }
        catch ( FileNotFoundException fnfe)
        {
            System.out.println("unable to find settings file");
        }
        catch ( IOException e)
        {
            e.printStackTrace();
        }
        catch ( ClassNotFoundException cnfe)
        {
            System.out.println(cnfe.getMessage());
        }
        return temp;
    }
    public static long getCrc32(byte[] b)
    {
        CRC32 crc = new CRC32();
        crc.update(b);
        return crc.getValue();
    }
    
    public static void bufferToFile(byte[] b, File file, String newFile)
    {
       File nFile = new File(file.getParent() + "/" + newFile);
       try (FileOutputStream fos = new FileOutputStream(nFile))
        {
            
            if (!nFile.exists()) 
            {
		nFile.createNewFile();
            }
 

            fos.write(b);
            fos.flush();
            fos.close();

            FileEncryptGUI.toConsole("success!");
 
	} catch (IOException e) 
        {
			e.printStackTrace();
	}
    }
    public static Bank fileToBuffer(File file)
    {
        
        try 
        {
            Path path = file.toPath();
            byte[] b = Files.readAllBytes(path);
            Bank data = new Bank(b);
            return data;
           
        }
        catch ( java.io.IOException ex)
        {
           System.out.println("File not found");
        }
        return null;
    }
    
    public static String getFileNameTime()
    {
        long m = System.currentTimeMillis();
        return Long.toString(m & 0xffff);
    }
    
    public static Bank compress(byte[] inBank)
    {
        Deflater def = new Deflater();
        def.setInput(inBank, 10, inBank.length - 10);
        byte[] temp = new byte[inBank.length];
        def.finish();
        int bLength = def.deflate(temp);
        def.end();
        byte[] outBank = new byte[bLength + 10];
        System.arraycopy(inBank, 0, outBank, 0, 10);
        System.arraycopy(temp, 0, outBank, 10, bLength);
        Bank compBank = new Bank(outBank);
        return compBank;
       
        
    }
    
    public static byte[] unCompress(byte[] b, int len)
    {
        
        try 
        {
            Inflater inf = new Inflater();
            inf.setInput(b, 0, b.length);
            byte[] bank = new byte[len];
            int bLength = inf.inflate(bank);
            inf.end();
            return bank;
        }
        catch ( java.util.zip.DataFormatException ex)
        {
            System.out.println("Data Format exception");
        }
                
        return null;
    }
}
